package ru.amplicode.rp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.amplicode.rp.model.Owner;

public interface OwnerRepository extends ReactiveMongoRepository<Owner, String> {

}