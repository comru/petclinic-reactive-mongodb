package ru.amplicode.rp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import ru.amplicode.rp.model.Pet;

public interface PetRepositoryEx {
    Flux<Pet> findAll(Criteria criteria, Pageable pageable);
}
