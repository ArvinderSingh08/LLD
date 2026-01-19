package Strategy;

import Model.Split;
import Model.User;

import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    public List<Split> split(double totalAmount, List<User> participants, Map<User, Double> metadata);
}
