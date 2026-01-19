package models;

import java.util.ArrayList;
import java.util.List;

public class Over {
    private static final int BALLS_PER_OVER = 6;

    List<Ball> balls = new ArrayList<>();
    int legalBalls = 0;

    public boolean addBall(Ball ball) {
        balls.add(ball);
        if (ball.isLegal()) {
            legalBalls++;
        }
        return legalBalls == BALLS_PER_OVER;
    }

    public boolean isCompleted() {
        return legalBalls == BALLS_PER_OVER;
    }
}
