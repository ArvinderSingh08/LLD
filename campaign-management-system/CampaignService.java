import java.time.LocalDateTime;
import java.util.List;

public class CampaignService {

    private final CampaignScheduler scheduler;

    public CampaignService() {
        this.scheduler = new CampaignScheduler(new CampaignExecutor());
    }

    public void createAndSend(Campaign campaign, List<User> users) {
        scheduler.schedule(campaign, users, LocalDateTime.now());
    }
}