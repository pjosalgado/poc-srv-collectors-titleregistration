package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.repository;

import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.TitleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TitleMongoRepository extends MongoRepository<TitleEntity, String> {
}
