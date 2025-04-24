package system.service.manager;

import controller.EnquiryController;
import entity.model.BTOProject;
import entity.model.Enquiry;
import entity.user.HDBManager;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewAndReplyEnquiriesService extends AbstractMenu {
    private HDBManager manager;
    private EnquiryController enquiryController;
    
    public ViewAndReplyEnquiriesService(HDBManager manager, EnquiryController enquiryController) {
        this.manager = manager;
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View/Reply to Enquiries ===");
        System.out.println("Press 'a' to view enquiries for ALL projects.");
        System.out.println("Press 'v' to view enquiries for your managed project.");
        System.out.println("Press 'b' to go back.");
    }
    
    @Override
    public void handleInput() {
        boolean flag = false;
        String input = Prompt.prompt("Your choice: ");
        if (input.equalsIgnoreCase("b")) {
            exit();
            return;
        }

        List<Enquiry> enquiries = new ArrayList<>();

        if (input.equalsIgnoreCase("a")) {
            // all enquiries in the system
            enquiries = enquiryController.getEnquiries();
            flag = true;
        }
        
        else if (input.equalsIgnoreCase("v")) {
            // gather enquiries for every project managed by this manager
            for (BTOProject proj : manager.getManagedProjects()) {
                enquiries.addAll(enquiryController.getEnquiriesForProject(proj.getProjectId()));
            }
        }
        
        else {
            System.out.println("Invalid input.");
            return;
        }

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            Prompt.prompt("Press anything to go back to Enquiry Menu...");
            return;
        }

        System.out.println("=== Enquiries ===");
        for (Enquiry e : enquiries) {
            System.out.println(e);
        }

        // common reply workflow
        if (flag) {
            while (true) {
                String choice = Prompt.prompt("Enter b to go back: ");
                if (choice.equalsIgnoreCase("b")) {
                    exit();
                    return;
                } else {
                    System.out.println("Invalid input");
                }
            }
        }

        else {
            String eidStr = Prompt.prompt("Enter Enquiry ID to reply (or 'b' to go back): ");
            if (eidStr.equalsIgnoreCase("b")) {
                exit();
                return;
            }
            try {
                int eid = Integer.parseInt(eidStr);
                String reply = Prompt.prompt("Enter your reply: ");
                enquiryController.replyEnquiry(eid, reply);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid enquiry ID.");
            }
        }
    }
}
