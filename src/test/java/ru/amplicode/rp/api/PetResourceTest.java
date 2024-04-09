package ru.amplicode.rp.api;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * Test class for the {@link PetResource}
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.docker.compose.skip.in-tests=false",
        "logging.level.org.springframework.data.mongodb.core.MongoTemplate=debug",
        "logging.level.org.springframework.data.mongodb.core.ReactiveMongoTemplate=debug"
})
public class PetResourceTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private InitMongoDbService mongoDbService;

    @BeforeEach
    public void setup() {
        mongoDbService.init();
    }

    @AfterEach
    void tearDown() {
        mongoDbService.drop();
    }

    @Test
    public void getListQueryAndSort() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets")
                        .queryParam("q", "Lu")
                        .queryParam("sort", "birthDate")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].typeId").isEqualTo(mongoDbService.getPetTypeIdByName("bird"))
                .jsonPath("$[0].birthDate").isEqualTo("1999-08-06")
                .jsonPath("$[1].typeId").isEqualTo(mongoDbService.getPetTypeIdByName("dog"))
                .jsonPath("$[1].birthDate").isEqualTo("2000-06-24");
    }

    @Test
    public void getListFindAndSortByName() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets")
                        .queryParam("q", "e")
                        .queryParam("sort", "name")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(4)
                .jsonPath("$[0].name").isEqualTo("Freddy")
                .jsonPath("$[1].name").isEqualTo("George")
                .jsonPath("$[2].name").isEqualTo("Jewel")
                .jsonPath("$[3].name").isEqualTo("Leo");
    }

    @Test
    public void getListFilterByQueryAll() {
        webTestClient.get().uri(uriBuilder ->
                                uriBuilder.path("/rest/pets")
                                        .queryParam("ownerId", mongoDbService.getOwnerIdByName("Eduardo"))
                                        .queryParam("q", "je")
//                                .queryParam("birthDateGreaterThan", "2000-01-01")
//                                .queryParam("birthDateLessThan", "2000-12-31")
                                        .build()
                ).exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$.[0].name").isEqualTo("Jewel");
    }

    @Test
    public void getListFilterByOwnerId() {
        String jeanId = mongoDbService.getOwnerIdByName("Jean");
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets")
                        .queryParam("ownerId", jeanId)
                        .queryParam("sort", "name").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].name").isEqualTo("Max")
                .jsonPath("$[0].ownerId").isEqualTo(jeanId)
                .jsonPath("$[1].name").isEqualTo("Samantha")
                .jsonPath("$[1].ownerId").isEqualTo(jeanId);
    }

    @Test
    public void getListSize5SortName() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets")
                        .queryParam("size", "5")
                        .queryParam("sort", "name").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0].name").isEqualTo("Basil")
                .jsonPath("$[1].name").isEqualTo("Freddy")
                .jsonPath("$[2].name").isEqualTo("George")
                .jsonPath("$[3].name").isEqualTo("Iggy")
                .jsonPath("$[4].name").isEqualTo("Jewel");
    }

    @Test
    public void getListSize5Page2SortName() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets")
                        .queryParam("size", "5")
                        .queryParam("page", "1")
                        .queryParam("sort", "name").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0].name").isEqualTo("Leo")
                .jsonPath("$[1].name").isEqualTo("Lucky")
                .jsonPath("$[2].name").isEqualTo("Lucky")
                .jsonPath("$[3].name").isEqualTo("Max")
                .jsonPath("$[4].name").isEqualTo("Mulligan");
    }

    @Test
    public void getOne() {
        String leoId = mongoDbService.getPetIdByName("Leo");
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets/" + leoId).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Leo")
                .jsonPath("$.ownerId").isEqualTo(mongoDbService.getOwnerIdByName("George"))
                .jsonPath("$.typeId").isEqualTo(mongoDbService.getPetTypeIdByName("cat"))
                .jsonPath("$.birthDate").isEqualTo("2000-09-07");
    }

    @Test
    public void getOneNotFound() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets/unknown-id").build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.title").isEqualTo("Not Found")
                .jsonPath("$.detail").isEqualTo("Entity with id `unknown-id` not found");
    }

    @Test
    public void getMany() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/rest/pets/by-ids")
                        .queryParam("ids",
                                mongoDbService.getPetIdByName("Leo"),
                                mongoDbService.getPetIdByName("Rosy"),
                                mongoDbService.getPetIdByName("Iggy"),
                                mongoDbService.getPetIdByName("Samantha")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$[0].name").isEqualTo("Leo")
                .jsonPath("$[1].name").isEqualTo("Rosy")
                .jsonPath("$[2].name").isEqualTo("Iggy")
                .jsonPath("$[3].name").isEqualTo("Samantha");
    }

    @Test
    public void createAndDelete() {
        String ownerId = mongoDbService.getOwnerIdByName("George");
        String typeId = mongoDbService.getPetTypeIdByName("cat");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/rest/pets").build())
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        	"name": "Buddy",
                        	"birthDate": "2020-04-01",
                        	"typeId": "%s",
                        	"ownerId": "%s"
                        }""".formatted(typeId, ownerId)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Buddy")
                .jsonPath("$.ownerId").isEqualTo(ownerId)
                .jsonPath("$.typeId").isEqualTo(typeId)
                .jsonPath("$.birthDate").isEqualTo("2020-04-01");
    }

    @Test
    public void createValidationException() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/rest/pets").build())
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        	"name": null,
                        	"birthDate": "2020-04-01",
                        	"typeId": null
                        }"""))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.detail").isEqualTo("Invalid request content.")
                .jsonPath("$.fieldErrors[2].field").isEqualTo("typeId")
                .jsonPath("$.fieldErrors[2].message").isEqualTo("must not be null")
                .jsonPath("$.fieldErrors[1].field").isEqualTo("ownerId")
                .jsonPath("$.fieldErrors[1].message").isEqualTo("must not be null")
                .jsonPath("$.fieldErrors[0].field").isEqualTo("name")
                .jsonPath("$.fieldErrors[0].message").isEqualTo("must not be empty");
    }

    @Test
    public void patchName() {
        String petId = mongoDbService.createAndSaveBuddyPet();

        webTestClient.patch().uri("/rest/pets/" + petId)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("""
                        {
                        	"name": "New Buddy Name"
                        }"""))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("New Buddy Name")
                .jsonPath("$.ownerId").isEqualTo(mongoDbService.getOwnerIdByName("George"))
                .jsonPath("$.typeId").isEqualTo(mongoDbService.getPetTypeIdByName("dog"))
                .jsonPath("$.birthDate").isEqualTo("2020-04-01");
    }

    @Test
    public void patchOwnerId() {
        String petId = mongoDbService.createAndSaveBuddyPet();
        String bettyId = mongoDbService.getOwnerIdByName("Betty");

        webTestClient.patch().uri("/rest/pets/" + petId)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("""
                        {
                        	"ownerId": "%s"
                        }""".formatted(bettyId)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Buddy")
                .jsonPath("$.ownerId").isEqualTo(bettyId)
                .jsonPath("$.typeId").isEqualTo(mongoDbService.getPetTypeIdByName("dog"))
                .jsonPath("$.birthDate").isEqualTo("2020-04-01");
    }

    @Test
    public void patchNullBirthDate() {
        String petId = mongoDbService.createAndSaveBuddyPet();

        webTestClient.patch().uri("/rest/pets/" + petId)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("""
                        {
                        	"birthDate": null
                        }"""))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Buddy")
                .jsonPath("$.ownerId").isEqualTo(mongoDbService.getOwnerIdByName("George"))
                .jsonPath("$.typeId").isEqualTo(mongoDbService.getPetTypeIdByName("dog"))
                .jsonPath("$.birthDate").value(IsNull.nullValue());
    }

    @Test
    public void patchAllProperties() {
        String petId = mongoDbService.createAndSaveBuddyPet();
        String ownerId = mongoDbService.getOwnerIdByName("Eduardo");
        String typeId = mongoDbService.getPetTypeIdByName("cat");

        webTestClient.patch().uri("/rest/pets/" + petId)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("""
                        {
                        	"name": "Buddy",
                        	"birthDate": "2020-04-01",
                        	"typeId": "%s",
                        	"ownerId": "%s"
                        }""".formatted(typeId, ownerId)))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Buddy")
                .jsonPath("$.ownerId").isEqualTo(ownerId)
                .jsonPath("$.typeId").isEqualTo(typeId)
                .jsonPath("$.birthDate").isEqualTo("2020-04-01");
    }

    @Test
    public void deleteTest() {
        String petId = mongoDbService.createAndSaveBuddyPet();

        webTestClient.delete().uri("/rest/pets/" + petId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.name").isEqualTo("Buddy")
                .jsonPath("$.ownerId").isEqualTo(mongoDbService.getOwnerIdByName("George"))
                .jsonPath("$.typeId").isEqualTo(mongoDbService.getPetTypeIdByName("dog"))
                .jsonPath("$.birthDate").isEqualTo("2020-04-01");
    }
}
