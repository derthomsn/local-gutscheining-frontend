package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;

@SpringBootApplication
public class KaufwodiebratwurstwächstApplication {
	public static void main(String[] args) {
		SpringApplication.run(KaufwodiebratwurstwächstApplication.class, args);
	}
	
	 
}
