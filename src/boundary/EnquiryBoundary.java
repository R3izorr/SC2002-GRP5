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
        System.out.print("Enter project ID for enquiry: ");
        int projectId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter your enquiry message: ");
        String message = scanner.nextLine();
        enquiryController.submitEnquiry(projectId, message);
        System.out.println("Enquiry submitted.");
    }
    
    public void editEnquiry() {
        System.out.print("Enter enquiry ID to edit: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new message: ");
        String newMessage = scanner.nextLine();
        enquiryController.editEnquiry(enquiryId, newMessage);
        System.out.println("Enquiry updated.");
    }
    
    public void deleteEnquiry() {
        System.out.print("Enter enquiry ID to delete: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
        enquiryController.deleteEnquiry(enquiryId);
        System.out.println("Enquiry deleted.");
    }
}


