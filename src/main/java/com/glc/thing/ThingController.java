package com.glc.thing;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.joran.conditional.ElseAction;

@RestController
@RequestMapping("/api/things")
public class ThingController {

    private ThingService thingService;

    public ThingController(ThingService thingService) {
        this.thingService = thingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Thing createThing(@RequestBody Thing thing){
        return thingService.saveThing(thing);
    }

    @GetMapping
    public List<Thing> getAllThings(){
        return thingService.getAllThings();
    }

    @GetMapping("{id}")
    public ResponseEntity<Thing> getThingById(@PathVariable("id") Long thingId){
        Thing thing = thingService.getThing(thingId);
        if(thing.getId() != null)
            return ResponseEntity.status(HttpStatus.OK).body(thing);
        else 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Thing());
    }

    @PutMapping("{id}")
    public ResponseEntity<Thing> updateThing(@PathVariable("id") Long thingId, @RequestBody Thing thing){
        thing.setId(thingId);
        try{
            thingService.updateThing(thing);
            return ResponseEntity.status(HttpStatus.OK).body(thing);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Thing());
        }
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteThing(@PathVariable("id") Long thingId){
        thingService.deleteThing(thingId);
        return new ResponseEntity<String>("Thing deleted successfully!", HttpStatus.OK);
    }
}
