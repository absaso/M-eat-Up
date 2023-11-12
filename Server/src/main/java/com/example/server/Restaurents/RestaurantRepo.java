package com.example.server.Restaurents;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<RestaurantDAO, String> {

}
