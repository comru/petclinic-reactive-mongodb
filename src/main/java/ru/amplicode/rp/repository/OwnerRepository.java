package ru.amplicode.rp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.amplicode.rp.model.Owner;

import java.util.List;

public interface OwnerRepository extends ReactiveMongoRepository<Owner, String> {

    Flux<Owner> findByLastName(String lastName);
}