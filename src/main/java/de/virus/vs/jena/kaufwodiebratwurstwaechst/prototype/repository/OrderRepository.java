package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

  Order findByPaypalTransactionId(String paypalTransactionId);

}