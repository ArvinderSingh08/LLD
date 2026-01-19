import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        User u1 = new User("Arvinder", "a@gmail.com", "9999999999");
        User u2 = new User("Rahul", "r@gmail.com", "8888888888");

        List<User> users = Arrays.asList(u1, u2);

        Campaign campaign = new Campaign(
                "C1",
                "Big Sale is Live!",
                Arrays.asList(ChannelType.SMS, ChannelType.EMAIL)
        );

        CampaignService service = new CampaignService();
        service.createAndSend(campaign, users);
    }
}