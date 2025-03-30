package model;

import utils.MaritalStatus;
import utils.Role;

public class Applicant extends User {

    public Applicant(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.APPLICANT);
    }

    // Method stubs for now — logic comes in Step 2
    public void viewEligibleProjects() {}
    public void applyForProject(Project project) {}
    public void viewApplicationStatus() {}
    public void withdrawApplication() {}
    public void submitEnquiry(String content) {}
    public void editEnquiry(Enquiry enquiry, String newContent) {}
    public void deleteEnquiry(Enquiry enquiry) {}
}
