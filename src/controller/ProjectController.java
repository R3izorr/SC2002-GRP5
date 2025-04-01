package controller;

import java.util.List;
import model.Applicant;
import model.Application;
import model.BTOProject;
import model.HDBOfficer;
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
            System.out.println("Your application is not in a state that requires booking a flat.");
            return false;
        }
        app.setStatus(Application.Status.BOOKING);
        System.out.println("Require for Booking is completed. Your application status is now BOOKING.");
        return true;
    }

     public boolean applyForProjectByUser(int projectId, String flatType, HDBOfficer officer) {
        BTOProject project = projectRepository.getProjectById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return false;
        }
        // Check if officer already has an application
        if(officer.getApplication() != null) {
            System.out.println("You have already applied for a project.");
            return false;
        }
        // Eligibility rules for officer as applicant (you may adjust as needed)
        String maritalStatus = officer.getMaritalStatus();
        int age = officer.getAge();
        if(maritalStatus.equalsIgnoreCase("Single")) {
            if(age < 35) {
                System.out.println("Single applicants must be 35 or older.");
                return false;
            }
            if(!flatType.equalsIgnoreCase("2-Room")) {
                System.out.println("Single applicants can only apply for 2-Room flats.");
                return false;
            }
        } else if(maritalStatus.equalsIgnoreCase("Married")) {
            if(age < 21) {
                System.out.println("Married applicants must be 21 or older.");
                return false;
            }
            if(!(flatType.equalsIgnoreCase("2-Room") || flatType.equalsIgnoreCase("3-Room"))) {
                System.out.println("Married applicants can only apply for 2-Room or 3-Room flats.");
                return false;
            }
        }
        Application application = new Application(officer, project, flatType);
        officer.setApplication(application);
        applicationRepository.addApplication(application);
        System.out.println("Application submitted with status PENDING.");
        return true;
    }
}
