package com.example.server.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByLoginAndPwd(String login, String pwd);
    Optional<User> findByLogin(String login);


}
