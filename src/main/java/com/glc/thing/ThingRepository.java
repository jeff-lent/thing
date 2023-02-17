package com.glc.thing;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThingRepository extends JpaRepository<Thing, Long>{  
    public Optional<Thing> findByName(String name);
}
