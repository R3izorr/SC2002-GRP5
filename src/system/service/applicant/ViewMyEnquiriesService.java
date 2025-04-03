package system.service.applicant;

import ui.AbstractMenu;
import ui.Prompt;
import controller.EnquiryController;

public class ViewMyEnquiriesService extends AbstractMenu {
    private EnquiryController enquiryController;
    
    public ViewMyEnquiriesService(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View My Enquiries ===");
        enquiryController.getEnquiries().forEach(e -> System.out.println(e.toString()));
        System.out.println("Type 'b' to go back.");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
