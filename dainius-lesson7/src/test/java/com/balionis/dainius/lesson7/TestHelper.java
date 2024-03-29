package com.balionis.dainius.lesson7;

import com.balionis.dainius.lesson7.entity.PetEntity;
import com.balionis.dainius.lesson7.entity.PetStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestHelper {
    public static PetEntity createMockPetEntity(UUID petId) {
        var now = LocalDateTime.now(Clock.systemUTC());
        var petEntity = new PetEntity();
        petEntity.setPetId(petId.toString());
        petEntity.setName("myPet");
        petEntity.setStatus(PetStatus.AVAILABLE);
        petEntity.setCreatedAt(now);
        petEntity.setUpdatedAt(now);
        return petEntity;
    }

}