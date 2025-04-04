package system.service.manager;

import controller.EnquiryController;
import java.util.List;
import model.BTOProject;
import model.Enquiry;
import model.user.HDBManager;
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
        List<BTOProject> projects = manager.getManagedProjects();
        if(projects.isEmpty()){
            System.out.println("You have no projects to manage.");
        } else {
            System.out.println(manager.displayManagedProject());
        }
        System.out.println("Enter project ID to edit/delete or 'b' to go back:");
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
        enquiryController.replyEnquiry(eid, reply);
        System.out.println("Reply submitted.");
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
