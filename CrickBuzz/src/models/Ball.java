package models;

import enums.ExtraType;

public class Ball {
    public int runs;
    public ExtraType extraType;
    public boolean isWicket;
    public Player batsman;
    public Player bowler;

    public Ball(int runs, ExtraType extraType, boolean isWicket,
         Player batsman, Player bowler) {
        this.runs = runs;
        this.extraType = extraType;
        this.isWicket = isWicket;
        this.batsman = batsman;
        this.bowler = bowler;
    }

    public boolean isLegal() {
        return extraType == ExtraType.NONE;
    }
}

