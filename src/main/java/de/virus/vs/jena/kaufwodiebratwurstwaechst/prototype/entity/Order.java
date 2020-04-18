package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Document("order")
public class Order {
    @Id
    private String id;

    @NotNull
    private OrderStatus status;
    
    @NotNull
    private Date creationDate;

    private Date paymentDate;
    private Date sentDate;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    private String ordererFirstName;
    
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    private String ordererLastName;

    @NotNull
    @NotEmpty
    @Email(message = "Email should be valid")
    private String ordererEmail;

    @Size(max = 150)
    private String couponText;

    @NotNull
    private BigDecimal couponPrice;

    @NotNull
    private BigDecimal couponTax;

    @NotNull
    private BigDecimal couponValue;

    @NotNull
    private String orderedCouponTemplateId;
    
    @DBRef
    private Coupon coupon;

    @DBRef
    private Store store;

    @Indexed(name = "idx-order-paypalTransactionId", unique = true, sparse = true)
    private String paypalTransactionId;
    
    private String paypalCaptureId;

    public Order() {
        setStatus(OrderStatus.OPEN);
        creationDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getOrdererFirstName() {
        return ordererFirstName;
    }

    public void setOrdererFirstName(String ordererFirstName) {
        this.ordererFirstName = ordererFirstName;
    }

    public String getOrdererLastName() {
        return ordererLastName;
    }

    public void setOrdererLastName(String ordererLastName) {
        this.ordererLastName = ordererLastName;
    }

    public String getOrdererEmail() {
        return ordererEmail;
    }

    public void setOrdererEmail(String ordererEmail) {
        this.ordererEmail = ordererEmail;
    }

    public String getCouponText() {
        return couponText;
    }

    public void setCouponText(String couponText) {
        this.couponText = couponText;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getCouponTax() {
        return couponTax;
    }

    public void setCouponTax(BigDecimal couponTax) {
        this.couponTax = couponTax;
    }

    public BigDecimal getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(BigDecimal couponValue) {
        this.couponValue = couponValue;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }

    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
    }

    public String getOrderedCouponTemplateId() {
        return orderedCouponTemplateId;
    }

    public void setOrderedCoupon(CouponTemplate orderedCoupon) {
        this.orderedCouponTemplateId = orderedCoupon.getId();
        
        this.setCouponPrice(orderedCoupon.getPrice());
        this.setCouponValue(orderedCoupon.getValue());
        this.setCouponTax(orderedCoupon.getTaxes());
    }


    public Date getPaymentDate() {
        return paymentDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getPaypalCaptureId() {
        return paypalCaptureId;
    }

    public void setPaypalCaptureId(String paypalCaptureId) {
        this.paypalCaptureId = paypalCaptureId;
    }

    public void markPaid() {
        setStatus(OrderStatus.PAID);
        paymentDate = new Date();
    }

    public void markSent() {
        setStatus(OrderStatus.SENT);
        sentDate = new Date();
    }
}
