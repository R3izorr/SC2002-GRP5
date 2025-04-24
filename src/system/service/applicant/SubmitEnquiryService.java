package system.service.applicant;

import controller.EnquiryController;
import ui.AbstractMenu;
import ui.Prompt;

public class SubmitEnquiryService extends AbstractMenu {
    private EnquiryController enquiryController;
    
    public SubmitEnquiryService(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Submit Enquiry ===");
        
    }
    
    @Override
    public void handleInput() {
        int projId = Prompt.promptInt("Enter project ID for enquiry: ");
        String message = Prompt.prompt("Enter your enquiry message: ");
        enquiryController.submitEnquiry(projId, message);
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
