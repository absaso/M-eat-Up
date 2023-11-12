package com.example.server.user;

import java.util.List;

public class UserDTO {
	private int id;
    private String name;
    private String login;
    private String pwd;
    private List<String> preferences;
	private int age;

	private List<String> historique_id;
	private List<String> historique_name;
	private List<String> liked_restaurants;
	private List<String> disliked_restaurants;
	private List<String> restaurants_suggere;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setRestaurants_suggere(List<String> restaurants_suggere) {
		this.restaurants_suggere = restaurants_suggere;
	}

	public List<String> getRestaurants_suggere() {
		return restaurants_suggere;
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
	public UserDTO(int id, String name, String login, int age, String pwd, List<String> preferences) {
		super();
		this.id = id;
		this.name = name;
		this.login = login;
		this.pwd = pwd;
		this.age = age;
		this.preferences = preferences;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	public UserDTO() {
		super();
	}

	@Override
	public String toString() {
		return "user {" +
				id+ " "+
				"age :"+ age +
				"historique_name :"+ historique_name + "liked_restaurants : "+liked_restaurants ;
	}
}
