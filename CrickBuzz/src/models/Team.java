package models;

import java.util.*;

public class Team {
    String name;
    List<Player> players;

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
    }
}

