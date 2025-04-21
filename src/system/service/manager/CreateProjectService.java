package system.service.manager;

import controller.ProjectController;
import entity.model.BTOProject;
import entity.user.HDBManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ui.AbstractMenu;
import ui.Prompt;

public class CreateProjectService extends AbstractMenu {
    private HDBManager manager;
    private ProjectController projectController;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public CreateProjectService(HDBManager manager, ProjectController projectController) {
        this.manager = manager;
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Create a New BTO Project ===");
    }
    
    @Override
    public void handleInput() {
        try {
            String name = Prompt.prompt("Enter Project Name: ");
            String neighborhood = Prompt.prompt("Enter Neighborhood: ");
            int units2 = Prompt.promptInt("Enter number of 2-Room units: ");
            float price2 = Float.parseFloat(Prompt.prompt("Enter selling price for 2-Room: "));
            int units3 = Prompt.promptInt("Enter number of 3-Room units: ");
            float price3 = Float.parseFloat(Prompt.prompt("Enter selling price for 3-Room: "));
            String openDateStr = Prompt.prompt("Enter Application Opening Date (dd/MM/yyyy): ");
            String closeDateStr = Prompt.prompt("Enter Application Closing Date (dd/MM/yyyy): ");
            
            Date openDate = dateFormat.parse(openDateStr);
            Date closeDate = dateFormat.parse(closeDateStr);
            
            // Check for overlapping application periods:
            boolean overlap = false;
            for (BTOProject proj : manager.getManagedProjects()) {
                Date existingOpen = proj.getApplicationOpen();
                Date existingClose = proj.getApplicationClose();
                // Overlap if new open is not after existing close and new close is not before existing open.
                if (!openDate.after(existingClose) && !closeDate.before(existingOpen)) {
                    overlap = true;
                    System.out.println("Cannot create project. Overlaps with project: " 
                        + proj.getProjectName() + " (" + dateFormat.format(existingOpen) + " to " + dateFormat.format(existingClose) + ").");
                    break;
                }
            }
            if(overlap) {
                Prompt.prompt("Press enter to go back.");
                exit();
                return;
            }
            
            int slots = Prompt.promptInt("Enter Available HDB Officer Slots (max 10): ");
            
            BTOProject newProject = new BTOProject(name, neighborhood, price2, units2, price3, units3,
                    openDate, closeDate, manager, slots, true);
            projectController.createProject(newProject);
            manager.addManagedProject(newProject);
            System.out.println("Project created successfully.");
        } catch (ParseException e) {
            System.out.println("Date parse error. Project creation failed.");
        }
        String input = Prompt.prompt("Type 'b' to go back: ");
        if(input.equalsIgnoreCase("b")) {
            exit();
        }
    }
}
