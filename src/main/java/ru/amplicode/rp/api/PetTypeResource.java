package ru.amplicode.rp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.amplicode.rp.api.mapper.PetTypeMapper;
import ru.amplicode.rp.model.PetType;
import ru.amplicode.rp.repository.PetTypeRepository;

@RestController
@RequestMapping("/rest/types")
@RequiredArgsConstructor
public class PetTypeResource {

    private final PetTypeRepository petTypeRepository;

    private final PetTypeMapper petTypeMapper;

    @PostMapping
    public Mono<PetTypeDto> save(@RequestBody Mono<PetTypeDto> petTypeDto) {
        return petTypeDto.flatMap(dto -> {
            PetType owner = petTypeMapper.toEntity(dto);
            return petTypeRepository.save(owner).map(petTypeMapper::toDto);
        });
    }
}

