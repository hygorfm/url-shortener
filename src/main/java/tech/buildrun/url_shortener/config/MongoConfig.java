package tech.buildrun.url_shortener.config;

import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public CommandLineRunner initializeIndexes(MongoTemplate mongoTemplate) {
        return _ -> {
            // Create TTL index on expiresAt field
            // When expiresAt time is reached, MongoDB automatically deletes the document
            var collection = mongoTemplate.getCollection("urls");

            // Create TTL index with 0 seconds expiration
            // This means documents expire at the time specified in expiresAt field
            collection.createIndex(
                    new Document("expiresAt", 1),
                    new IndexOptions().expireAfter(0L, java.util.concurrent.TimeUnit.SECONDS)
            );
        };
    }

}




