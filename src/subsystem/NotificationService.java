package subsystem;

public class NotificationService {

    public void sendEmail(String email, String message) {
        System.out.println("[Notification] Email to " + email + ": " + message);
    }
}
