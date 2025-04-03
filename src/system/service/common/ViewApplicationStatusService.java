package system.service.common;

import controller.ProjectController;
import model.Application;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewApplicationStatusService extends AbstractMenu {
    private ProjectController projectController;
    
    public ViewApplicationStatusService(ProjectController projectController) {
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View Application Status ===");
    }
    
    @Override
    public void handleInput() {
        Application app = projectController.getApplication();
        if(app != null){
            System.out.println("\n=== Application Details ===");
            System.out.println("---------------------------------");
            System.out.printf("%-15s: %s%n", "Project ID", app.getProject().getProjectId());
            System.out.printf("%-15s: %s%n", "Project Name", app.getProject().getProjectName());
            System.out.printf("%-15s: %s%n", "Flat Type", app.getFlatType());
            System.out.printf("%-15s: %s%n", "Status", app.getStatus());
            System.out.println("---------------------------------");
        } else {
            System.out.println("No application found.");
        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
