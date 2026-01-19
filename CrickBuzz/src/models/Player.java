package models;

import enums.PlayerRole;

public class Player {
    public String name;
    public PlayerRole role; // can create set as well player can have multiple roles like Captain & Batsman

    public Player(String name, PlayerRole role) {
        this.name = name;
        this.role = role;
    }
}

