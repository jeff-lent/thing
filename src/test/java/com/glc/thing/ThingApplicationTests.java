package com.glc.thing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThingApplicationTests {
	@Mock
	private ThingRepository thingRepository;
	
	@InjectMocks
	private ThingServiceImpl thingServiceImpl;

// As a user, I want to be able to manage my things in the system so I can keep track of how many of each of them I have.
// Thinking:  Need a pojo with ID (Long), Name(String), Description(String), and a Quantity(Long)
	@Test
	void contextLoads() {
	}
	@Test
	void getAndSetThingID(){
		Thing cut = new Thing();
		Long myId = 1L;
		cut.setId(myId);
		assertEquals(myId, cut.getId());
	}

	@Test
	void getAndSetThingName(){
		Thing cut = new Thing();
		String myName = "table";
		cut.setName(myName);
		assertEquals(myName, cut.getName());
	}

	@Test
	void getAndSetThingDescription(){
		Thing cut = new Thing();
		String myDescription = "table";
		cut.setDescription(myDescription);
		assertEquals(myDescription, cut.getDescription());
	}

	@Test
	void getAndSetThingQuantity(){
		Thing cut = new Thing();
		Long myQuantity = 10L;
		cut.setQuantity(myQuantity);
		assertEquals(myQuantity, cut.getQuantity());
	}

	@Test
	void allArgsConstructorThing(){
		Long myId = 1L;
		String myName = "table";
		String myDescription = "this is a table";
		Long myQuantity = 10L;
		Thing cut = new Thing(myId, myName, myDescription, myQuantity);
		assertEquals(myId, cut.getId());
		assertEquals(myName, cut.getName());
		assertEquals(myDescription, cut.getDescription());
		assertEquals(myQuantity, cut.getQuantity());
	}

	@Test
	void builderThing(){
		Long myId = 1L;
		String myName = "table";
		String myDescription = "this is a table";
		Long myQuantity = 10L;
		Thing cut = Thing.builder()
							.id(myId)
							.name(myName)
							.description(myDescription)
							.quantity(myQuantity)
							.build();
		assertEquals(myId, cut.getId());
		assertEquals(myName, cut.getName());
		assertEquals(myDescription, cut.getDescription());
		assertEquals(myQuantity, cut.getQuantity());
	}

	@Test
	void canSaveAThing(){
		Long myId = 1L;
		String myName = "table";
		String myDescription = "this is a table";
		Long myQuantity = 10L;
		Thing cut = Thing.builder()
							.id(myId)
							.name(myName)
							.description(myDescription)
							.quantity(myQuantity)
							.build();
		given(thingRepository.findByName(myName)).willReturn(Optional.empty());
		given(thingRepository.save(cut)).willReturn(cut);
		Thing savedThing = thingServiceImpl.saveThing(cut);
		assertNotNull(savedThing);
	}

	@Test
	void canGetAThing(){
		Long myId = 1L;
		String myName = "table";
		String myDescription = "this is a table";
		Long myQuantity = 10L;
		Thing cut = Thing.builder()
							.id(myId)
							.name(myName)
							.description(myDescription)
							.quantity(myQuantity)
							.build();
		given(thingRepository.getReferenceById(myId)).willReturn(cut);
		Thing gottenThing = thingServiceImpl.getThing(cut.getId());
		//assertNotNull(gottenThing);		
		assertEquals(cut.getName(), gottenThing.getName());
		assertEquals(cut.getDescription(), gottenThing.getDescription());
	}
	
	@Test
	void canUpdateAThing(){
		Long myId = 1L;
		String myName = "table";
		String myDescription = "this is a table";
		Long myQuantity = 10L;
		Thing cut = Thing.builder()
							.id(myId)
							.name(myName)
							.description(myDescription)
							.quantity(myQuantity)
							.build();
		given(thingRepository.findByName(myName)).willReturn(Optional.of(cut));
		given(thingRepository.save(cut)).willReturn(cut);
		Thing updatedThing = thingServiceImpl.updateThing(cut);
		assertEquals(cut.getName(), updatedThing.getName());
		assertEquals(cut.getDescription(), updatedThing.getDescription());
	}

	@Test
	void canDeleteAThing(){
		Long myId = 1L;
		willDoNothing().given(thingRepository).deleteById(myId);
		thingServiceImpl.deleteThing(myId);
		verify(thingRepository, times(1)).deleteById(myId);
	}

	@Test
	void canGetAllThings(){
		Thing cut1 = Thing.builder()
							.id(1L)
							.name("chair")
							.description("this is a chair")
							.quantity(4L)
							.build();
		Thing cut2 = Thing.builder()
							.id(2L)
							.name("table")
							.description("this is a table")
							.quantity(1L)
							.build();
		given(thingRepository.findAll()).willReturn(List.of(cut1, cut2));
		List<Thing> thingList = thingServiceImpl.getAllThings();
		assertNotNull(thingList);
		assertEquals(2, thingList.size());
	}

}
