package com.game.service.impl;

import com.game.entity.Player;
import com.game.exceptions.BadRequestException;
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
        if (player.getName() != null){
            updatePlayer(player1, player);
        }else throw new BadRequestException();
        return playerRepository.saveAndFlush(player1);
    }

    public Player read(long id) {
        Optional<Player> player = playerRepository.findById(id);
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
        Player currentPlayer = playerRepository.findById(id).orElseThrow(NotFoundException::new);
        updatePlayer(currentPlayer, player);
        return playerRepository.save(currentPlayer);

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
    public Page<Player> getPlayerCount(int pageNumber, int pageSize, String order) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.order = order;

        Page<Player> page = playerRepository.findAll(PageRequest.of(this.pageNumber, this.pageSize, Sort.by(this.order)));

        return page;
    }

    private void idValidate(long id) {
        if (id % 1 == 0) {
            if (id > 0) {
            } else throw new BadRequestException();
        } else throw new BadRequestException();
    }

    private void dataValidate(Date date) {
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
        if (dateRangeValidator.isWithInRange(mainDate)) {

        } else {
            throw new BadRequestException();
        }

    }

    private static class DateRangeValidator {

        private final Date startDate;
        private final Date endDate;

        public DateRangeValidator(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public boolean isWithInRange(Date testDate) {
            return testDate.getTime() >= startDate.getTime() &&
                    testDate.getTime() <= endDate.getTime();
        }


    }

    private void updatePlayer(Player playerForUpdate, Player playerFromRequest){

        if (playerFromRequest != null ) {
            if (playerFromRequest.getName() != null) {
                if(playerFromRequest.getName().length() <= 12){
                    playerForUpdate.setName(playerFromRequest.getName());
                }else throw new BadRequestException();
            }
            if (playerFromRequest.getTitle() != null) {
                if(playerFromRequest.getTitle().length() <= 30){
                    playerForUpdate.setTitle(playerFromRequest.getTitle());
                }else throw new BadRequestException();

            }
            if (playerFromRequest.getRace() != null) {
                playerForUpdate.setRace(playerFromRequest.getRace());
            }
            if (playerFromRequest.getProfession() != null) {
                playerForUpdate.setProfession(playerFromRequest.getProfession());
            }
            if (playerFromRequest.getBirthday() != null) {
                dataValidate(playerFromRequest.getBirthday());
                playerForUpdate.setBirthday(playerFromRequest.getBirthday());
            }
            playerForUpdate.setBanned(playerFromRequest.getBanned());

            if (playerFromRequest.getExperience() != null) {
                if(playerFromRequest.getExperience() <= 10000000){
                    if(playerFromRequest.getExperience() > 0){
                        playerForUpdate.setExperience(playerFromRequest.getExperience());
                    }else throw new BadRequestException();
                }else throw new BadRequestException();

            }
                playerForUpdate.setLevel(playerForUpdate.calculateLevel());
                playerForUpdate.setUntilNextLevel(playerForUpdate.expUntilNextLevel());
        }

    }
}