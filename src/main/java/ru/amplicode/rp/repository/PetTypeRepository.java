package ru.amplicode.rp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.amplicode.rp.model.PetType;

public interface PetTypeRepository extends ReactiveMongoRepository<PetType, String> {
}