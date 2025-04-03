package system.service.applicant;

import ui.AbstractMenu;
import ui.Prompt;
import controller.EnquiryController;

public class EditEnquiryService extends AbstractMenu {
    private EnquiryController enquiryController;
    
    public EditEnquiryService(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Edit Enquiry ===");
    }
    
    @Override
    public void handleInput() {
        int enquiryId = Prompt.promptInt("Enter enquiry ID to edit: ");
        String newMessage = Prompt.prompt("Enter new message: ");
        enquiryController.editEnquiry(enquiryId, newMessage);
        System.out.println("Enquiry updated successfully.");
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
