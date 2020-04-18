package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Document("coupon")
@CompoundIndexes({
        @CompoundIndex(name = "coupon_template_couponnumber", def = "{'template' : 1, 'couponNumber': 1}", unique = true)
})
public class Coupon {
    @Id
    private String id;

    @NotNull
    private String templateId;

    @DBRef
    @NotNull
    private Order order;

    @NotEmpty
    @NotNull
    private String couponNumber;

    private Binary generatedPdf;

    public Coupon() {
    }

    public String getId() {
        return id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public Binary getGeneratedPdf() {
        return generatedPdf;
    }

    public void setGeneratedPdf(Binary generatedPdf) {
        this.generatedPdf = generatedPdf;
    }
    
    public ZonedDateTime getValidUntil() {
        // TODO store in coupon?
        ZonedDateTime creationDate = ZonedDateTime.ofInstant(getOrder().getCreationDate().toInstant(), ZoneId.of("Europe/Berlin"));
        return creationDate.plusYears(2);
    }
}
