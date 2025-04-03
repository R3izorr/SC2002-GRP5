package system.service.manager;

import ui.AbstractMenu;
import ui.Prompt;
import java.util.List;
import model.BTOProject;
import model.HDBManager;
import repository.ProjectRepository;

public class ToggleProjectVisibilityService extends AbstractMenu {
    private HDBManager manager;
    private ProjectRepository projectRepository;
    
    public ToggleProjectVisibilityService(HDBManager manager, ProjectRepository projectRepository) {
        this.manager = manager;
        this.projectRepository = projectRepository;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Toggle Project Visibility ===");
        List<BTOProject> projects = manager.getManagedProjects();
        if(projects.isEmpty()){
            System.out.println("You have no projects to manage.");
        } else {
            for (int i = 0; i < projects.size(); i++){
                System.out.println((i+1) + ". " + projects.get(i).toStringForManagerOfficer());
            }
        }
        System.out.println("Enter project number to toggle visibility, or 'b' to go back:");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
        if(input.equalsIgnoreCase("b")){
            exit();
            return;
        }
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch(NumberFormatException e){
            System.out.println("Invalid input.");
            return;
        }
        List<BTOProject> projects = manager.getManagedProjects();
        if(choice < 1 || choice > projects.size()){
            System.out.println("Invalid selection.");
            return;
        }
        BTOProject selected = projects.get(choice - 1);
        selected.setVisible(!selected.isVisible());
        System.out.println("Project visibility toggled. Now: " + (selected.isVisible() ? "Visible" : "Not Visible"));
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
