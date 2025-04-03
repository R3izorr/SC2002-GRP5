package system.service.manager;

import ui.AbstractMenu;
import ui.Prompt;
import java.util.List;
import model.BTOProject;

public class ViewAllProjectsService extends AbstractMenu {
    private List<BTOProject> projects;
    
    public ViewAllProjectsService(List<BTOProject> projects) {
        this.projects = projects;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View All Projects ===");
        if(projects.isEmpty()){
            System.out.println("No projects available.");
        } else {
            for(BTOProject proj : projects){
                System.out.println(proj.toStringForManagerOfficer());
            }
        }
        System.out.println("Type 'b' to go back.");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your input: ");
        if(input.equalsIgnoreCase("b")){
            exit();
        } else {
            System.out.println("Invalid input. Type 'b' to go back.");
        }
    }
}

