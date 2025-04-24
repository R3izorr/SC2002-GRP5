package system.service.common;

import controller.ApplicationController;
import controller.NotificationController;
import entity.model.BTOProject;
import entity.user.HDBOfficer;
import ui.AbstractMenu;
import ui.Prompt;

public class RequestFlatBookingService extends AbstractMenu {
    private ApplicationController applicationController;
    private NotificationController notificationController;

    public RequestFlatBookingService(ApplicationController applicationController, NotificationController notificationController) {
        this.applicationController = applicationController;
        this.notificationController = notificationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Request Flat Booking ===");
    }
    
    @Override
    public void handleInput() {
        boolean success = applicationController.requestBooking();
        if(success){
            System.out.println("Flat booking requested successfully.");
            BTOProject project = applicationController.getApplication().getProject();
            for (HDBOfficer officer : project.getOfficers()) {
                String message = "Flat booking request by %s for project %s (ID: %d)".formatted(
                        applicationController.getApplication().getApplicant().getName(),
                        project.getProjectName(),
                        project.getProjectId()
                );
                notificationController.send(officer.getNric(), message);
            }

        }
        System.out.println("Type 'b' to go back.");
        String input = Prompt.prompt("");
        if(input.equalsIgnoreCase("b")){
            exit();
        }
    }
}
