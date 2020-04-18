package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponTemplateRepository extends MongoRepository<CouponTemplate, String> {

  CouponTemplate findByName(String name);

}