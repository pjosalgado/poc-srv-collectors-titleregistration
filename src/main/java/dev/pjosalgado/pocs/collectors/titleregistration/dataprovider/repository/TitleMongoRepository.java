package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.repository;

import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.entity.TitleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TitleMongoRepository extends MongoRepository<TitleEntity, String> {
}
