package system.service.applicant;

import controller.EnquiryController;
import ui.AbstractMenu;
import ui.Prompt;

public class EditEnquiryService extends AbstractMenu {
    private EnquiryController enquiryController;
    
    public EditEnquiryService(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Edit Enquiry ===");
        if (enquiryController.getEnquiries().isEmpty()) {
            System.out.println("No enquiries found.");
        }
        else {
            System.out.println("Your enquiries:");
            enquiryController.getEnquiries().forEach(e -> System.out.println(e.toString()));
        }
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Enter enquiry ID to edit or 'b' to back: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        int enquiryId = Integer.parseInt(input);
        String newMessage = Prompt.prompt("Enter new message: ");
        enquiryController.editEnquiry(enquiryId, newMessage);
        System.out.println("Enquiry updated successfully.");
        System.out.println("Type 'b' to go back.");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
