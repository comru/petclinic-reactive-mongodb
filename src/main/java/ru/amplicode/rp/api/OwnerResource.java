package ru.amplicode.rp.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.amplicode.rp.api.dto.OwnerDto;
import ru.amplicode.rp.api.mapper.OwnerMapper;
import ru.amplicode.rp.model.Owner;
import ru.amplicode.rp.repository.OwnerRepository;

@RestController
@RequestMapping("/rest/owners")
@RequiredArgsConstructor
public class OwnerResource {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @PostMapping
    public Mono<OwnerDto> save(@RequestBody @Valid Mono<OwnerDto> ownerDto) {
        return ownerDto.flatMap(dto -> {
            Owner owner = ownerMapper.toEntity(dto);
            return ownerRepository.save(owner).map(ownerMapper::toDto);
        });
    }
}

