package system.service.officer;

import model.BTOProject;
import model.HDBOfficer;
import repository.ProjectRepository;
import ui.AbstractMenu;
import ui.Prompt;

public class RegisterForProjectService extends AbstractMenu {
    private HDBOfficer officer;
    private ProjectRepository projectRepository;
    
    public RegisterForProjectService(HDBOfficer officer, ProjectRepository projectRepository) {
        this.officer = officer;
        this.projectRepository = projectRepository;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Register for a New Project ===");
        // Display all projects (Manager/Officer view)
        for(BTOProject proj : projectRepository.getProjects()) {
            System.out.println(proj.toStringForManagerOfficer());
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
        BTOProject selected = projectRepository.getProjectById(projId);
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
        // Add registration to pending registrations.
        officer.getPendingRegistrations().add(selected);
        System.out.println("Registration submitted for project '" + selected.getProjectName() + "'. Awaiting manager approval.");
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
