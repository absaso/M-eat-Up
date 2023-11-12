package com.example.server.Event;

public class SavingUser {
    int idUser;
    String idResto;
    String nameResto;

    public SavingUser(int idUser, String idResto, String nameResto) {
        this.idUser = idUser;
        this.idResto = idResto;
        this.nameResto = nameResto;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdResto() {
        return idResto;
    }

    public void setIdResto(String idResto) {
        this.idResto = idResto;
    }

    public String getNameResto() {
        return nameResto;
    }

    public void setNameResto(String nameResto) {
        this.nameResto = nameResto;
    }
}
