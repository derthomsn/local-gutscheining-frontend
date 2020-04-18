package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepository extends MongoRepository<Store, String> {

  Store findByName(String name);

}