package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.mail;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;

import javax.mail.MessagingException;

public interface ISendEmailService {
    void sendCouponMailToOrdererAsync(Coupon coupon) throws MessagingException;
}
