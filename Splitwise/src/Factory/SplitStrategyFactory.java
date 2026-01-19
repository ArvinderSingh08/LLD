package Factory;

import Strategy.EqualSplitStrategy;
import Strategy.PercentageSplitStrategy;
import Strategy.SplitStrategy;
import Enum.SplitType;

public class SplitStrategyFactory {
    public static SplitStrategy getStrategy(SplitType type) throws Exception {
        switch(type) {
            case EQUAL -> {
                return new EqualSplitStrategy();
            }
            case PERCENTAGE -> {
                return new PercentageSplitStrategy();
            }
            default -> throw new Exception("Not Supported");
        }
    }
}
