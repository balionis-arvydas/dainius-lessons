package com.balionis.dainius.lesson6.repository;

import com.balionis.dainius.lesson6.entity.PetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetRepository extends CrudRepository<PetEntity, UUID> {
}
