package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;
import com.paypal.orders.Order;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.mail.ISendEmailService;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.CouponRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.OrderRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.*;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf.CouponHtmlGenerator;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf.HtmlToPdfTransformer;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.StoreRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.ConfirmPaymentDto;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.CouponOptionDto;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.OrderDto;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.StoreDto;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.service.CouponNumberRangeService;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin
public class FrontendRestController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private HtmlToPdfTransformer htmlToPdfTransformer;
    
    @Autowired
    private CouponHtmlGenerator couponHtmlGenerator;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponNumberRangeService couponNumberRangeService;
    
    @Autowired
    private ISendEmailService sendEmailService;
    
    @Value("${frontend.url}")
    private String frontendBaseUrl;
    
    private static Logger logger = LoggerFactory.getLogger(FrontendRestController.class); 

    @GetMapping("/stores")
    public List<StoreDto> getAllStores() {
        // using DTO because we do not want to expose paypal credentials
        return storeRepository.findAll().stream()
                .map(s -> StoreDto.fromStore(s, false))
                .collect(Collectors.toList());
    }

    @GetMapping("/stores/{id}")
    public StoreDto getStore(@PathVariable String id) {
        Store store = storeRepository.findById(id).orElseThrow(null);
        return StoreDto.fromStore(store, true);
    }

    @GetMapping("/couponOption/{storeId}/{id}")
    public CouponOptionDto getCouponOption(@PathVariable String storeId, @PathVariable String id) {
        CouponTemplate couponTemplate = getCouponTemplate(storeId, id);
        return CouponOptionDto.fromCouponTemplate(couponTemplate);
    }

    private CouponTemplate getCouponTemplate(@PathVariable String storeId, @PathVariable String couponTemplateId) {
        Store store = storeRepository.findById(storeId).get();
        return store.getCouponTemplateById(couponTemplateId);
    }

    /**
     * @param storeId Store ID
     * @return Paypal URL for payment
     */
    @PostMapping("/stores/{storeId}/orderCoupon/{couponId}")
    public String orderCoupon(@PathVariable String storeId, @PathVariable String couponId, @RequestBody OrderDto orderDto) {
        Store store = storeRepository.findById(storeId).orElseThrow(null);
        CouponTemplate couponTemplate = getCouponTemplate(storeId, couponId);

        de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order order = new de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order();

        order.setOrderedCoupon(couponTemplate);
        order.setCouponPrice(couponTemplate.getPrice());
        order.setCouponText(orderDto.getCouponText());
        order.setOrdererEmail(orderDto.getEmail());
        order.setOrdererFirstName(orderDto.getFirstName());
        order.setOrdererLastName(orderDto.getLastName());
        order.setStore(store);
        
        orderRepository.save(order);
        
        logger.info("Created order " + order.getId() + " for " + order.getOrdererEmail());

        // HOWTO register at paypal: https://developer.paypal.com/
        // Create REST Application
        // Copy Client ID and Secret

        PayPalHttpClient client = getPayPalHttpClient(storeId);

        // Construct a request object and set desired parameters
        // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        orderRequest.applicationContext(
                new ApplicationContext()
                        .cancelUrl(frontendBaseUrl + "/#/store/" + storeId)
                        .returnUrl(frontendBaseUrl + "/#/store/" + storeId  + "/payment-result/" + couponId)
        );


        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits
                .add(new PurchaseUnitRequest()
                        .amountWithBreakdown(new AmountWithBreakdown().currencyCode("EUR").value(String.valueOf(couponTemplate.getPrice()))));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            // Call API with your client and get a response for your call
            HttpResponse<Order> response = client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            Order paypalOrder = response.result();

            // store paypal data on transaction
            order.setPaypalTransactionId(paypalOrder.id());
            order.setStatus(OrderStatus.PAYMENT_PENDING);
            orderRepository.save(order);

            List<LinkDescription> links = paypalOrder.links();
            //links.forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
            logger.info("Got Paypal ID: " + paypalOrder.id() + " for order with ID " + order.getId());

            String approvalLink = getApprovalLink(links);

            logger.info("Sending user to " + approvalLink + " for order " + order.getId());
            
            return approvalLink;
        } catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                
                System.out.println(he.getMessage());
            } else {
                // Something went wrong client-side
            }
            
            throw new RuntimeException(ioe);
        }
        
    }

    @PostMapping("/confirmPayment/{id}")
    public void confirmPayment(@PathVariable String /* storeId */ id,  @RequestBody ConfirmPaymentDto confirmPaymentDto) {
        String transactionId = confirmPaymentDto.getToken();
        logger.info("now confirming payment " + transactionId);

        de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order order = orderRepository.findByPaypalTransactionId(transactionId);
        if(order == null) {
            throw new IllegalStateException("Could not find order for confirmed paypal transaction id " + transactionId);
        }

        logger.info("Payment " + transactionId + " for order " + order.getId() + " is confirmed, now trying to capture");

        try {
            PayPalHttpClient payPalHttpClient = getPayPalHttpClient(id);

            // token seems to be the orderId
            OrdersCaptureRequest request = new OrdersCaptureRequest(transactionId);
            
            // now capture paypal transaction
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            logger.info("Payment " + transactionId + " for order " + order.getId() + " was captured successfully");
            Order paypalOrder = response.result();
            String captureId = paypalOrder.purchaseUnits().get(0).payments().captures().get(0).id();
            logger.info("Capture ID is " + captureId);

            // here we got our money - lets safe that immediately
            order.markPaid();
            order.setPaypalCaptureId(captureId);
            orderRepository.save(order);

        } catch (Exception ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                if (he.getMessage().contains("ORDER_ALREADY_CAPTURED")) {
                    logger.info("Re-capturing order. " + he.getMessage());

                    // ist ok - vlt. haben wir den browser aktualisiert oder so
                    return;
                }

                throw new RuntimeException("Error processing paypal", ioe);


            } else {
                // Something went wrong client-side
            }

            throw new RuntimeException(ioe);
        }


        Coupon coupon;
        try {
            logger.info("Creating coupon for order " + order.getId());
            // create coupon
            coupon = new Coupon();
            coupon.setOrder(order);
            coupon.setTemplateId(order.getOrderedCouponTemplateId());

            CouponNumberRange couponNumberRange = couponNumberRangeService.fetchCouponNumberAndMarkAsUsed(order.getStore());
            coupon.setCouponNumber(couponNumberRange.getCouponNumber());
            order.setCoupon(coupon);

            Binary couponPdf = generateCouponPdf(coupon);
            coupon.setGeneratedPdf(couponPdf);

            // need to safe coupon first for DbRef
            couponRepository.save(coupon);
            orderRepository.save(order);

        } catch (Exception e) {
            logger.error("Error creating coupon", e);
            throw new RuntimeException("Error creating coupon", e);
        }
            
        try {
            logger.info("Now sending couping mail for order " + order.getId());
            sendEmailService.sendCouponMailToOrdererAsync(coupon);
            
            order.markSent();
            orderRepository.save(order);

            logger.info("Order " + order.getId() + " completed successfully");
            
        } catch (MessagingException e) {
            logger.error("Error sending mail", e);
            throw new RuntimeException("Error sending mail", e);
        }
    }

    private Binary generateCouponPdf(Coupon coupon) {
        try {
            String couponHtml = couponHtmlGenerator.generate(coupon);

            return htmlToPdfTransformer.transformToPdf(couponHtml);
        } catch (Exception e) {
            logger.error("Error generating coupon", e);
        }
        
        return null;
    }

    private String getApprovalLink(List<LinkDescription> linkDescriptions) {
        for (LinkDescription link : linkDescriptions) {
            if (link.rel().equalsIgnoreCase("approve")) {
                return link.href();
            }
        }
        throw new IllegalStateException("No approve URL found");
    }

    private PayPalHttpClient getPayPalHttpClient(String storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(null);

        // HOWTO register at paypal: https://developer.paypal.com/
        // Create REST Application
        // Copy Client ID and Secret

        PaypalInformation paypalInformation = store.getPaypalInformation();
        
        // wl sandbox
        String clientId = "";
        String clientSecret = "";
        boolean sandbox = true;
        
        if(paypalInformation != null) {
            clientId = paypalInformation.getClientId();
            clientSecret = paypalInformation.getClientSecret();
            sandbox = paypalInformation.isSandBoxAccount();
        }

        PayPalEnvironment environment;
        if(sandbox) {
            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        } else {
            environment = new PayPalEnvironment.Live(clientId, clientSecret);
        }
        
        return new PayPalHttpClient(environment);
    }
    
}
