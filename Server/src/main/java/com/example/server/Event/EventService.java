package com.example.server.Event;

import com.example.server.user.User;
import com.example.server.user.UserRepository;
import com.example.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    List<EventDAO> listEvents;

    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private UserService userService;

    public void addEvent(EventDTO eventDTO) {
        EventDAO event = new EventDAO(eventDTO);
        eventRepo.save(event);
    }

    public List<EventDAO> getEvents(){
        listEvents = eventRepo.findAll();
        return listEvents;
    }

    public EventDAO getEventById (int id){

        return eventRepo.findById(id).get();
    }


    public List<EventDTO> getEventsByRestaurantId(String idresto)  {
        List<EventDAO> eventDAO = eventRepo.findByIdResto(idresto);
        List<EventDTO> listEventDTO = new ArrayList<>();
        for(EventDAO event:eventDAO){
            EventDTO dto = new EventDTO(event);
            //int iduser = dto.getIdUserOrganizer();
            //dto.setNameOrganizer(getNameOrganizer(iduser));
            listEventDTO.add(dto);
        }
        return listEventDTO;
    }

    public void addToEvent(SavingUser userToAdd, int idEvent) {
        List<String> listParticipants = new ArrayList<>();
        EventDAO event = getEventById(idEvent);
        Optional<User> user = userService.getUserById(userToAdd.getIdUser());
        if (event.getListParticipants() != null){
            listParticipants = event.getListParticipants();
        }
        listParticipants.add(user.get().getName());
        event.setListParticipants(listParticipants);

        userService.AddHistory(user.get(),userToAdd.getNameResto());
        userService.AddHistoryId(user.get(),userToAdd.getIdResto());
        eventRepo.save(event);
        System.out.println("Service: user bien updated");
    }
}
