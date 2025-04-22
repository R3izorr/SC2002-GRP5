package system.service.officer;

import controller.NotificationController;
import controller.ProjectController;
import entity.model.BTOProject;
import entity.user.HDBOfficer;
import ui.AbstractMenu;
import ui.Prompt;

public class RegisterForProjectService extends AbstractMenu {
    private HDBOfficer officer;
    private ProjectController projectController;
    private NotificationController notificationController;

    
    public RegisterForProjectService(HDBOfficer officer, ProjectController projectController, 
            NotificationController notificationController) {
        this.officer = officer;
        this.projectController = projectController;
        this.notificationController = notificationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Register for a New Project ===");
        // Display all projects (Manager/Officer view)
        for(BTOProject proj : projectController.getVisibleProjects()) {
            System.out.println(proj.toStringforOfficer());
        }
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Enter project ID to register for (or 'b' to go back): ");
        if(input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        int projId;
        try {
            projId = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Invalid project ID.");
            return;
        }
        BTOProject selected = projectController.getProjectById(projId);
        if(selected == null) {
            System.out.println("Project not found.");
            return;
        }
        // Check if officer already approved for this project
        if(officer.getAssignedProjects().stream().anyMatch(p -> p.getProjectId() == projId)) {
            System.out.println("You are already approved for this project.");
            return;
        }
        // Check if officer already applied as an applicant for this project
        if(officer.getApplication() != null && officer.getApplication().getProject().getProjectId() == projId) {
            System.out.println("You have already applied for this project as an applicant.");
            return;
        }
        if(officer.hasOverlappingApprovedProject(selected)) {
            System.out.println("You have an overlapping approved project. Cannot register for this project.");
            return;
        }
        if(selected.getOfficerSlots() <= 0) {
            System.out.println("No available slots for this project.");
            return;
        }
        for(BTOProject proj : officer.getPendingRegistrations()) {
            if(proj.getProjectId() == projId) {
                System.out.println("You have already submitted a registration for this project. Awaiting manager approval.");
                return;
            }
            if(officer.isOverlap(proj, selected)) {
                System.out.println("You have an overlapping pending registration. Cannot register for this project.");
                return;
            }
        }  
        
        // Add registration to pending registrations.
        officer.getPendingRegistrations().add(selected);
        System.out.println("Registration submitted for project " + selected.getProjectName() + "(ID: " + selected.getProjectId() + ")" + ". Awaiting manager approval.");
        
        // Notify the project manager
        String message = "Officer %s has submitted a registration for project %s (ID: %d)".formatted(
                officer.getName(),
                selected.getProjectName(),
                selected.getProjectId()
        );
        String managerNric = selected.getManager().getNric();
        notificationController.send(managerNric, message);
        
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
