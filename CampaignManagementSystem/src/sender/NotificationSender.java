package sender;

import model.Campaign;
import model.User;

public interface NotificationSender {
    void send(User user, Campaign campaign);
}
