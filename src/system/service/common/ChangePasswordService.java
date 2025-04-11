package system.service.common;

import controller.UserController;
import model.user.User;
import ui.AbstractMenu;
import ui.Prompt;

public class ChangePasswordService extends AbstractMenu {
    private UserController userController;
    private User user;
    
    public ChangePasswordService(UserController userController, User user) {
        this.userController = userController;
        this.user = user;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Change Password ===");
    }
    
    @Override
    public void handleInput() {
        String option = Prompt.prompt("Enter 's' to set a new password or 'b' to cancel: ");
        if (option.equals("b")) {
            exit();
        } else if (option.equals("s")) {
            String newPassword = Prompt.prompt("Enter new password: ");
            userController.changePassword(user, newPassword);
            System.out.println("Password changed successfully. Please re-login!.");
            System.out.println("Logging out...");
            exit();
        } else {
            System.out.println("Invalid input. Please try again.");
            return;
        }
    }

}
