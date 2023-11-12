package com.cpe.meatup;

import java.util.List;

public class User {

    private int id;
    private String name;
    private String login;
    private int age;
    private String pwd;
    private List<String> preferences;
    private List<String> historique_id;
    private List<String> historique_name;
    private List<String> liked_restaurants;
    private List<String> disliked_restaurants;

    public User(String name, String login, int age, String password, List<String> preferences) {
        this.name = name;
        this.login = login;
        this.pwd = password;
        this.age = age;
        this.preferences = preferences;
    }

    public List<String> getLiked_restaurants() {
        return liked_restaurants;
    }

    public void setLiked_restaurants(List<String> liked_restaurants) {
        this.liked_restaurants = liked_restaurants;
    }

    public List<String> getDisliked_restaurants() {
        return disliked_restaurants;
    }

    public void setDisliked_restaurants(List<String> disliked_restaurants) {
        this.disliked_restaurants = disliked_restaurants;
    }

    public User() {
    }
    public User(String login, String pwd){
        this.login = login;
        this.pwd = pwd;
    }
    public User(String name, String login, String pwd, int age,List<String> preferences) {
        this.name = name;
        this.login = login;
        this.pwd = pwd;
        this.age = age;
        this.preferences = preferences;
    }

    public User(int id, String name, String login, String pwd, List<String> preferences) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.pwd = pwd;
        this.preferences = preferences;
    }

    public List<String> getHistorique_id() {
        return historique_id;
    }

    public void setHistorique_id(List<String> historique_id) {
        this.historique_id = historique_id;
    }

    public List<String> getHistorique_name() {
        return historique_name;
    }

    public void setHistorique_name(List<String> historique_name) {
        this.historique_name = historique_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
