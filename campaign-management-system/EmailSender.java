public class EmailSender implements NotificationSender {

    @Override
    public void send(User user, Campaign campaign) {
        System.out.println("Sending Email to " + user.getEmail()
                + " : " + campaign.getContent());
    }
}