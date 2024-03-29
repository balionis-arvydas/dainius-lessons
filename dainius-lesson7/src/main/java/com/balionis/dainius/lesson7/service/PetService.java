package com.balionis.dainius.lesson7.service;

import com.balionis.dainius.lesson7.entity.PetEntity;
import com.balionis.dainius.lesson7.entity.PetStatus;
import com.balionis.dainius.lesson7.generated.model.Pet;
import com.balionis.dainius.lesson7.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class PetService {

    private final String name;

    private final PetRepository petRepository;

    public UUID addPet(Pet pet) {
        var petId = UUID.randomUUID();
        var now = LocalDateTime.now(Clock.systemUTC());

        log.info("name={}, pet={}, petId={}", name, pet, petId);

        PetEntity petEntity = new PetEntity();
        petEntity.setPetId(petId.toString());
        petEntity.setName(pet.getName());
        petEntity.setStatus(PetStatus.valueOf(pet.getStatus().getValue()));

        petEntity.setCreatedAt(now);
        petEntity.setUpdatedAt(now);

        petRepository.save(petEntity);

        return petId;
    }

    public void deletePet(UUID petId) {
        log.info("name={}, petId={}", name, petId);
        petRepository.deleteById(petId.toString());
    }

    public Pet getPet(UUID petId) {
        log.info("name={}, petId={}", name, petId);
        var petIdStr = petId.toString();
        var pet = petRepository.findById(petIdStr);
        return pet.map(entity ->
                        new Pet().id(UUID.fromString(entity.getPetId()))
                                .name(entity.getName())
                                .status(Pet.StatusEnum.valueOf(entity.getStatus().name())))
                .orElseThrow(() -> new IllegalArgumentException(petId.toString()));
    }
}
