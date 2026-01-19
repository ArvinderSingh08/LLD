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