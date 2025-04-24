package system.service.officer;

import controller.EnquiryController;
import entity.model.BTOProject;
import entity.model.Enquiry;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
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
        }
        else {
            List<Enquiry> enquiries = new ArrayList<>();
            for (BTOProject proj : officer.getAssignedProjects()) {
                enquiries.addAll(
                    enquiryController.getEnquiriesForProject(proj.getProjectId())
                );
            }
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries found for any of your projects.");
            }
            else {
                for (Enquiry e : enquiries) {
                    System.out.println(e);           // relies on Enquiry.toString()
                }
            }
        }
    }
    
    @Override
    public void handleInput() {
        while (true) {
        int eid = Prompt.promptInt("Enter Enquiry ID or '0' to go back: ");
        if (eid == 0) {
            exit();
            return;
        }
        String reply = Prompt.prompt("Enter your reply: ");
        enquiryController.replyEnquiry(eid, reply);
        
        List<Enquiry> enquiries = new ArrayList<>();
        for (BTOProject proj : officer.getAssignedProjects()) {
            enquiries.addAll(enquiryController.getEnquiriesForProject(proj.getProjectId()));
            }
        if (!enquiries.isEmpty()) {
            for (Enquiry e : enquiries) {
                System.out.println(e);           // relies on Enquiry.toString()
                }
            }
        }
    }
}
