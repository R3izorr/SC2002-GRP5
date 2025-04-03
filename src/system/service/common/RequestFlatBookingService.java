package system.service.common;

import ui.AbstractMenu;
import ui.Prompt;
import controller.ProjectController;

public class RequestFlatBookingService extends AbstractMenu {
    private ProjectController projectController;
    
    public RequestFlatBookingService(ProjectController projectController) {
        this.projectController = projectController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Request Flat Booking ===");
    }
    
    @Override
    public void handleInput() {
        boolean success = projectController.requestBooking();
        if(success){
            System.out.println("Flat booking requested successfully.");
        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
