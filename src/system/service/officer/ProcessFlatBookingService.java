package system.service.officer;

import controller.ApplicationController;
import controller.NotificationController;
import controller.ProjectController;
import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.user.HDBOfficer;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ProcessFlatBookingService extends AbstractMenu {
    private HDBOfficer officer;
    private ApplicationController applicationController;
    private NotificationController notificationController;
    private ProjectController projectController;    
    
    public ProcessFlatBookingService(HDBOfficer officer, ApplicationController applicationController, 
            NotificationController notificationController, ProjectController projectController) {
        this.officer = officer;
        this.applicationController = applicationController;
        this.notificationController = notificationController;
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Process Flat Booking ===");
        if(officer.getAssignedProjects().isEmpty()){
            System.out.println("You are not approved for any projects.");
        }
    }
    
    @Override
    public void handleInput() {
        List<Application> bookingApps = new java.util.ArrayList<>();
        for (BTOProject proj : officer.getAssignedProjects()) {
            for (Application app : applicationController.getAllApplications()) {
                if (app.getProject().getProjectId() == proj.getProjectId() &&
                    app.getStatus() == ApplicationStatus.BOOKING) {
                    bookingApps.add(app);
                }
            }
        }
        if (bookingApps.isEmpty()) {
            System.out.println("No request for flat booking found.");
            String back = Prompt.prompt("Type 'b' to go back: ");
            if (back.equalsIgnoreCase("b")) {
            exit();
            }
        } else {
            System.out.println("=== Request Booking Flat for Your Assigned Projects ===");
            for (int i = 0; i < bookingApps.size(); i++) {
                Application app = bookingApps.get(i);
                System.out.println((i + 1) + ". Applicant Name: " + app.getApplicant().getName() +
                    " | Project: " + app.getProject().getProjectName() +
                    " | Flat Type Requested: " + app.getFlatType());
            }
            int choice = Prompt.promptInt("Enter the number of the application to process booking (or 0 to stop): ");
            if (choice == 0) {
                System.out.println("Stopping booking process.");
            }
            if (choice < 1 || choice > bookingApps.size()) {
                System.out.println("Invalid selection.");
            }
            Application targetApp = bookingApps.get(choice - 1);
            String flatType = targetApp.getFlatType();
            BTOProject proj = targetApp.getProject();
            if (flatType.equalsIgnoreCase("2-Room")) {
                if (proj.getUnits2Room() <= 0) {
                System.out.println("No 2-Room units available.");
                }
                proj.setUnits2Room(proj.getUnits2Room() - 1);
            } else if (flatType.equalsIgnoreCase("3-Room")) {
                if (proj.getUnits3Room() <= 0) {
                System.out.println("No 3-Room units available.");
                }
                proj.setUnits3Room(proj.getUnits3Room() - 1);
            } else {
                System.out.println("Invalid flat type in application.");
            }
            targetApp.setStatus(ApplicationStatus.BOOKED);
            System.out.println("Flat booking processed for applicant " 
                + targetApp.getApplicant().getName() + ". Application status updated to BOOKED.");
                applicationController.updateApplication();
                projectController.updateProject();
            String message = "Flat booking processed for %s for project %s (ID: %d) is completed".formatted(
                    targetApp.getApplicant().getName(),
                    targetApp.getProject().getProjectName(),
                    targetApp.getProject().getProjectId()
            );
            notificationController.send(targetApp.getApplicant().getNric(), message);
            }
            String back = Prompt.prompt("Type 'b' to go back: ");
            if (back.equalsIgnoreCase("b")) {
            exit();
        }
    }
}

