package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponNumberRange;

public interface CustomerCouponNumberRangeRepository {
    
    CouponNumberRange getUnusedAndMarkAsUsed(String orderId);
    
}
