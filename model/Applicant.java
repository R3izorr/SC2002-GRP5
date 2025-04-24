package model;

import utils.ApplicationStatus;
import utils.FlatType;
import utils.MaritalStatus;
import utils.Role;
import model.Project;
import utils.FlatType;
import utils.ApplicationStatus;


public class Applicant extends User {

    public Applicant(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.APPLICANT);
    }

    private Project appliedProject;
    private FlatType appliedFlatType;
    private ApplicationStatus applicationStatus;



    // Method stubs for now — logic comes in Step 2
    public void viewEligibleProjects() {}
    public void applyForProject(Project project) {}
    public void viewApplicationStatus() {}
    public void submitEnquiry(String content) {}
    public void editEnquiry(Enquiry enquiry, String newContent) {}
    public void deleteEnquiry(Enquiry enquiry) {}
    public Project getAppliedProject() {
        return appliedProject;
    }
    
    public FlatType getAppliedFlatType() {
        return appliedFlatType;
    }
    
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    public void setApplicationStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }
        

    public void withdrawApplication() {
        if (this.appliedProject == null) {
            System.out.println("No application to withdraw.");
            return;
        }
    
        if (this.applicationStatus == ApplicationStatus.WITHDRAWAL_REQUESTED) {
            System.out.println("You have already requested withdrawal.");
            return;
        }
    
        this.applicationStatus = ApplicationStatus.WITHDRAWAL_REQUESTED;
        System.out.println("Withdrawal request submitted. Awaiting manager approval.");
    }
    
    public void clearApplication() {
        this.appliedProject = null;
        this.appliedFlatType = null;
        this.applicationStatus = null;
    }
    
    
    
}
