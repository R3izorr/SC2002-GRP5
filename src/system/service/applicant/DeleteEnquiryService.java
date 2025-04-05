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
        String input = Prompt.prompt("Enter enquiry ID to delete or 'b' to back: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }
        try {
            int enquiryId = Integer.parseInt(input);
            enquiryController.deleteEnquiry(enquiryId);
            System.out.println("Enquiry deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid enquiry ID or 'b' to go back.");
        }
    }
}
