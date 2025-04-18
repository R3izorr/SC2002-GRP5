package system.service.common;

import controller.ProjectController;
import ui.AbstractMenu;
import ui.Prompt;

public class WithdrawApplicationService extends AbstractMenu {
    private ProjectController projectController;
    
    public WithdrawApplicationService(ProjectController projectController) {
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Withdraw Application ===");
    }
    
    @Override
    public void handleInput() {
        boolean success = projectController.withdrawApplication();
        if(success) {
            System.out.println("Submitting withdrawn request successfully.");
        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
