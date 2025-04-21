package system.service.common;

import entity.model.Application;
import entity.user.Applicant;
import ui.AbstractMenu;
import ui.Prompt;

public class ViewApplicationStatusService extends AbstractMenu {
    private Applicant applicant;
    
    public ViewApplicationStatusService (Applicant applicant) {
        this.applicant = applicant;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== View Application Status ===");
    }
    
    @Override
    public void handleInput() {
        Application app = applicant.getApplication();
        if(app != null){
            System.out.println(app.getApplicationDetail());
        } else {
            System.out.println("No application found.");
        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
