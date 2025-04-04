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
            System.out.println(app.getApplicationDetail());
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
