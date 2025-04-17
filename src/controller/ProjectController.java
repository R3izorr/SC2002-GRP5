package controller;

import java.util.List;
import model.Application;
import model.ApplicationStatus;
import model.BTOProject;
import model.user.Applicant;
import model.user.HDBOfficer;
import repository.ApplicationRepository;
import repository.ProjectRepository;

public class ProjectController {
    private ProjectRepository projectRepository; 
    private ApplicationRepository applicationRepository; 
    private Applicant currentApplicant;
    public ProjectController(ProjectRepository projectRepository, ApplicationRepository applicationRepository, Applicant applicant) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.currentApplicant = applicant;
    }
    
    // Returns only projects that are visible.
    public List<BTOProject> getVisibleProjects() {
        return projectRepository.getProjects().stream()
                .filter(BTOProject::isVisible)
                .toList();
    }
    
    
    public BTOProject getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    // Apply for a project with a chosen flat type.
    public boolean applyForProject(int projectId, String flatType) {
        if(currentApplicant.getApplication() != null && 
           currentApplicant.getApplication().getStatus() != ApplicationStatus.UNSUCCESSFUL && 
           currentApplicant.getApplication().getStatus() != ApplicationStatus.WITHDRAWN
          ) {
            System.out.println("You have already applied for a Project. Cannot apply for multiple projects!");
            return false;
        }
        
        // Check if the project ID is valid and if the project is visible.
        BTOProject project = projectRepository.getProjectById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return false;
        }
        if(project.isVisible() == false) {
            System.out.println("Project is not visible.");
            return false;
        }
        // If an application exists and its status is not UNSUCCESSFUL or WITHDRAWN, do not allow re-application.
        // Enforce eligibility rules.
        if(currentApplicant.CheckEligiblity(project, flatType)) {
            Application application = new Application(currentApplicant, project, flatType, ApplicationStatus.PENDING);
            applicationRepository.removeApplication(currentApplicant.getApplication());
            currentApplicant.setApplication(application);
            applicationRepository.addApplication(application);
            applicationRepository.saveApplications();
            return true;
        }
        return false;
    }
    
    // Overloaded method to allow HDBOfficer to apply for a project.
    public boolean applyForProject(int projectId, String flatType, HDBOfficer officer) {
        // Check if the project ID is valid and if the project is visible.
        BTOProject project = projectRepository.getProjectById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return false;
        }
        if(project.isVisible() == false) {
            System.out.println("Project is not visible.");
            return false;
        }
        // Check if officer already has an application
        if(officer.getApplication() != null && 
            officer.getApplication().getStatus() == ApplicationStatus.UNSUCCESSFUL &&
            officer.getApplication().getStatus() == ApplicationStatus.WITHDRAWN
         ) {
            System.out.println("You have already applied for a project.");
            return false;
        }
        for(BTOProject proj: officer.getAssignedProjects()) {
            if(proj.getProjectId() == projectId) {
                System.out.println("Cannot apply as Applicant for a project that you are handling.");
                return false;
            }
        }
        for(BTOProject proj: officer.getPendingRegistrations()) {
            if(proj.getProjectId() == projectId) {
                System.out.println("Cannot apply as Applicant for a project that you are registering for.");
                return false;
            }
        }
        // Eligibility rules for officer as applicant (you may adjust as needed)
        if(currentApplicant.CheckEligiblity(project, flatType)) {
            Application application = new Application(currentApplicant, project, flatType, ApplicationStatus.PENDING);
            applicationRepository.removeApplication(currentApplicant.getApplication());
            currentApplicant.setApplication(application);
            applicationRepository.addApplication(application);
            applicationRepository.saveApplications();
            return true;
        }
        return false;
    }

    // Retrieve the applicant's current application.
    public Application getApplication() {
        return currentApplicant.getApplication();
    }
    
    // Allow withdrawal if not yet BOOKED.
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
        applicationRepository.saveApplications();
        return true;
    }
    
    // Request flat booking if the application status is SUCCESSFUL.
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
        applicationRepository.saveApplications();
        System.out.println("Require for Booking is completed. Your application status is now BOOKING.");
        return true;
    }

}
