package ru.amplicode.rp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.amplicode.rp.model.Pet;

public interface PetRepository extends ReactiveMongoRepository<Pet, String>, PetRepositoryEx {



}

