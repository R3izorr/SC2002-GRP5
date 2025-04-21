package controller;

import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.user.Applicant;
import java.util.ArrayList;
import java.util.List;
import repository.ICRUDRepository;


public class ApplicationController {
    private ICRUDRepository<Application> applicationRepository;
    private Applicant currentApplicant;

    // Constructor for applicant and officer (currentApplicant is set)
    public ApplicationController(ICRUDRepository<Application> applicationRepository, Applicant currentApplicant) {
        this.currentApplicant = currentApplicant;
        this.applicationRepository = applicationRepository;
    }

    // Constructor for manager (no current applicant needed)
    public ApplicationController(ICRUDRepository<Application> applicationRepository) {
        this.currentApplicant = null; // No current applicant in this mode
        this.applicationRepository = applicationRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.getAll(); // Return all applications from the repository
    }

    public List<Application> getApplicationByProjectId(int projectId) {
        List<Application> filteredApplications = new ArrayList<>();
        for (Application app : applicationRepository.getAll()) {
            if (app.getProject().getProjectId() == projectId) {
                filteredApplications.add(app);
            }
        }
        return filteredApplications;
    }

    public void updateApplication() {
        applicationRepository.update(); // Save the updated application to the repository
    }

    public boolean withdrawApplication() {
        Application app = currentApplicant.getApplication();
        if(app == null) {
            System.out.println("No application to withdraw.");
            return false;
        }
        if(app.getStatus() == ApplicationStatus.BOOKED) {
            System.out.println("Cannot withdraw after having booked a flat.");
            return false;
        }
        app.setStatus(ApplicationStatus.WITHDRAW_REQUESTED);
        applicationRepository.update();
        return true;
    }

    public boolean requestBooking() {
        Application app = currentApplicant.getApplication();
        if(app == null) {
            System.out.println("No application found.");
            return false;
        }
        if(app.getStatus() != ApplicationStatus.SUCCESSFUL) {
            System.out.println("Your application is not in a state that requires booking a flat.");
            return false;
        }
        app.setStatus(ApplicationStatus.BOOKING);
        applicationRepository.update();
        System.out.println("Require for Booking is completed. Your application status is now BOOKING.");
        return true;
    }


}
