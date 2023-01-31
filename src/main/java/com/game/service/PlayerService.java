package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlayerService {
    Player addPlayer(Player player);
    void delete(long id);
    Player updatePlayer(Player player, long id);
    List<Player> getAll();
    Player getPlayerById (Long id);

    Page<Player> getPlayerCount (int pageNumber, int pageSize, String order);

}
