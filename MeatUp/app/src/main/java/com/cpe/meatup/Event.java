package com.cpe.meatup;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Event {

    private String eventName;
    private int id;
    private String idResto;
    private String date;
    private int idUserOrganizer;
    private String nameOrganizer;
    private int nbLimitUsers;
    private List<Integer> listParticipants;

    public Event() {
    }

    public Event(String eventName, String idResto, String date, int idUserOrganizer, String nameOrganizer, int nbLimitUsers) {
        this.eventName = eventName;
        this.idResto = idResto;
        this.date = date;
        this.idUserOrganizer = idUserOrganizer;
        this.nameOrganizer = nameOrganizer;
        this.nbLimitUsers = nbLimitUsers;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdResto() {
        return idResto;
    }

    public void setIdResto(String idResto) {
        this.idResto = idResto;
    }

    public String getStartEvent() {
        return date;
    }

    public void setStartEvent(String date) {
        this.date = date;
    }

    public int getIdUserOrganizer() {
        return idUserOrganizer;
    }

    public void setIdUserOrganizer(int idUserOrganizer) {
        this.idUserOrganizer = idUserOrganizer;
    }

    public String getNameOrganizer() {
        return nameOrganizer;
    }

    public void setNameOrganizer(String nameOrganizer) {
        this.nameOrganizer = nameOrganizer;
    }

    public int getNbLimitUsers() {
        return nbLimitUsers;
    }

    public void setNbLimitUsers(int nbLimitUsers) {
        this.nbLimitUsers = nbLimitUsers;
    }

    public List<Integer> getListParticipants() {
        return listParticipants;
    }

    public void setListParticipants(List<Integer> listParticipants) {
        this.listParticipants = listParticipants;
    }
}
