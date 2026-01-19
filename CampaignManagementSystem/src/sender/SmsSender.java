package sender;

import model.Campaign;
import model.User;

public class SmsSender implements NotificationSender {

    @Override
    public void send(User user, Campaign campaign) {
        System.out.println("Sending SMS to " + user.getContact()
                + " : " + campaign.getContent());
    }
}