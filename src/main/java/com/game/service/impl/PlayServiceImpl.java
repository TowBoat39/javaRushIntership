package com.game.service.impl;

import com.game.entity.Player;
import com.game.exceptions.IdNotValidException;
import com.game.exceptions.NotFoundException;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class PlayServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    int pageNumber;
    int pageSize;
    String order;

    @Override
    public Player addPlayer(Player player) {
        Player player1 = new Player();
        if (player.getName() == null) {
            throw new IdNotValidException();
            //TODO заменить эксепшены на нормальные
        } else {
            if (player.getName().length() > 12) {
                throw new IdNotValidException();
            } else {
                player1.setName(player.getName());
            }
        }
        if (player.getTitle().length() > 30) {
            throw new IdNotValidException();
        } else {
            player1.setTitle(player.getTitle());
        }
        player1.setRace(player.getRace());
        player1.setProfession(player.getProfession());
        dataValidate(player.getBirthday());
        player1.setBirthday(player.getBirthday());
        player1.setBanned(player.getBanned());
        if (player.getExperience() < 10000000) {
            if (player.getExperience() > 0) {
                player1.setExperience(player.getExperience());
            } else throw new IdNotValidException();
        } else throw new IdNotValidException();
        player1.setLevel(player.currentLevel());
        player1.setUntilNextLevel(player.expUntilNextLevel());

        return playerRepository.saveAndFlush(player1);
    }

    public Player read(long id) {
        Optional<Player> player =playerRepository.findById(id);
        if (!player.isPresent()) return null;
        return playerRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        idValidate(id);
        playerRepository.findById(id).orElseThrow(NotFoundException::new);
        playerRepository.deleteById(id);
    }


    @Override
    public Player updatePlayer(Player player, long id) {
        idValidate(id);
        Player player1 = playerRepository.findById(id).orElseThrow(NotFoundException::new);

        if (player != null ) {
            if (player.getName() != null) {
                player1.setName(player.getName());
            }
            if (player.getTitle() != null) {
                player1.setTitle(player.getTitle());
            }
            if (player.getRace() != null) {
                player1.setRace(player.getRace());
            }
            if (player.getProfession() != null) {
                player1.setProfession(player.getProfession());
            }
            if (player.getBirthday() != null) {
                dataValidate(player.getBirthday());
                player1.setBirthday(player.getBirthday());
            }
            player1.setBanned(player.getBanned());
            if (player.getExperience() != null) {
                if (player.getExperience() < 10000000) {
                    if (player.getExperience() > 0) {
                        player1.setExperience(player.getExperience());
                    } else throw new IdNotValidException();
                } else throw new IdNotValidException();
            }
            if (player.expUntilNextLevel() != null) {
                player1.setUntilNextLevel(player.expUntilNextLevel());
            }
            if (player.currentLevel() != null) {
                player1.setLevel(player.currentLevel());
            }
        }

        return playerRepository.save(player1);

    }

    @Override
    public List<Player> getAll() {

        String order = "id";
        int pageNumber = 0;
        int pageSize = 3;

        Page<Player> page = playerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(order)));
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : page) {
            players.add(player);
        }
        return players;
    }

    @Override
    public Player getPlayerById(Long id) {
        idValidate(id);
        Player player = playerRepository.findById(id).orElseThrow(NotFoundException::new);
        return player;
    }

    @Override
    public Page<Player> getPlayerCount(int pageNumber, int pageSize,  String order) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.order = order;

        Page<Player> page = playerRepository.findAll(PageRequest.of(this.pageNumber, this.pageSize, Sort.by(this.order)));

        return page;
    }

    public void idValidate(long id) {
        if (id % 1 == 0) {
            if (id > 0) {
            } else throw new IdNotValidException();
        } else throw new IdNotValidException();
    }

    public void dataValidate(Date date) {
        Date dateBefore;
        Date dateAfter;
        Date mainDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dateAfter = sdf.parse("2000-01-01");
            dateBefore = sdf.parse("3000-01-01");
            mainDate = date;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DateRangeValidator dateRangeValidator = new DateRangeValidator(dateAfter, dateBefore);
        if(dateRangeValidator.isWithinRange(mainDate)){

        }else{
            throw new IdNotValidException();
        }

    }

    public static class DateRangeValidator {

        private final Date startDate;
        private final Date endDate;

        public DateRangeValidator(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean isWithinRange(Date testDate) {
            return testDate.getTime() >= startDate.getTime() &&
                    testDate.getTime() <= endDate.getTime();
        }


    }
}