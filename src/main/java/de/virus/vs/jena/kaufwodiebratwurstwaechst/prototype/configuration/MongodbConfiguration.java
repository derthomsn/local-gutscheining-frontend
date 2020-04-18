package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.configuration;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Coupon;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponNumberRange;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import javax.annotation.PostConstruct;

@Configuration
@DependsOn("mongoTemplate")
public class MongodbConfiguration {
  
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoConverter mongoConverter;

    @PostConstruct
    public void initIndexes() {
        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = this.mongoConverter.getMappingContext();

        if (mappingContext instanceof MongoMappingContext) {
            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
            resolver.resolveIndexFor(CouponNumberRange.class).forEach(mongoTemplate.indexOps(CouponNumberRange.class)::ensureIndex);
            resolver.resolveIndexFor(Store.class).forEach(mongoTemplate.indexOps(Store.class)::ensureIndex);
            resolver.resolveIndexFor(Coupon.class).forEach(mongoTemplate.indexOps(Coupon.class)::ensureIndex);
        }
    }
    
}
