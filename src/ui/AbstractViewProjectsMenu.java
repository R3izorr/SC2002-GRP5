package ui;

import entity.model.BTOProject; 
import java.util.ArrayList;
import java.util.List;
import utils.FilterSettings;

public abstract class AbstractViewProjectsMenu extends AbstractMenu {
    protected List<BTOProject> projects;
    protected FilterSettings filterSettings;
    
    public AbstractViewProjectsMenu(List<BTOProject> projects, FilterSettings filterSettings) {
        this.projects = projects;
        this.filterSettings = filterSettings;
    }

    // Subclasses provide the desired project string representation.
    protected abstract String getProjectString(BTOProject proj);
    
    @Override
    public void display() {
        System.out.println("\n=== View Projects ===");
        List<BTOProject> filtered = new ArrayList<>(projects);
        
        // Apply neighborhood filter.
        if(filterSettings.getNeighborhood() != null && !filterSettings.getNeighborhood().isEmpty()){
            filtered.removeIf(proj -> !proj.getNeighborhood().equalsIgnoreCase(filterSettings.getNeighborhood()));
        }
        
        // Apply project ID filter.
        if(filterSettings.getProjectId() != null){
            filtered.removeIf(proj -> proj.getProjectId() != filterSettings.getProjectId());
        }
        
        // Apply price filters for 2-Room.
        if(filterSettings.getMinPrice2Room() != null){
            filtered.removeIf(proj -> proj.getSellingPrice2Room() < filterSettings.getMinPrice2Room());
        }
        if(filterSettings.getMaxPrice2Room() != null){
            filtered.removeIf(proj -> proj.getSellingPrice2Room() > filterSettings.getMaxPrice2Room());
        }
        
        // Apply price filters for 3-Room.
        if(filterSettings.getMinPrice3Room() != null){
            filtered.removeIf(proj -> proj.getSellingPrice3Room() < filterSettings.getMinPrice3Room());
        }
        if(filterSettings.getMaxPrice3Room() != null){
            filtered.removeIf(proj -> proj.getSellingPrice3Room() > filterSettings.getMaxPrice3Room());
        }
        
        // Sort alphabetically by project name if required.
        if(filterSettings.isSortAlphabetical()){
            filtered.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
        }
        
        if(filtered.isEmpty()){
            System.out.println("No projects match your criteria.");
        } else {
            for(BTOProject proj : filtered){
                System.out.println(getProjectString(proj));
            }
        }
        System.out.println("\nType 'f' to update filters or 'b' to go back.");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your input: ");
        if(input.equalsIgnoreCase("b")){
            exit();
        } else if(input.equalsIgnoreCase("f")){
            updateFilters();
        } else {
            System.out.println("Invalid input. Type 'f' to filter or 'b' to go back.");
        }
    }
    
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
                    // check invalid input
                    try {
                        if (pid < 0) {
                            System.out.println("Invalid input. Project ID cannot be negative.");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid numeric Project ID.");
                        return;
                    }
                    filterSettings.setProjectId(pid == 0 ? null : pid);
                    break;
                case 3:
                    System.out.print("Enter minimum price for 2-Room (or 0 to clear): ");
                    float min2 = Float.parseFloat(Prompt.prompt(""));
                    filterSettings.setMinPrice2Room(min2 == 0 ? null : min2);
                    break;
                case 4:
                    System.out.print("Enter maximum price for 2-Room (or 0 to clear): ");
                    float max2 = Float.parseFloat(Prompt.prompt(""));
                    filterSettings.setMaxPrice2Room(max2 == 0 ? null : max2);
                    break;
                case 5:
                    System.out.print("Enter minimum price for 3-Room (or 0 to clear): ");
                    float min3 = Float.parseFloat(Prompt.prompt(""));
                    filterSettings.setMinPrice3Room(min3 == 0 ? null : min3);
                    break;
                case 6:
                    System.out.print("Enter maximum price for 3-Room (or 0 to clear): ");
                    float max3 = Float.parseFloat(Prompt.prompt(""));
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
