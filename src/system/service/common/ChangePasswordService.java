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
        String newPassword = Prompt.prompt("Enter new password: ");
        userController.changePassword(user, newPassword);
        System.out.println("Password changed successfully. Please re-login!.");
        System.out.println("Logging out...");
        exit();
    }
}
