package com.glc.thing;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;

@Service
public class ThingServiceImpl implements ThingService {
    private ThingRepository thingRepository;

    public ThingServiceImpl(ThingRepository thingRepository){
        this.thingRepository = thingRepository;
    }

    public Thing saveThing(Thing thing) {
        Optional<Thing> savedThing = thingRepository.findByName(thing.getName());
        if(savedThing.isPresent()){
            throw new InvalidConfigurationPropertyValueException("Name", thing.getName(), "A thing named "+thing.getName()+" already exists in the database!");
        }
        return thingRepository.save(thing);
    }

    public Thing getThing(Long id) {
        return thingRepository.getReferenceById(id);
    }

    public Thing updateThing(Thing thing) {
        //TODO this needs to be cleaned up
        Optional<Thing> savedThing = thingRepository.findByName(thing.getName()); 
        if(savedThing.isEmpty()){
            throw new InvalidConfigurationPropertyValueException("Name", thing.getName(), "A thing named "+thing.getName()+" does not already exist in the database.");
        }
        return thingRepository.save(thing);
    }

    public void deleteThing(Long id){
        thingRepository.deleteById(id);
    }

    public List<Thing> getAllThings(){
        return thingRepository.findAll();
    }
}
