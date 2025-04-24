package system.service.common;

import controller.ApplicationController;
import controller.NotificationController;
import entity.model.Application;
import ui.AbstractMenu;
import ui.Prompt;

public class WithdrawApplicationService extends AbstractMenu {
    private ApplicationController applicationController;
    private NotificationController notificationController;

    public WithdrawApplicationService(ApplicationController applicationController, NotificationController notificationController) {
        this.applicationController = applicationController;
        this.notificationController = notificationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Withdraw Application ===");
    }
    
    @Override
    public void handleInput() {
        Application app = applicationController.getApplication();
        boolean success = applicationController.withdrawApplication();
        if(success) {
            System.out.println("Submitting withdrawn request successfully.");
            String message = "Application withdrawn request by %s for Project Name: %s (ID: %d)".formatted(
                    app.getApplicant().getName(),
                    app.getProject().getProjectName(),
                    app.getProject().getProjectId()
            );
            String managerNric = applicationController.getApplication().getProject().getManager().getNric();
            notificationController.send(managerNric, message);
        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
