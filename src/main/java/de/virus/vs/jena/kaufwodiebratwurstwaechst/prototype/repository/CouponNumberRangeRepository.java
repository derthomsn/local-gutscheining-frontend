package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponNumberRange;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CouponNumberRangeRepository extends MongoRepository<CouponNumberRange, String>, CustomerCouponNumberRangeRepository {

        Optional<CouponNumberRange> findFirstByStoreOrderByRunningNumberDesc(String storeId);
        
        @Query(value = "{'store.$id' : ?0, 'used' : false}", count = true)
        long countUnusedDocumentsForStoreId(String storeId);
        
}
