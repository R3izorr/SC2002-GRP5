package system.service.officer;

import java.util.List;
import model.Application;
import model.BTOProject;
import model.user.HDBOfficer;
import repository.ApplicationRepository;
import ui.AbstractMenu;
import ui.Prompt;

public class ProcessFlatBookingService extends AbstractMenu {
    private HDBOfficer officer;
    private ApplicationRepository applicationRepository;
    
    public ProcessFlatBookingService(HDBOfficer officer, ApplicationRepository applicationRepository) {
        this.officer = officer;
        this.applicationRepository = applicationRepository;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Process Flat Booking ===");
        if(officer.getAssignedProjects().isEmpty()){
            System.out.println("You are not approved for any projects.");
        }
    }
    
    @Override
    public void handleInput() {
        List<Application> bookingApps = new java.util.ArrayList<>();
        for (BTOProject proj : officer.getAssignedProjects()) {
            for (Application app : applicationRepository.getApplications()) {
                if (app.getProject().getProjectId() == proj.getProjectId() &&
                    app.getStatus() == Application.Status.BOOKING) {
                    bookingApps.add(app);
                }
            }
        }
        if (bookingApps.isEmpty()) {
            System.out.println("No request for flat booking found.");
            String back = Prompt.prompt("Type 'b' to go back: ");
            if (back.equalsIgnoreCase("b")) {
            exit();
            }
        } else {
            System.out.println("=== Request Booking Flat for Your Assigned Projects ===");
            for (int i = 0; i < bookingApps.size(); i++) {
                Application app = bookingApps.get(i);
                System.out.println((i + 1) + ". Applicant NRIC: " + app.getApplicant().getNric() +
                    " | Applicant Name: " + app.getApplicant().getName() +
                    " | Project: " + app.getProject().getProjectName() +
                    " | Flat Type Requested: " + app.getFlatType());
            }
            int choice = Prompt.promptInt("Enter the number of the application to process booking (or 0 to stop): ");
            if (choice == 0) {
                System.out.println("Stopping booking process.");
            }
            if (choice < 1 || choice > bookingApps.size()) {
                System.out.println("Invalid selection.");
            }
            Application targetApp = bookingApps.get(choice - 1);
            String flatType = targetApp.getFlatType();
            BTOProject proj = targetApp.getProject();
            if (flatType.equalsIgnoreCase("2-Room")) {
                if (proj.getUnits2Room() <= 0) {
                System.out.println("No 2-Room units available.");
                }
                proj.setUnits2Room(proj.getUnits2Room() - 1);
            } else if (flatType.equalsIgnoreCase("3-Room")) {
                if (proj.getUnits3Room() <= 0) {
                System.out.println("No 3-Room units available.");
                }
                proj.setUnits3Room(proj.getUnits3Room() - 1);
            } else {
                System.out.println("Invalid flat type in application.");
            }
            targetApp.setStatus(Application.Status.BOOKED);
            System.out.println("Flat booking processed for applicant " 
                + targetApp.getApplicant().getNric() + ". Application status updated to BOOKED.");
                applicationRepository.saveApplications();
            }
            applicationRepository.saveApplications();
            String back = Prompt.prompt("Type 'b' to go back: ");
            if (back.equalsIgnoreCase("b")) {
            exit();
        }
    }
}

