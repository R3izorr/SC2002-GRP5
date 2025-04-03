package system.service.manager;

import ui.AbstractMenu;
import ui.Prompt;
import java.util.List;
import model.Enquiry;
import model.BTOProject;
import model.HDBManager;
import controller.EnquiryController;

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
        System.out.println("Your Managed Projects:");
        for (BTOProject proj : manager.getManagedProjects()){
            System.out.println(proj.toStringForManagerOfficer());
        }
        System.out.println("Enter project ID to view its enquiries (or 'b' to go back): ");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
        if(input.equalsIgnoreCase("b")){
            exit();
            return;
        }
        int projId;
        try {
            projId = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(projId);
        if(enquiries.isEmpty()){
            System.out.println("No enquiries found for this project.");
        } else {
            for(Enquiry e : enquiries){
                System.out.println(e.toString());
            }
        }
        String eidStr = Prompt.prompt("Enter Enquiry ID to reply (or 'b' to go back): ");
        if(eidStr.equalsIgnoreCase("b")){
            exit();
            return;
        }
        int eid;
        try {
            eid = Integer.parseInt(eidStr);
        } catch(NumberFormatException e){
            System.out.println("Invalid input.");
            return;
        }
        String reply = Prompt.prompt("Enter your reply: ");
        enquiryController.editEnquiry(eid, reply);
        System.out.println("Reply submitted.");
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
