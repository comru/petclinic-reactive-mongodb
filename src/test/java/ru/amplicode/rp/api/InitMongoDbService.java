package ru.amplicode.rp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.amplicode.rp.model.Owner;
import ru.amplicode.rp.model.Pet;
import ru.amplicode.rp.model.PetType;
import ru.amplicode.rp.repository.OwnerRepository;
import ru.amplicode.rp.repository.PetRepository;
import ru.amplicode.rp.repository.PetTypeRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class InitMongoDbService {

    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PetRepository petRepository;

    private final Map<String, String> petNameToId = new HashMap<>();
    private final Map<String, String> petTypeNameToId = new HashMap<>();
    private final Map<String, String> ownerNameToId = new HashMap<>();

    public void init() {
        petNameToId.clear();
        petTypeNameToId.clear();
        ownerNameToId.clear();

        PetType catType = createPetType("cat");
        PetType dogType = createPetType("dog");
        PetType lizardType = createPetType("lizard");
        PetType snakeType = createPetType("snake");
        PetType birdType = createPetType("bird");
        PetType hamsterType = createPetType("hamster");

        Owner george = createOwner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
        Owner betty = createOwner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");
        Owner eduardo = createOwner("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763");
        Owner harold = createOwner("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198");
        Owner peter = createOwner("Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765");
        Owner jean = createOwner("Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654");
        Owner jeff = createOwner("Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387");
        Owner maria = createOwner("Maria", "Escobito", "345 Maple St.", "Madison", "6085557683");
        Owner david = createOwner("David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435");
        Owner carlos = createOwner("Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487");

        Pet leoPet = createPet("Leo", LocalDate.of(2000,9, 7), catType, george);
        Pet basilPet = createPet("Basil", LocalDate.of(2002,8, 6), hamsterType, betty);
        Pet rosyPet = createPet("Rosy", LocalDate.of(2001,4, 17), dogType, eduardo);
        Pet jewelPet = createPet("Jewel", LocalDate.of(2000,3, 7), dogType, eduardo);
        Pet iggyPet = createPet("Iggy", LocalDate.of(2000,11, 30), lizardType, harold);
        Pet georgePet = createPet("George", LocalDate.of(2000,1, 20), snakeType, peter);
        Pet samantaPet = createPet("Samantha", LocalDate.of(1995,9, 4), catType, jean);
        Pet maxPet = createPet("Max", LocalDate.of(1995,9, 4), catType, jean);
        Pet luckyPet = createPet("Lucky", LocalDate.of(1999,8, 6), birdType, jeff);
        Pet mulliganPet = createPet("Mulligan", LocalDate.of(1997,2, 24), dogType, maria);
        Pet freddyPet = createPet("Freddy", LocalDate.of(2000,3, 9), birdType, david);
        Pet lucky2Pet = createPet("Lucky", LocalDate.of(2000,6, 24), dogType, carlos);
        Pet slyPet = createPet("Sly", LocalDate.of(2002,6, 8), catType, carlos);
    }

    public void drop() {
        petTypeRepository.deleteAll().block();
        ownerRepository.deleteAll().block();
        petRepository.deleteAll().block();
    }

    private Pet createPet(String name, LocalDate birthDate, PetType type, Owner owner) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setBirthDate(birthDate);
        pet.setTypeId(type.getId());
        pet.setOwnerId(owner.getId());
        Pet savedPet = petRepository.insert(pet).block();
        petNameToId.put(name, savedPet.getId());
        return savedPet;
    }

    private Owner createOwner(String firstName,
                              String lastName,
                              String address,
                              String city,
                              String telephone) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setAddress(address);
        owner.setCity(city);
        owner.setTelephone(telephone);
        Owner savedOwner = ownerRepository.insert(owner).block();
        ownerNameToId.put(firstName, savedOwner.getId());
        return savedOwner;
    }

    private PetType createPetType(String name) {
        PetType petType = new PetType();
        petType.setName(name);
        PetType savedPetType = petTypeRepository.insert(petType).block();

        petTypeNameToId.put(name, savedPetType.getId());
        return savedPetType;
    }

    public String getPetTypeIdByName(String petTypeName) {
        return petTypeNameToId.get(petTypeName);
    }

    public String getPetIdByName(String petName) {
        return petNameToId.get(petName);
    }

    public String getOwnerIdByName(String ownerName) {
        return ownerNameToId.get(ownerName);
    }

    public String createAndSaveBuddyPet() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setBirthDate(LocalDate.of(2020, 4, 1));
        pet.setTypeId(getPetTypeIdByName("dog"));
        pet.setOwnerId(getOwnerIdByName("George"));
        Pet savedPet = petRepository.save(pet).block();
        return savedPet.getId();
    }
}
