package ru.amplicode.rp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import ru.amplicode.rp.model.Pet;

@RequiredArgsConstructor
public class PetRepositoryExImpl implements PetRepositoryEx {

    private final ReactiveMongoTemplate template;

    @Override
    public Flux<Pet> findAll(Criteria criteria, Pageable pageable) {
        return template.find(Query.query(criteria).with(pageable), Pet.class);
    }
}
