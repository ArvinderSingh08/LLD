package factory;

import model.ChannelType;
import sender.EmailSender;
import sender.NotificationSender;
import sender.SmsSender;

public class SenderFactory {

    public static NotificationSender getSender(ChannelType type) {
        switch (type) {
            case SMS:
                return new SmsSender();
            case EMAIL:
                return new EmailSender();
            default:
                throw new IllegalArgumentException("Unsupported channel");
        }
    }
}
