package com.game.entity;



import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private long id;

    private String name;

    private String title;

    @Enumerated(EnumType.STRING)
    private Race race;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    private Integer experience;

    private Integer level;

    private Integer untilNextLevel;

    private Date birthday;

    private boolean banned;



    public Player() {
    }

    public Player(Long id, String name, String title, Race race,
                  Profession profession, Integer experience,
                  Integer level, Integer untilNextLevel,
                  Date birthday, boolean banned){
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }



    public Integer calculateLevel(){
        int result = 0;
        if (experience != null) {
            result =(int) ((Math.sqrt(2500 + 200 * experience)) - 50) / 100;
        }
        return result;
    }

    public Integer expUntilNextLevel() {
        calculateLevel();
        int result = 0;
        if (experience != null) {
            result = 50 * (calculateLevel() + 1) * (calculateLevel() + 2) - experience;
        }
        return result;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean getBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
