package system.service.applicant;

import controller.EnquiryController;
import ui.AbstractMenu;
import ui.Prompt;

public class DeleteEnquiryService extends AbstractMenu {
    private EnquiryController enquiryController;
    
    public DeleteEnquiryService(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Delete Enquiry ===");
    }
    
    @Override
    public void handleInput() {
        int enquiryId = Prompt.promptInt("Enter enquiry ID to delete: ");
        enquiryController.deleteEnquiry(enquiryId);
        System.out.println("Enquiry deleted successfully.");
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
