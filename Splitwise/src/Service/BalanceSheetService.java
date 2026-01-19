package Service;

import Model.Group;
import Model.Split;
import Model.User;

import java.util.List;

public class BalanceSheetService {
    public void updateBalances(Group group, User paidBy, List<Split> splits) {
        // sum up all the splits
        double totalAmount = splits.stream().mapToDouble(Split::getAmount).sum();
        // total paid by user
        group.getBalanceSheet(paidBy).addTotalPaid(totalAmount);

        for (Split split : splits) {
            User user = split.getUser();
            double amt = split.getAmount();
            // expense paid by user
            group.getBalanceSheet(user).addTotalExpense(amt);
            if (!user.equals(paidBy)) {
                // paidBy ko -amt dene hai
                group.getBalanceSheet(user).addBalance(paidBy, -amt);
                // user se amt lene hai
                group.getBalanceSheet(paidBy).addBalance(user, amt);
            }
        }
    }
}
