package com.example.server.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String login;
	private String pwd;
	private int age;
	@ElementCollection
	private List<String> preferences;
	@ElementCollection
	private List<String> historique_id;
	@ElementCollection
	private List<String> historique_name;
	@ElementCollection
	private List<String> liked_restaurants;
	@ElementCollection
	private List<String> restaurants_suggere;


	public List<String> getLiked_restaurants() {
		return liked_restaurants;
	}

	public void setLiked_restaurants(List<String> historique_like) {
		this.liked_restaurants = historique_like;
	}

	public void setHistorique_Restaurants_id(List<String> historique_id) {
		this.historique_id = historique_id;
	}

	public User(String name, String login, String pwd, List<String> preferences) {
		super();
		this.name = name;
		this.login = login;
		this.pwd = pwd;
		this.preferences = preferences;
	}

	public User(String name, String login, int age,String pwd, List<String> preferences) {
		super();
		this.name = name;
		this.login = login;
		this.age = age;
		this.pwd = pwd;
		this.preferences = preferences;
	}

	public User() {
		super();
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


	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


	public List<String> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public void setRestaurants_suggere(List<String> restaurants_suggere) {
		this.restaurants_suggere = restaurants_suggere;
	}

	public List<String> getRestaurants_suggere() {
		return restaurants_suggere;
	}

	public User(UserDTO userdto) {
		this.name = userdto.getName();
		this.login = userdto.getLogin();
		this.pwd = userdto.getPwd();
		this.age = userdto.getAge();
		this.preferences = userdto.getPreferences();
	}
}
