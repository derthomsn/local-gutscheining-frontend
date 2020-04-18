package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponNumberRange;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * We need to mix MongoTemplate and generated Crud repositories => Thats the way to do this
 */
public class CustomerCouponNumberRangeRepositoryImpl implements CustomerCouponNumberRangeRepository {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private MongoConverter mongoConverter;

    @Override
    public CouponNumberRange getUnusedAndMarkAsUsed(String storeId) {
        Query query = new Query(Criteria.where("used").is(false).and("store.$id").is(new ObjectId(storeId)));
        Update update = new Update().set("used", true);

        return mongoTemplate.findAndModify(query, update, CouponNumberRange.class);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void initIndicesAfterStartup() {
//        IndexOperations indexOps = mongoTemplate.indexOps(CouponNumberRange.class);
//
//        var mappingContext = this.mongoConverter.getMappingContext();
//
//        if (mappingContext instanceof MongoMappingContext) {
//            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
//            resolver.resolveIndexFor(CouponNumberRange.class).forEach(indexOps::ensureIndex);    
//        }
//    }
}
