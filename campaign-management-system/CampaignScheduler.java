import java.time.LocalDateTime;
import java.util.List;

public class CampaignScheduler {

    private final CampaignExecutor executor;

    public CampaignScheduler(CampaignExecutor executor) {
        this.executor = executor;
    }

    public void schedule(Campaign campaign, List<User> users, LocalDateTime time) {
        System.out.println("Campaign scheduled at: " + time);
        executor.execute(campaign, users);
    }
}