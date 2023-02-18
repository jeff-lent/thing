package com.glc.thing;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class ThingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThingServiceImpl thingService;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<Thing> jsonThing;

    @Test
    public void postANewThing() throws Exception{
        Thing thing = Thing.builder()
                            .name("table")
                            .description("it's a table")
                            .quantity(1L)
                            .build();
        given(thingService.saveThing(any(Thing.class))).willAnswer((invocation) -> invocation.getArgument(0));
        ResultActions response = mockMvc.perform(post("/api/things")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(thing)));
        response.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name",is(thing.getName())))
            .andExpect(jsonPath("$.description",is(thing.getDescription())))
           .andExpect(jsonPath("$.quantity", is(thing.getQuantity().intValue()))); 
          // TODO figure how to make this work with LONGS!
    }

    @Test
    public void getAllTheThings() throws Exception{
        Thing thing1 = Thing.builder()
            .name("table")
            .description("it's a table")
            .quantity(1L)
            .build(); 
        Thing thing2 = Thing.builder()
            .name("chair")
            .description("it's a chair")
            .quantity(4L)
            .build();
        List<Thing> things = List.of(thing1,thing2);
        given(thingService.getAllThings()).willReturn(things);

        ResultActions response = mockMvc.perform(get("/api/things"));
        
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(things.size())));
    }

    @Test
    public void getOneThingById() throws Exception{
       // Long thingId = 1L;
        Thing thing = Thing.builder()
            .id(1L)
            .name("table")
            .description("it's a table")
            .quantity(1L)
            .build(); 
        given(thingService.getThing(1L)).willReturn(thing);

        ResultActions response = mockMvc.perform(get("/api/things/{id}", thing.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(thing.getName())))
                .andExpect(jsonPath("$.description", is(thing.getDescription())));
    }

    @Test
    public void cantGetOneThingByIdIfTheThingDoesntExist() throws Exception{
        Long thingId = 1L;
        given(thingService.getThing(thingId)).willReturn(new Thing());

        ResultActions response = mockMvc.perform(get("/api/things/{id}", thingId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void canUpdateAThing() throws Exception{
        Long thingId = 1L;
        Thing savedThing = Thing.builder()
                                .name("table")
                                .description("its a table")
                                .quantity(1L)
                                .build();
        Thing updatedThing = Thing.builder()
                                .name("chair")
                                .description("its a chair")
                                .quantity(4L)
                                .build();
        given(thingService.getThing(thingId)).willReturn(savedThing);
        given(thingService.updateThing(any(Thing.class))).willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/things/{id}", thingId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedThing)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name",is(updatedThing.getName())))
                .andExpect(jsonPath("$.description", is(updatedThing.getDescription())));
    }

    @Test
    public void cantUpdateAThingWhenTheThingDoesntExist() throws Exception{
        Long thingId = 1L;
        Thing updatedThing = Thing.builder()
                                .name("chair")
                                .description("its a chair")
                                .quantity(4L)
                                .build();
        given(thingService.getThing(thingId)).willReturn(new Thing());
        given(thingService.updateThing(any(Thing.class))).willThrow(new InvalidConfigurationPropertyValueException("Name", updatedThing.getName(), "Thing not found!"));

        ResultActions response = mockMvc.perform(put("/api/things/{id}", thingId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedThing)));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void canDeleteAThing() throws Exception{
        Long thingId = 1L;
        willDoNothing().given(thingService).deleteThing(thingId);

        ResultActions response = mockMvc.perform(delete("/api/things/{id}", thingId));

        response.andExpect(status().isOk()).andDo(print());
        verify(thingService,times(1)).deleteThing(thingId);
    }
    
}
