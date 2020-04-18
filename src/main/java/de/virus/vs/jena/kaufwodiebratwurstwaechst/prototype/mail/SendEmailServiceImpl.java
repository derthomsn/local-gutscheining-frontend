package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.mail;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SendEmailServiceImpl implements ISendEmailService {

    static private Logger logger = LoggerFactory.getLogger(SendEmailServiceImpl.class);
    
    @Value("${coupon.mail.subject}")
    private String subject;

    @Value("${mail.service.activated}")
    private boolean mailServiceActivated;
    
    @Autowired
    public JavaMailSender emailSender;

    @Async
    @Override
    public void sendCouponMailToOrdererAsync(Coupon coupon) throws MessagingException {
        
        logger.info("mail service acitvated: " + mailServiceActivated);
        
        if(mailServiceActivated) {
            logger.info("Asynchronously sending coupon mail");

            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Order order = coupon.getOrder();

            helper.setTo(order.getOrdererEmail());
            helper.setSubject(subject);
            helper.setText("Well - thanks i guess");
            helper.addAttachment("Coupon.pdf", new ByteArrayResource(coupon.getGeneratedPdf().getData()));

            emailSender.send(message);
        } else {
            logger.info("Mail service deactivated - skipping coupon mail sending");
        }
    }
}