package system.service.common;

import controller.ApplicationController;
import ui.AbstractMenu;
import ui.Prompt;

public class WithdrawApplicationService extends AbstractMenu {
    private ApplicationController applicationController;

    public WithdrawApplicationService(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Withdraw Application ===");
    }
    
    @Override
    public void handleInput() {
        boolean success = applicationController.withdrawApplication();
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
