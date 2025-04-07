package system.service.officer;

import controller.EnquiryController;
import java.util.List;
import model.BTOProject;
import model.Enquiry;
import model.user.HDBOfficer;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewAndReplyEnquiriesService extends AbstractMenu {
    private HDBOfficer officer;
    private EnquiryController enquiryController;
    
    public ViewAndReplyEnquiriesService(HDBOfficer officer, EnquiryController enquiryController) {
        this.officer = officer;
        this.enquiryController = enquiryController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View/Reply to Enquiries ===");
        if(officer.getAssignedProjects().isEmpty()){
            System.out.println("You are not approved for any projects.");
            return;
        }
        System.out.println("Your Approved Projects:");
        for(BTOProject proj : officer.getAssignedProjects()){
            System.out.println("Project Name: " + proj.getProjectName() + " (ID: " + proj.getProjectId() + ")");
        }
    }
    
    @Override
    public void handleInput() {
        int projId = Prompt.promptInt("Enter project ID to view its enquiries or '0' to go back: ");
        if (projId == 0) {
            exit();
            return;
        }
        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(projId);
        if(enquiries.isEmpty()){
            System.out.println("No enquiries found for this project.");
            String back = Prompt.prompt("Type 'b' to go back: ");
            if(back.equalsIgnoreCase("b")){
                exit();
            }
        }
        else {
            System.out.println("=== Enquiries for Project ID " + projId + " ===");
            for(Enquiry e : enquiries){
                System.out.println(e.toString());
            }
            int eid = Prompt.promptInt("Enter Enquiry ID to reply (or 0 to cancel): ");
            if(eid == 0) return;
            String reply = Prompt.prompt("Enter your reply: ");
            enquiryController.replyEnquiry(eid, reply);
            System.out.println("Reply submitted.");
            String back = Prompt.prompt("Type 'b' to go back: ");
            if(back.equalsIgnoreCase("b")){
                exit();
            }
        }
    }
}
