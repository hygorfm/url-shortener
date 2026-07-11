package tech.buildrun.url_shortener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.buildrun.url_shortener.entity.UrlEntity;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {

}
