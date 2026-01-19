package Service;

import Model.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import Enum.SplitType;

@AllArgsConstructor
public class GroupService {

    private final ExpenseService expenseService;
    private final DebtSimplificationService simplifier;

    public Group createGroup(String name, List<User> users){
        String id = UUID.randomUUID().toString();
        Group group = new Group(id, name);

        for(User user : users) {
            group.addMember(user);
        }

        return group;
    }

    public void addExpense(Group group, String description, double amount, User paidBy,
                           List<User> participants, SplitType splitType, Map<User, Double> metadata) throws Exception {
        expenseService.addExpense(group, description, amount, paidBy, participants, splitType, metadata);
    }

}
