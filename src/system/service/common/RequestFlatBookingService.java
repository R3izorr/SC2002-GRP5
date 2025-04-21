package system.service.common;

import controller.ApplicationController;
import ui.AbstractMenu;
import ui.Prompt;

public class RequestFlatBookingService extends AbstractMenu {
    private ApplicationController applicationController;

    public RequestFlatBookingService(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Request Flat Booking ===");
    }
    
    @Override
    public void handleInput() {
        boolean success = applicationController.requestBooking();
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
