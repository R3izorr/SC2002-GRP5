package system.service.common;

import controller.ProjectController;
import model.Applicant;
import model.HDBOfficer;
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
    }

    @Override
    public void handleInput() {
        int projId = Prompt.promptInt("Enter project ID to apply for: ");
        String flatType = Prompt.prompt("Enter flat type (2-Room/3-Room): ");
        boolean applied;
        // If the applicant is an HDBOfficer, use the officer-specific method.
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
