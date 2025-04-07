package system.service.manager;

import java.util.List;
import model.BTOProject;
import model.user.HDBManager;
import ui.AbstractMenu;
import ui.Prompt;

public class ToggleProjectVisibilityService extends AbstractMenu {
    private HDBManager manager;
    
    public ToggleProjectVisibilityService(HDBManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Toggle Project Visibility ===");
        List<BTOProject> projects = manager.getManagedProjects();
        if (projects.isEmpty()) {
            System.out.println("You have no projects to manage.");
        } else {
            System.out.println(manager.displayManagedProject());
            System.out.println("Enter the Project ID to toggle visibility, or 'b' to go back:");
        }
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        List<BTOProject> projects = manager.getManagedProjects();
        BTOProject selected = projects.stream()
            .filter(project -> project.getProjectId() == Integer.parseInt(input))
            .findFirst()
            .orElse(null);
        if (selected == null) {
            System.out.println("Invalid Project ID.");
            return;
        }
        selected.setVisible(!selected.isVisible());
        System.out.println("Project visibility toggled. Now: " + (selected.isVisible() ? "Visible" : "Not Visible"));
        String back = Prompt.prompt("Type 'b' to go back: ");
        if (back.equalsIgnoreCase("b")) {
            exit();
        }
    }
}
