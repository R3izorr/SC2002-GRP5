package system.service.manager;

import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.ApplicationStatus;
import model.user.HDBManager;
import repository.ApplicationRepository;
import ui.AbstractMenu;
import ui.Prompt;


public class ManageApplicantWithdrawalsService extends AbstractMenu {
    private HDBManager manager;
    private ApplicationRepository applicationRepository;

    public ManageApplicantWithdrawalsService(HDBManager manager, ApplicationRepository applicationRepository) {
        this.manager = manager;
        this.applicationRepository = applicationRepository;
    }


    @Override
    public void display() {
        System.out.println("\n=== Manage Withdrawal Requests ===");
        List<Application> requests = new ArrayList<>();
        for (Application app : applicationRepository.getApplications()) {
            if (app.getProject().getManager().equals(manager) &&
                app.getStatus() == ApplicationStatus.WITHDRAW_REQUESTED) {
                requests.add(app);
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
        System.out.println("Type the number to process, or 'b' to go back.");
    }

    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
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
        for (Application app : applicationRepository.getApplications()) {
            if (app.getProject().getManager().equals(manager) &&
                app.getStatus() == ApplicationStatus.WITHDRAW_REQUESTED) {
                requests.add(app);
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
                applicationRepository.saveApplications();
                System.out.println("Withdrawal approved. Application status set to WITHDRAWN.");
                break;
            case 2:
                target.setStatus(ApplicationStatus.PENDING);
                applicationRepository.saveApplications();
                System.out.println("Withdrawal rejected. Application status reset to PENDING.");
                break;
            default:
                System.out.println("Invalid decision.");
                break;
        }
    }
}
