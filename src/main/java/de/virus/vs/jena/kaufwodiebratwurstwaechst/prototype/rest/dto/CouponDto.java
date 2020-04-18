package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponTemplate;

import java.math.BigDecimal;

public class CouponDto {
    
    private String id;
    private String couponNumber;
    private String couponText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getCouponText() {
        return couponText;
    }

    public void setCouponText(String couponText) {
        this.couponText = couponText;
    }

    public static CouponDto fromCouponTemplate(Coupon coupon) {
        CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setCouponNumber(coupon.getCouponNumber());
        dto.setCouponText(coupon.getOrder().getCouponText());
        
        return dto;
    }
}
