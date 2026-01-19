import java.util.List;

public class CampaignExecutor {

    public void execute(Campaign campaign, List<User> users) {
        for (User user : users) {
            for (ChannelType channel : campaign.getChannels()) {
                NotificationSender sender = SenderFactory.getSender(channel);
                sender.send(user, campaign);
            }
        }
    }
}