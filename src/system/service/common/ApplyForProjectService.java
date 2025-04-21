package system.service.common;

import controller.ProjectController;
import entity.model.BTOProject;
import entity.user.Applicant;
import entity.user.HDBOfficer;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ApplyForProjectService extends AbstractMenu {
    private ProjectController projectController;
    private Applicant applicant; // Can be an Applicant or HDBOfficer
    public ApplyForProjectService(ProjectController projectController, Applicant applicant) {
        this.projectController = projectController;
        this.applicant = applicant;
    }

    @Override
    public void display() {
        System.out.println("\n=== Apply for a Project ===");
        System.out.println("Available Projects:");
        List<BTOProject> projects = projectController.getVisibleProjects();
        // When an applicant is Single, call toStringForApplicant(true)
        boolean isSingle = applicant.getMaritalStatus().equalsIgnoreCase("Single");
        for(BTOProject project : projects) {
            if(isSingle){
                System.out.println("Project ID: " + project.getProjectId() + " | " + project.toStringForApplicant(true));
            } else {
                System.out.println("Project ID: " + project.getProjectId() + " | " + project.toStringForApplicant(false));
            }
        }
    }

    @Override
    public void handleInput() {
        String projIdInput = Prompt.prompt("Enter project ID to apply for or type 'b' to go back: ");
        if (projIdInput.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        int projId = Integer.parseInt(projIdInput);
        // For a single applicant, flat type is auto-set to "2-Room"
        String flatType;
        if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
            flatType = "2-Room";
            System.out.println("As a single applicant, you can only apply for 2-Room flats.");
        } else {
            flatType = Prompt.prompt("Enter flat type (2-Room/3-Room): ");
        }
        boolean applied;
        if (applicant instanceof HDBOfficer) {
            applied = projectController.applyForProject(projId, flatType, (HDBOfficer) applicant);
        } else {
            applied = projectController.applyForProject(projId, flatType);
        }
        if (applied) {
            System.out.println("Application submitted successfully. Status: Pending");
        } else {
            System.out.println("Application submission failed.");
        }
        String input = Prompt.prompt("Type 'b' to go back: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
        }
    }
}
