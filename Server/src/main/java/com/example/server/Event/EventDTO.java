package com.example.server.Event;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDTO {

    private int id;
    private String eventName;
    private String idResto;
    private Date date;
    private int idUserOrganizer;
    private String nameOrganizer;
    private int nbLimitUsers;
    private List<String> listParticipants;

    public EventDTO() {
    }

    public EventDTO(String eventName, String idResto, String date, int idUserOrganizer, String nameOrg,int nbLimitUsers) throws ParseException {
        this.eventName = eventName;
        this.idResto = idResto;
        this.date = adaptDate(date);
        this.nameOrganizer=nameOrg;
        this.idUserOrganizer = idUserOrganizer;
        this.nbLimitUsers = nbLimitUsers;
    }

    public EventDTO(EventDAO eventDAO) {
        this.eventName = eventDAO.getEventName();
        this.idResto = eventDAO.getIdResto();
        this.date = eventDAO.getDate();
        this.nameOrganizer=eventDAO.getNameOrganizer();
        this.idUserOrganizer = eventDAO.getIdUserOrganizer();
        this.nbLimitUsers = eventDAO.getNbLimitUsers();
    }

    private Date adaptDate(String toConvert) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = dateFormat.parse(toConvert);
        System.out.println("date converted in server "+date.toString());
        return date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /*public Timestamp getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(Timestamp endEvent) {
        this.endEvent = endEvent;
    }*/

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

    public List<String> getListParticipants() {
        return listParticipants;
    }

    public void setListParticipants(List<String> listParticipants) {
        this.listParticipants = listParticipants;
    }
}
