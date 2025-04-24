package system.service.common;

import controller.NotificationController;
import entity.model.Notification;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewNotificationsService extends AbstractMenu {
    private NotificationController notificationController;
    private String userNric;

    public ViewNotificationsService(NotificationController controller, String userNric) {
        this.userNric = userNric;
        this.notificationController = controller;
    }

    @Override
    public void display() {
        System.out.println("\n=== Notifications ===");
        List<Notification> list = notificationController.getForUser(userNric);
        if (list.isEmpty()) {
            System.out.println("No notifications.");
        } else {
            list.forEach(n -> System.out.println("ID: " +
                n.getId() + ". " + n.toString()));
        }
        System.out.println("Type notification ID to mark read, or 'b' to back:");
    }

    @Override
    public void handleInput() {
        List<Notification> list = notificationController.getForUser(userNric);
        String input = Prompt.prompt("");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        try {
            int id = Integer.parseInt(input);
            if (list.stream().noneMatch(n -> n.getId() == id)) {
                System.out.println("Invalid input.");
                return;
            }
            notificationController.markAsRead(id);
            System.out.println("Marked as read.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}