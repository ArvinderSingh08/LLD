package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import Enum.SplitType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Expense {
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<Split> splits;
    private final SplitType splitType;

}
