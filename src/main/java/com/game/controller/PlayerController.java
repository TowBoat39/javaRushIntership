package com.game.controller;

import com.game.entity.Player;
import com.game.service.impl.PlayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    public PlayServiceImpl playService;

    @Autowired
    public PlayerController(PlayServiceImpl playService) {
        this.playService = playService;
    }

    @GetMapping
    public List<Player> getPlayerList() {

        return playService.getAll();
    }

    @GetMapping("{id}")
    public Player getPlayer(@PathVariable("id") Long id) {
        return playService.getPlayerById(id);
    }


    @GetMapping("/count")
    public Page<Player> getCount(int pageNumber, int pageSize, String order) {
        return playService.getPlayerCount(pageNumber, pageSize, order);
    }


    @PostMapping
    public Player create(@RequestBody Player player) {
        return playService.addPlayer(player);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        playService.delete(id);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable("id") long id, @RequestBody Player player) {
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player currentPlayer = playService.read(id);
        if (isEmptyData(player)) return new ResponseEntity<>(currentPlayer, HttpStatus.OK);

        final Player updated = playService.updatePlayer(player, id);
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private boolean isEmptyData(Player player) {
        boolean result = player.getName() == null;
        if ((player.getTitle() != null)) result = false;
        if ((player.getRace() != null)) result = false;
        if ((player.getProfession() != null)) result = false;
        if ((player.getExperience() != null)) result = false;
        if ((player.getBirthday() != null)) result = false;

        return result;
    }
}




