package com.example.server.Event;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Table(name="Events")
@Entity
public class EventDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String eventName;
    private String idResto;

    private Date date;
    //private Timestamp endEvent;
    private int idUserOrganizer;
    private String nameOrganizer;

    private int nbLimitUsers;
    @ElementCollection
    private List <String> listParticipants;

    public EventDAO(String eventName, String idResto, Date date, int idUserOrganizer, String nameOrg, int nbLimitUsers) throws ParseException {
        this.eventName = eventName;
        this.idResto = idResto;
        this.date = date;
        this.nameOrganizer = nameOrg;
        this.idUserOrganizer = idUserOrganizer;
        this.nbLimitUsers = nbLimitUsers;
    }

    public EventDAO() {
        super();
    }

    public EventDAO(EventDTO eventDTO) {
        this.eventName = eventDTO.getEventName();
        this.idResto = eventDTO.getIdResto();
        this.date = eventDTO.getDate();
        this.nameOrganizer=eventDTO.getNameOrganizer();
        this.idUserOrganizer = eventDTO.getIdUserOrganizer();
        this.nbLimitUsers = eventDTO.getNbLimitUsers();
    }

    private Date adaptDate(String toConvert) throws ParseException {
        //String inputString = "11-11-2012";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(toConvert);
        return date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getIdResto() {
        return idResto;
    }

    public void setIdResto(String restoName) {
        this.idResto = restoName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNameOrganizer() {
        return nameOrganizer;
    }

    public void setNameOrganizer(String nameOrganizer) {
        this.nameOrganizer = nameOrganizer;
    }

    public int getIdUserOrganizer() {
        return idUserOrganizer;
    }

    public void setIdUserOrganizer(int idUserOrganizer) {
        this.idUserOrganizer = idUserOrganizer;
    }

    public int getNbLimitUsers() {
        return nbLimitUsers;
    }

    public void setNbLimitUsers(int nbLimitUsers) {
        this.nbLimitUsers = nbLimitUsers;
    }

    public List<String> getListParticipants() {
        return listParticipants;
    }

    public void setListParticipants(List<String> listParticipants) {
        this.listParticipants = listParticipants;
    }
}
