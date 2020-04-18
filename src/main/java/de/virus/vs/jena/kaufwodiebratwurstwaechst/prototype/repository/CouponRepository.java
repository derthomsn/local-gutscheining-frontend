package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponRepository extends MongoRepository<Coupon, String> {

    Coupon findByCouponNumber(String couponNumber);

}