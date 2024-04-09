package ru.amplicode.rp.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.amplicode.rautils.patch.ObjectPatcher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.amplicode.rp.api.dto.PetDto;
import ru.amplicode.rp.api.filter.PetFilter;
import ru.amplicode.rp.api.mapper.PetMapper;
import ru.amplicode.rp.model.Pet;
import ru.amplicode.rp.repository.PetRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/pets")
public class PetResource {

    private final PetMapper petMapper;
    private final PetRepository petRepository;

    private final ObjectPatcher objectPatcher;

    @GetMapping
    public Flux<PetDto> getList(@ModelAttribute PetFilter filter, Pageable pageable) {
        var criteria = filter.toCriteria();
        return petRepository.findAll(criteria, pageable).map(petMapper::toDto);
    }


    @GetMapping("/{id}")
    public Mono<PetDto> getOne(@PathVariable("id") String id) {
        return petRepository.findById(id)
                .map(petMapper::toDto)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @GetMapping("/by-ids")
    public Flux<PetDto> getMany(@RequestParam List<String> ids) {
        return petRepository.findAllById(ids).map(petMapper::toDto);
    }

    @PostMapping
    public Mono<PetDto> create(@RequestBody @Valid Mono<PetDto> petDto) {
        //todo check valid method
        return petDto.flatMap(dto -> {
            Pet pet = petMapper.toEntity(dto);
            return petRepository.save(pet).map(petMapper::toDto);
        }).onErrorResume(Mono::error);
    }

    @PatchMapping("/{id}")
    public Mono<PetDto> patch(@PathVariable String id,
                              @RequestBody JsonNode patchNode) {
        return petRepository.findById(id).flatMap(pet -> {
                    PetDto dto = petMapper.toDto(pet);
                    dto = objectPatcher.patchAndValidate(dto, patchNode);
                    petMapper.update(dto, pet);

                    return petRepository.save(pet).map(petMapper::toDto);
                })
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    @DeleteMapping("/{id}")
    public Mono<PetDto> delete(@PathVariable String id) {
        return petRepository.findById(id).map(pet -> {
            petRepository.delete(pet);
            return petMapper.toDto(pet);
        });
    }
}

