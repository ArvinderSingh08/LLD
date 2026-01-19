//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import model.Campaign;
import model.ChannelType;
import model.User;
import service.CampaignService;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        /*
        * Design a campaign management system where marketers can create, schedule for later or recurring and
        * send multi-channel campaigns to a list of users. Campaign can be sms, email or be extensible to other
        * channels in future.
        * */

        User u1 = new User(
                "Arvinder",
                "a@gmail.com",
                "9999999999"
        );

        User u2 = new User(
                "Rahul",
                "r@gmail.com",
                "8888888888"
        );

        List<User> users = Arrays.asList(u1, u2);

        Campaign campaign = new Campaign(
                "C1",
                "Big Sale is Live!",
                Arrays.asList(
                        ChannelType.SMS,
                        ChannelType.EMAIL
                )
        );

        CampaignService service = new CampaignService();
        service.createAndSend(campaign, users);
    }
}
