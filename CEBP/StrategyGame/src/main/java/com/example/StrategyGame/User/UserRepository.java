package com.example.StrategyGame.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByLobby_LobbyId(Integer lobbyId);
}
