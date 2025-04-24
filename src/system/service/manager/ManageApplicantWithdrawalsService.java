package system.service.manager;

import controller.ApplicationController;
import controller.NotificationController;
import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.user.HDBManager;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;


public class ManageApplicantWithdrawalsService extends AbstractMenu {
    private HDBManager manager;
    private ApplicationController applicationController;
    private NotificationController notificationController;

    public ManageApplicantWithdrawalsService(HDBManager manager, ApplicationController applicationController, 
            NotificationController notificationController) {
        this.manager = manager;
        this.applicationController = applicationController;
        this.notificationController = notificationController;
    }


    @Override
    public void display() {
        System.out.println("\n=== Manage Withdrawal Requests ===");
        List<Application> requests = new ArrayList<>();
        for (BTOProject project : manager.getManagedProjects()) {
            for (Application app : applicationController.getApplicationByProjectId(project.getProjectId())) {
                if (app.getStatus() == ApplicationStatus.WITHDRAW_REQUESTED) {
                    requests.add(app);
                }
            }
        }
        if (requests.isEmpty()) {
            System.out.println("No withdrawal requests at this time.");
            return;
        }
        System.out.println("Pending Withdrawal Requests:");
        for (int i = 0; i < requests.size(); i++) {
            Application app = requests.get(i);
            System.out.printf("%d. Applicant Name: %s | Project ID: %d | Project Name: %s | Flat Type: %s\n" ,
                i+1, app.getApplicant().getName(),
                app.getProject().getProjectId(), app.getProject().getProjectName(), app.getFlatType());
        }
    }

    @Override
    public void handleInput() {
        String input = Prompt.prompt("Type the number to process, or 'b' to go back: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number or 'b'.");
            return;
        }
        // Rebuild the list of requests
        List<Application> requests = new ArrayList<>();
        for (BTOProject project : manager.getManagedProjects()) {
            for (Application app : applicationController.getApplicationByProjectId(project.getProjectId())) {
                if (app.getStatus() == ApplicationStatus.WITHDRAW_REQUESTED) {
                    requests.add(app);
                }
            }
        }
        if (choice < 1 || choice > requests.size()) {
            System.out.println("Selection out of range.");
            return;
        }
        Application target = requests.get(choice - 1);
        System.out.println("1. Approve withdrawal\n2. Reject withdrawal");
        int decision = Prompt.promptInt("Enter your decision: ");
        switch (decision) {
            case 1:
                target.setStatus(ApplicationStatus.WITHDRAWN);
                applicationController.updateApplication();
                System.out.println("Withdrawal approved. Application status set to WITHDRAWN.");
                // Notify the applicant
                String message = "Your withdrawal request for project %s (ID: %d) has been approved.".formatted(
                        target.getProject().getProjectName(),
                        target.getProject().getProjectId()
                );
                notificationController.send(target.getApplicant().getNric(), message);
                break;
            case 2:
                target.setStatus(ApplicationStatus.PENDING);
                applicationController.updateApplication();
                System.out.println("Withdrawal rejected. Application status reset to PENDING.");
                // Notify the applicant
                String rejectionMessage = "Your withdrawal request for project %s (ID: %d) has been rejected.".formatted(
                        target.getProject().getProjectName(),
                        target.getProject().getProjectId()
                );
                notificationController.send(target.getApplicant().getNric(), rejectionMessage);
                break;
            default:
                System.out.println("Invalid decision.");
                break;
        }
    }
}
