import enums.ExtraType;
import enums.PlayerRole;
import models.*;
import observer.ScoreboardObserver;
import service.MatchService;
import strategy.MatchRuleStrategy;
import strategy.T20RuleStrategy;

import java.util.List;

public class CricketApp {

    public static void main(String[] args) {

        // ---------------- Players ----------------
        Player virat = new Player("Virat", PlayerRole.BATSMAN);
        Player rohit = new Player("Rohit", PlayerRole.BATSMAN);
        Player surya = new Player("Surya", PlayerRole.BATSMAN);
        Player bumrah = new Player("Bumrah", PlayerRole.BOWLER);

        Player smith = new Player("Smith", PlayerRole.BATSMAN);
        Player starc = new Player("Starc", PlayerRole.BOWLER);

        // ---------------- Teams ----------------
        Team india = new Team("India", List.of(virat, rohit, surya, bumrah));
        Team australia = new Team("Australia", List.of(smith, starc));

        // ---------------- Match ----------------
        MatchRuleStrategy rules = new T20RuleStrategy(); // assume maxOvers = 2
        Match match = new Match("MATCH_001", india, australia, rules);
        match.registerObserver(new ScoreboardObserver());

        MatchService service = new MatchService();
        service.startMatch(match);

        System.out.println("===== MATCH STARTED =====");

        // ==================================================
        // 🏏 FIRST INNINGS — INDIA BATTING
        // ==================================================
        Innings innings1 = match.getCurrentInnings();
        innings1.setCurrentBowler(starc);

        // -------- Over 0 --------
        playBall(service, innings1,
                new Ball(1, ExtraType.NONE, false, virat, starc));

        playBall(service, innings1,
                new Ball(2, ExtraType.NONE, false, rohit, starc));

        playBall(service, innings1,
                new Ball(1, ExtraType.NONE, false, rohit, starc)); // strike rotates

        playBall(service, innings1,
                new Ball(0, ExtraType.NONE, false, virat, starc));

        playBall(service, innings1,
                new Ball(0, ExtraType.NONE, true, virat, starc)); // wicket

        playBall(service, innings1,
                new Ball(2, ExtraType.NONE, false, surya, starc)); // over ends

        // -------- Over 1 --------
        playBall(service, innings1,
                new Ball(4, ExtraType.NONE, false, rohit, starc));

        playBall(service, innings1,
                new Ball(1, ExtraType.NONE, false, rohit, starc)); // strike rotates

        playBall(service, innings1,
                new Ball(0, ExtraType.NONE, false, surya, starc));

        playBall(service, innings1,
                new Ball(6, ExtraType.NONE, false, surya, starc));

        playBall(service, innings1,
                new Ball(0, ExtraType.NONE, true, surya, starc)); // wicket

        playBall(service, innings1,
                new Ball(1, ExtraType.NONE, false, bumrah, starc)); // innings ends

        // ==================================================
        // 🏏 SECOND INNINGS — AUSTRALIA BATTING
        // ==================================================
//        System.out.println("===== INNINGS CHANGE =====");
//
//        Innings innings2 = match.getCurrentInnings();
//        innings2.setCurrentBowler(bumrah);
//
//        playBall(service, innings2,
//                new Ball(1, ExtraType.NONE, false, smith, bumrah));
//
//        playBall(service, innings2,
//                new Ball(0, ExtraType.NONE, true, smith, bumrah)); // wicket
//
//        playBall(service, innings2,
//                new Ball(4, ExtraType.NONE, false, smith, bumrah));

        System.out.println("===== DEMO COMPLETED =====");
    }

    // ---------------------------------------------------
    // Helper: plays a ball and prints REAL derived state
    // ---------------------------------------------------
    private static void playBall(MatchService service,
                                 Innings innings,
                                 Ball ball) {

        service.addBall("MATCH_001", ball);
        printState(ball, innings);
    }

    // ---------------------------------------------------
    // Helper: prints state from domain objects ONLY
    // ---------------------------------------------------
    private static void printState(Ball ball, Innings innings) {

        System.out.println("BALL EVENT");
        System.out.println("  Batsman : " + ball.batsman.name);
        System.out.println("  Bowler  : " + ball.bowler.name);
        System.out.println("  Runs    : " + ball.runs);
        System.out.println("  Wicket  : " + (ball.isWicket ? "YES" : "NO"));
        System.out.println("  Extra   : " + ball.extraType);

        System.out.println("CURRENT STATE");
        System.out.println("  Striker     : " + innings.striker.name);
        System.out.println("  Non-Striker : " + innings.nonStriker.name);
        System.out.println("  Bowler      : " + innings.currentBowler.name);
        System.out.println("  Score       : "
                + innings.totalRuns + "/" + innings.totalWickets
                + " (" + innings.getOvers() + " overs)");

        System.out.println("----------------------------------------");
    }
}
