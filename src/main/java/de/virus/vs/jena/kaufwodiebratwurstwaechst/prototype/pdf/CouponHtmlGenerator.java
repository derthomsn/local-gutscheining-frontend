package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.pdf;

import com.google.common.io.Resources;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Address;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponTemplate;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.StoreRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto.Base64Image;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

@Service
public class CouponHtmlGenerator {

    @Autowired
    private StoreRepository storeRepository;
    
    private static String couponResource;
    private static Binary platformLogo;
    private static Binary backgroundImage;
    private static Binary wlLogo;

    static {
        try {
            couponResource = Resources.toString(Resources.getResource(CouponHtmlGenerator.class, "coupon.ftl"), StandardCharsets.UTF_8);
            platformLogo = new Binary(Resources.toByteArray(Resources.getResource(CouponHtmlGenerator.class, "localgutscheining_logo_colour.svg")));
            wlLogo = new Binary(Resources.toByteArray(Resources.getResource(CouponHtmlGenerator.class, "WL-Logo+powered-by_white.svg")));
            backgroundImage = new Binary(Resources.toByteArray(Resources.getResource(CouponHtmlGenerator.class, "Jena-CitySkyline.svg")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String generate(Coupon coupon) {
        FreemarkerTemplate freemarkerTemplate = new FreemarkerTemplate(couponResource);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("platformLogo", Base64Image.of(platformLogo, "image/svg+xml"));
        parameters.put("wlLogo", Base64Image.of(wlLogo, "image/svg+xml"));
        parameters.put("backgroundImage", Base64Image.of(backgroundImage, "image/svg+xml"));
        parameters.put("couponNumber", coupon.getCouponNumber());
        parameters.put("couponText", coupon.getOrder().getCouponText());
        parameters.put("couponValue", coupon.getOrder().getCouponValue());
        parameters.put("validUntil", coupon.getValidUntil().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).localizedBy(Locale.GERMANY)));
        
        Store store = coupon.getOrder().getStore();
        if(store != null) {
            parameters.put("storeName", store.getName());
            Address address = store.getAddress();
            if(address != null) {
                parameters.put("storeStreet", address.getStreet());
                parameters.put("storeHouseNumber", address.getHouseNumber());
                parameters.put("storeZipCode", address.getZipCode());
                parameters.put("storeCity", address.getCity());
            }
            
            parameters.put("storeLogo", Base64Image.of(store.getLogo()));
        }
        
        CouponTemplate couponTemplate = store.getCouponTemplateById(coupon.getOrder().getOrderedCouponTemplateId());
        if(couponTemplate != null) {
            parameters.put("couponName", couponTemplate.getName());
            parameters.put("couponDescription", couponTemplate.getDescription());
        }
        
        return freemarkerTemplate.render(parameters);
    }
    
}
