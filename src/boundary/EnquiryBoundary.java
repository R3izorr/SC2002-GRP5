package boundary;

import controller.EnquiryController;
import java.util.List;
import java.util.Scanner;
import model.Enquiry;

public class EnquiryBoundary {
    private EnquiryController enquiryController;
    private Scanner scanner;
    
    public EnquiryBoundary(EnquiryController enquiryController) {
        this.enquiryController = enquiryController;
        this.scanner = new Scanner(System.in);
    }
    
    public void displayEnquiries() {
        List<Enquiry> enquiries = enquiryController.getEnquiries();
        if(enquiries.isEmpty()){
            System.out.println("No enquiries found.");
            return;
        }
        System.out.println("=== Enquiries ===");
        for (Enquiry enquiry : enquiries) {
            System.out.println(enquiry.toString());
        }
    }
    
    public void submitEnquiry() {
        int projectId = Integer.parseInt(prompt("Enter project ID for enquiry: "));
        String message = prompt("Enter your enquiry message: ");
        enquiryController.submitEnquiry(projectId, message);
        System.out.println("Enquiry submitted.");
    }
    
    public void editEnquiry() {
        int enquiryId = Integer.parseInt(prompt("Enter enquiry ID to edit: "));
        String newMessage = prompt("Enter new message: ");
        enquiryController.editEnquiry(enquiryId, newMessage);
    }
    
    public void deleteEnquiry() {
        int enquiryId = Integer.parseInt(prompt("Enter enquiry ID to delete: "));
        enquiryController.deleteEnquiry(enquiryId);
    }
    
    // New method for replying to an enquiry.
    public void replyEnquiry() {
        int enquiryId = Integer.parseInt(prompt("Enter enquiry ID to reply: "));
        String newReply = prompt("Enter your reply: ");
        enquiryController.replyEnquiry(enquiryId, newReply);
    }
    
    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
