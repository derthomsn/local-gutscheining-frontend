package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest.dto;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponTemplate;
import org.bson.internal.Base64;
import org.bson.types.Binary;

import java.math.BigDecimal;

public class CouponOptionDto {
    
    private String id;
    private BigDecimal value;
    private BigDecimal price;
    private BigDecimal taxes;
    private String name;
    private String description;
    private String image; // base 64

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static CouponOptionDto fromCouponTemplate(CouponTemplate couponTemplate) {
        CouponOptionDto dto = new CouponOptionDto();
        dto.setId(couponTemplate.getId());
        dto.setName(couponTemplate.getName());
        dto.setDescription(couponTemplate.getDescription());
        dto.setValue(couponTemplate.getValue());
        dto.setPrice(couponTemplate.getPrice());
        dto.setTaxes(couponTemplate.getTaxes());


        Binary couponImage = couponTemplate.getCouponImage();
        if(couponImage != null && couponImage.length() > 0) {
            dto.setImage(Base64Image.of(couponImage));
        }
        
        return dto;
    }
}
