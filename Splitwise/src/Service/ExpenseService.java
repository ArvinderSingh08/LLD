package Service;

import Factory.SplitStrategyFactory;
import Model.Expense;
import Model.Group;
import Model.Split;
import Model.User;

import java.util.List;
import java.util.Map;
import Enum.SplitType;
import Strategy.SplitStrategy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpenseService {
    private final BalanceSheetService balanceSheetService;

    public void addExpense(Group group, String description, double amount, User paidBy,
                           List<User> participants, SplitType splitType, Map<User, Double> metadata) throws Exception {

        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        List<Split> splits = strategy.split(amount, participants, metadata);
        Expense expense = new Expense(description, amount, paidBy, splits, splitType);
        group.addExpense(expense);

        balanceSheetService.updateBalances(group, paidBy, splits);
    }
}
