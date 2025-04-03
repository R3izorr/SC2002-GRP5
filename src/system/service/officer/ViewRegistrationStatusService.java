package system.service.officer;

import model.HDBOfficer;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewRegistrationStatusService extends AbstractMenu {
    private HDBOfficer officer;
    
    public ViewRegistrationStatusService(HDBOfficer officer) {
        this.officer = officer;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View Registration Status ===");
        System.out.println("Approved Projects:");
        if(officer.getAssignedProjects().isEmpty()) {
            System.out.println("None");
        } else {
            officer.getAssignedProjects().forEach(proj -> 
                System.out.println(proj.toStringForManagerOfficer())
            );
        }
        System.out.println("Pending Registrations:");
        if(officer.getPendingRegistrations().isEmpty()){
            System.out.println("None");
        } else {
            officer.getPendingRegistrations().forEach(proj -> 
                System.out.println(proj.toStringForManagerOfficer())
            );
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
