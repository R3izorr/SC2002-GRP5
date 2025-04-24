package system.service.common;

import ui.AbstractMenu;
import ui.Prompt;
import java.util.ArrayList;
import java.util.List;
import model.BTOProject;
import utils.FilterSettings;

public abstract class AbstractProjectViewService extends AbstractMenu {
    protected FilterSettings filterSettings;
    
    public AbstractProjectViewService(FilterSettings filterSettings) {
        this.filterSettings = filterSettings;
    }
    
    // Subclasses must supply the base projects.
    protected abstract List<BTOProject> getBaseProjects();
    
    // Apply filtering based on FilterSettings.
    protected List<BTOProject> filterProjects() {
        List<BTOProject> projects = new ArrayList<>(getBaseProjects());
        
        // Filter by neighborhood if set.
        if (filterSettings.getNeighborhood() != null && !filterSettings.getNeighborhood().isEmpty()) {
            projects.removeIf(proj -> !proj.getNeighborhood().equalsIgnoreCase(filterSettings.getNeighborhood()));
        }
        
        // Filter by project ID if set.
        if (filterSettings.getProjectId() != null) {
            projects.removeIf(proj -> proj.getProjectId() != filterSettings.getProjectId());
        }
        
        // Filter by price for 2-Room units if set.
        if (filterSettings.getMinPrice2Room() != null) {
            projects.removeIf(proj -> proj.getSellingPrice2Room() < filterSettings.getMinPrice2Room());
        }
        if (filterSettings.getMaxPrice2Room() != null) {
            projects.removeIf(proj -> proj.getSellingPrice2Room() > filterSettings.getMaxPrice2Room());
        }
        
        // Filter by price for 3-Room units if set.
        if (filterSettings.getMinPrice3Room() != null) {
            projects.removeIf(proj -> proj.getSellingPrice3Room() < filterSettings.getMinPrice3Room());
        }
        if (filterSettings.getMaxPrice3Room() != null) {
            projects.removeIf(proj -> proj.getSellingPrice3Room() > filterSettings.getMaxPrice3Room());
        }
        
        if (filterSettings.isSortAlphabetical()) {
            projects.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
        }
        return projects;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View Projects ===");
        List<BTOProject> filtered = filterProjects();
        if (filtered.isEmpty()) {
            System.out.println("No projects match your filter criteria.");
        } else {
            for (BTOProject proj : filtered) {
                System.out.println(proj.toStringForManagerOfficer());
            }
        }
        System.out.println("Type 'f' to update filters or 'b' to go back.");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your input: ");
        if(input.equalsIgnoreCase("b")){
            exit();
        } else if(input.equalsIgnoreCase("f")){
            updateFilters();
        } else {
            System.out.println("Invalid input. Please type 'f' to filter or 'b' to go back.");
            handleInput();
        }
    }
    
    // Update the filter settings
    protected void updateFilters() {
        System.out.println("\n=== Update Filter Settings ===");
        System.out.println("1. Update Neighborhood Filter");
        System.out.println("2. Update Project ID Filter");
        System.out.println("3. Update Min Price for 2-Room");
        System.out.println("4. Update Max Price for 2-Room");
        System.out.println("5. Update Min Price for 3-Room");
        System.out.println("6. Update Max Price for 3-Room");
        System.out.println("7. Clear All Filters");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(Prompt.prompt(""));
        switch(choice) {
            case 1:
                System.out.print("Enter Neighborhood (or empty to clear): ");
                String nb = Prompt.prompt("");
                filterSettings.setNeighborhood(nb.isEmpty() ? null : nb);
                break;
            case 2:
                System.out.print("Enter Project ID (or 0 to clear): ");
                int pid = Integer.parseInt(Prompt.prompt(""));
                filterSettings.setProjectId(pid == 0 ? null : pid);
                break;
            case 3:
                System.out.print("Enter minimum price for 2-Room (or 0 to clear): ");
                double min2 = Double.parseDouble(Prompt.prompt(""));
                filterSettings.setMinPrice2Room(min2 == 0 ? null : min2);
                break;
            case 4:
                System.out.print("Enter maximum price for 2-Room (or 0 to clear): ");
                double max2 = Double.parseDouble(Prompt.prompt(""));
                filterSettings.setMaxPrice2Room(max2 == 0 ? null : max2);
                break;
            case 5:
                System.out.print("Enter minimum price for 3-Room (or 0 to clear): ");
                double min3 = Double.parseDouble(Prompt.prompt(""));
                filterSettings.setMinPrice3Room(min3 == 0 ? null : min3);
                break;
            case 6:
                System.out.print("Enter maximum price for 3-Room (or 0 to clear): ");
                double max3 = Double.parseDouble(Prompt.prompt(""));
                filterSettings.setMaxPrice3Room(max3 == 0 ? null : max3);
                break;
            case 7:
                filterSettings.setNeighborhood(null);
                filterSettings.setProjectId(null);
                filterSettings.setMinPrice2Room(null);
                filterSettings.setMaxPrice2Room(null);
                filterSettings.setMinPrice3Room(null);
                filterSettings.setMaxPrice3Room(null);
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
        System.out.println("Filter settings updated.");
        // After updating, re-display the filtered projects.
        display();
        handleInput();
    }
}
