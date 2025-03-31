package controller;

import java.util.List;
import model.Applicant;
import model.Application;
import model.BTOProject;
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
    
    // Apply for a project with a chosen flat type.
    public boolean applyForProject(int projectIndex, String flatType) {
        List<BTOProject> projects = getVisibleProjects();
        if(projectIndex < 1 || projectIndex > projects.size())
            return false;
        BTOProject project = projects.get(projectIndex - 1);
        // If an application exists and its status is not UNSUCCESSFUL or WITHDRAWN, do not allow re-application.
        if(currentApplicant.getApplication() != null) {
            Application.Status status = currentApplicant.getApplication().getStatus();
            if(status != Application.Status.UNSUCCESSFUL && status != Application.Status.WITHDRAWN) {
                System.out.println("You have already applied for a project.");
                return false;
            }
        }
        // Enforce eligibility rules.
        String maritalStatus = currentApplicant.getMaritalStatus();
        int age = currentApplicant.getAge();
        if(maritalStatus.equalsIgnoreCase("Single")) {
            if(age < 35) {
                System.out.println("You are not eligible to apply as a single applicant (must be 35 or above).");
                return false;
            }
            if(!flatType.equalsIgnoreCase("2-Room")) {
                System.out.println("As a single applicant, you can only apply for a 2-Room flat.");
                return false;
            }
        } else if(maritalStatus.equalsIgnoreCase("Married")) {
            if(age < 21) {
                System.out.println("You are not eligible to apply as a married applicant (must be 21 or above).");
                return false;
            }
            if(!(flatType.equalsIgnoreCase("2-Room") || flatType.equalsIgnoreCase("3-Room"))) {
                System.out.println("Invalid flat type. Please choose either '2-Room' or '3-Room'.");
                return false;
            }
        }
        Application application = new Application(currentApplicant, project, flatType);
        currentApplicant.setApplication(application);
        applicationRepository.addApplication(application);
        System.out.println("Application submitted. Status: PENDING");
        return true;
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
        if(app.getStatus() == Application.Status.BOOKED) {
            System.out.println("Cannot withdraw after flat booking.");
            return false;
        }
        app.setStatus(Application.Status.WITHDRAWN);
        currentApplicant.setApplication(null); // Clear current application record.
        System.out.println("Application withdrawn.");
        return true;
    }
    
    // Request flat booking if the application status is SUCCESSFUL.
    public boolean requestBooking() {
        Application app = currentApplicant.getApplication();
        if(app == null) {
            System.out.println("No application found.");
            return false;
        }
        if(app.getStatus() != Application.Status.SUCCESSFUL) {
            System.out.println("Your application is not in a state to book a flat.");
            return false;
        }
        BTOProject project = app.getProject();
        String flatType = app.getFlatType();
        if(flatType.equalsIgnoreCase("2-Room")) {
            if(project.getUnits2Room() <= 0) {
                System.out.println("No 2-Room flats available.");
                return false;
            }
            project.setUnits2Room(project.getUnits2Room() - 1);
        } else if(flatType.equalsIgnoreCase("3-Room")) {
            if(project.getUnits3Room() <= 0) {
                System.out.println("No 3-Room flats available.");
                return false;
            }
            project.setUnits3Room(project.getUnits3Room() - 1);
        }
        app.setStatus(Application.Status.BOOKED);
        System.out.println("Flat booking completed. Your application status is now BOOKED.");
        return true;
    }
}
