package com.example.appspringdata;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppSpringDataApplication {

	private Driver driver;

	public static void main(String[] args) {
		SpringApplication.run(AppSpringDataApplication.class, args);
	}

	@Bean
	public Configuration configuration() {
		return Configuration.newConfig().withDialect(Dialect.NEO4J_5).build();
	}

	@Bean 
    public Driver neo4jDriver(
		@Value("${spring.neo4j.uri:bolt://localhost:7687}") String uri,
        @Value("${spring.neo4j.authentication.username:neo4j}") String user,
        @Value("${spring.neo4j.authentication.password:secret}") String pass
	){
		this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass));
        this.driver.verifyConnectivity(); 
        return this.driver;
    }
   
}