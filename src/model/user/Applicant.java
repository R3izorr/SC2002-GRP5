package model.user;

import java.util.Date;
import model.Application;
import model.BTOProject;

public class Applicant extends User {
    private Application application;

    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.application = null;
    }

    public boolean CheckEligiblity(Applicant currentApplicant, BTOProject project, String flatType) {
        String applicantMaritalStatus = currentApplicant.getMaritalStatus();
        int age = currentApplicant.getAge();
        Date today = new Date();
        if (today.before(project.getApplicationOpen()) || today.after(project.getApplicationClose())) {
            System.out.println("The project is not open for application.");
            return false;
        }
        if(applicantMaritalStatus.equalsIgnoreCase("Single")) {
            if(age < 35) {
                System.out.println("You are not eligible to apply as a single applicant (must be 35 or above).");
                return false;
            }
            if(!flatType.equalsIgnoreCase("2-Room")) {
                System.out.println("As a single applicant, you can only apply for a 2-Room flat.");
                return false;
            }
        } else if(applicantMaritalStatus.equalsIgnoreCase("Married")) {
            if(age < 21) {
                System.out.println("You are not eligible to apply as a married applicant (must be 21 or above).");
                return false;
            }
            if(!(flatType.equalsIgnoreCase("2-Room") || flatType.equalsIgnoreCase("3-Room"))) {
                System.out.println("Invalid flat type. Please choose either '2-Room' or '3-Room'.");
                return false;
            }
        }
        return true;
    }


    public Application getApplication() {
        return application;
    }
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    @Override
    public String getRole() {
        return "Applicant";
    }
}
