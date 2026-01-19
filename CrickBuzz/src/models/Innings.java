package models;

import strategy.MatchRuleStrategy;

import java.util.*;

public class Innings {

    Team battingTeam;
    Team bowlingTeam;
    MatchRuleStrategy ruleStrategy;

    // Batting state
    public Player striker;
    public Player nonStriker;
    Queue<Player> yetToBat;
    Map<Player, BattingStats> battingStats = new HashMap<>();

    // Bowling state
    public Player currentBowler;
    Map<Player, BowlingStats> bowlingStats = new HashMap<>();

    // Over state
    public List<Over> overs = new ArrayList<>();
    public Over currentOver = new Over();

    public int totalRuns = 0;
    public int totalWickets = 0;

    public Innings(Team battingTeam,
                   Team bowlingTeam,
                   MatchRuleStrategy ruleStrategy) {

        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.ruleStrategy = ruleStrategy;

        // Init batting order
        this.yetToBat = new LinkedList<>(battingTeam.players);
        this.striker = yetToBat.poll();
        this.nonStriker = yetToBat.poll();

        battingStats.put(striker, new BattingStats());
        battingStats.put(nonStriker, new BattingStats());

        overs.add(currentOver);
    }

    public void setCurrentBowler(Player bowler) {
        this.currentBowler = bowler;
        bowlingStats.putIfAbsent(bowler, new BowlingStats());
    }

    public void addBall(Ball ball) {

        // Validation
        if (ball.batsman != striker) {
            throw new IllegalStateException("Wrong batsman on strike");
        }

        totalRuns += ball.runs;

        // Batting stats
        BattingStats bs = battingStats.get(striker);
        bs.runs += ball.runs;

        if (ball.isLegal()) {
            bs.balls++;
        }

        // Bowling stats
        BowlingStats bw = bowlingStats.get(currentBowler);
        bw.runs += ball.runs;
        if (ball.isLegal()) {
            bw.balls++;
        }

        if (ball.isWicket) {
            totalWickets++;
            bw.wickets++;

            if (!yetToBat.isEmpty()) {
                striker = yetToBat.poll();
                battingStats.put(striker, new BattingStats());
            }
        }

        boolean overCompleted = currentOver.addBall(ball);

        // Strike rotation on odd runs
        if (ball.isLegal() && ball.runs % 2 == 1) {
            swapStrike();
        }

        // Over completion
        if (overCompleted) {
            swapStrike();
            if (overs.size() < ruleStrategy.maxOvers()) {
                currentOver = new Over();
                overs.add(currentOver);
            }
        }
    }

    public boolean isCompleted() {
        return ruleStrategy.isInningsComplete(this);
    }

    public String getOvers() {
        int completedOvers = overs.size() - 1;
        int balls = currentOver.legalBalls;
        return completedOvers + "." + balls;
    }

    private void swapStrike() {
        Player temp = striker;
        striker = nonStriker;
        nonStriker = temp;
    }
}
