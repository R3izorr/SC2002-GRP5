package system.service.manager;

import controller.ProjectController;
import entity.model.BTOProject;
import entity.user.HDBManager;
import entity.user.HDBOfficer;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class EditDeleteProjectService extends AbstractMenu {
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private ProjectController projectController;
    
    public EditDeleteProjectService(HDBManager manager, List<HDBOfficer> officers, ProjectController projectController) {
        this.officers = officers;
        this.manager = manager;
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Edit/Delete Project Listing ===");
        List<BTOProject> projects = manager.getManagedProjects();
        if(projects.isEmpty()){
            System.out.println("You have no projects to manage.");
        } else {
            System.out.println(manager.displayManagedProject());

        }
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Enter project ID to edit/delete or 'b' to go back: ");
        if(input.equalsIgnoreCase("b")){
            exit();
            return;
        }
        List<BTOProject> projects = manager.getManagedProjects();
        BTOProject selected = null;
        for (BTOProject project : projects) {
            if (project.getProjectId() == Integer.parseInt(input)) {
                selected = project;
                break;
            }
        }
        
        if (selected == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        String action = Prompt.prompt("Enter 'e' to edit, 'd' to delete, or 'b' to go back: ");
        if(action.equalsIgnoreCase("b")){
            return;
        }
        if (action.equalsIgnoreCase("e")){
            System.out.println("\n=== Edit Project Options ===");
            System.out.println("1. Project Name");
            System.out.println("2. Neighborhood");
            System.out.println("3. 2-Room Units");
            System.out.println("4. 2-Room Price");
            System.out.println("5. 3-Room Units");
            System.out.println("6. 3-Room Price");
            System.out.println("7. Application Opening Date");
            System.out.println("8. Application Closing Date");
            System.out.println("9. Officer Slots");
            System.out.println("10. Back to Menu");
            int editOption = Prompt.promptInt("Choose an option:");
            switch(editOption) {
                case 1:
                    String newProjectName = Prompt.prompt("Enter a new Project Name: ");
                    selected.setProjectName(newProjectName);
                    break;
                case 2:
                    String newNeighborhood = Prompt.prompt("Enter new Neighborhood: ");
                    selected.setNeighborhood(newNeighborhood);
                    break;
                case 3:
                    int newUnits2 = Prompt.promptInt("Enter new number of 2-Room units: ");
                    selected.setUnits2Room(newUnits2);
                    break;
                case 4:
                    float newPrice2 = Prompt.promptFloat("Enter new selling price for 2-Room: ");
                    selected.setSellingPrice2Room(newPrice2);
                    break;
                case 5:
                    int newUnits3 = Prompt.promptInt("Enter new number of 3-Room units: ");
                    selected.setUnits3Room(newUnits3);
                    break;
                case 6:
                    float newPrice3 = Prompt.promptFloat("Enter new selling price for 3-Room: ");
                    selected.setSellingPrice3Room(newPrice3);
                    break;
                case 7:
                    selected.setApplicationOpen(Prompt.promptDate("Enter new Application Opening Date (dd/MM/yyyy): "));
                    break;
                case 8:
                    selected.setApplicationClose(Prompt.promptDate("Enter new Application Closing Date (dd/MM/yyyy): "));
                    break;
                case 9:
                    int slots = Prompt.promptInt("Enter new Available HDB Officer Remaining Slots (max 10): ");
                    selected.setOfficerSlots(slots);
                    break;
                case 10: break;
                default:
                    System.out.println("Invalid option.");
            }
            System.out.println("Project updated.");
            projectController.updateProject();
            
        } else if (action.equalsIgnoreCase("d")){
            manager.getManagedProjects().remove(selected);
            for(HDBOfficer officer: officers) {
                for(BTOProject proj : officer.getAssignedProjects()) {
                    if (proj == selected) {
                        officer.unAssignedProject(proj);
                        break;
                    }
                }
            }
            projectController.removeProject(selected);
            System.out.println("Project deleted.");
            projectController.updateProject();
        } else {
            System.out.println("Invalid action.");
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
