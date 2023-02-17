package com.glc.thing;

import java.util.List;

public interface ThingService {
    Thing saveThing(Thing thing);
    Thing getThing(Long id);
    Thing updateThing(Thing thing);
    void deleteThing(Long id);
    List<Thing> getAllThings();
}
