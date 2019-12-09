package com.szydd.software.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource(value = "classpath:mongodb.properties")
class MongoConfiguration {

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private Integer port;

    @Value("${mongo.dbname}")
    private String dbname;

    @Value("${mongo.username}")
    private String username;

    @Value("${mongo.password}")
    private String password;

    @Value("${mongo.connectionsPerHost}")
    private int connectionsPerHost;

    @Value("${mongo.threadsAllowedToBlockForConnectionMultiplier}")
    private int threadsAllowedToBlockForConnectionMultiplier;

    @Value("${mongo.connectTimeout}")
    private int connectTimeout;

    @Value("${mongo.maxWaitTime}")
    private int maxWaitTime;

    @Value("${mongo.socketKeepAlive}")
    private boolean socketKeepAlive;

    @Value("${mongo.socketTimeout}")
    private int socketTimeout;

    @Bean
    public MongoClient mongoClient() throws Exception {
        ServerAddress address = new ServerAddress(host, port);
        MongoClientOptions options = MongoClientOptions.builder().build();
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        credentialsList.add(MongoCredential.createScramSha1Credential(username,
                dbname, password.toCharArray()));
        return new MongoClient(address, credentialsList, options);
    }

    @Bean(name = "mgFactory")
    public MongoDbFactory mongoDbFactory(MongoClient client) {
        return new SimpleMongoDbFactory(client, dbname);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory factory) {
        return new MongoTemplate(factory);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
