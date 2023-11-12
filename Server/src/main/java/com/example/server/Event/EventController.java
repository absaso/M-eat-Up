package com.example.server.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/Events")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.POST,value="/add")
    private void addEvent(@RequestBody EventDTO eventDTO) {
        eventService.addEvent(eventDTO);
        String result = "Event created ";
        System.out.println(result);
    }

    @RequestMapping(method = RequestMethod.POST,value="/add/{idEvent}")
    private void addUserToEvent(@RequestBody SavingUser userToAdd, @PathVariable int idEvent) {
        eventService.addToEvent(userToAdd,idEvent);
        String result = "User added to the event";
        System.out.println(result);
    }

    @RequestMapping(method = RequestMethod.GET)
    private List<EventDAO> getRestaurants(){
        List<EventDAO> events = eventService.getEvents();
        System.out.println(events.toString());
        return events;
    }

    @RequestMapping(method = RequestMethod.GET,value="/{idresto}")
    public List<EventDTO> getEventsByRestaurant(@PathVariable String idresto)  {
        return eventService.getEventsByRestaurantId(idresto);
    }

}
