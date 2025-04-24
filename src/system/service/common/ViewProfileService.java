package system.service.common;

import entity.user.User;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewProfileService extends AbstractMenu {
    private User user;

    public ViewProfileService(User user) {
        this.user = user;
    }

    @Override
    public void display() {
        System.out.println("\n=== My Profile ===");
        System.out.printf("Name           : %s%n", user.getName());
        System.out.printf("NRIC           : %s%n", user.getNric());
        System.out.printf("Age            : %d%n", user.getAge());
        System.out.printf("Marital Status : %s%n", user.getMaritalStatus());
        System.out.printf("Role           : %s%n", user.getRole());
        System.out.println("\nType 'b' to go back.");
    }

    @Override
    public void handleInput() {
        String input = Prompt.prompt("");
        if ("b".equalsIgnoreCase(input)) {
            exit();
        } else {
            System.out.println("Invalid input. Type 'b' to go back.");
        }
    }
}