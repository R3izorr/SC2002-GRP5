package model;

import utils.MaritalStatus;

public class Receipt {
    private String applicantName;
    private String nric;
    private int age;
    private MaritalStatus maritalStatus;
    private String flatType;
    private Project project;

    public Receipt(Applicant applicant, String flatType, Project project) {
        this.applicantName = applicant.getNric(); 
        this.nric = applicant.getNric();
        this.age = applicant.getAge();
        this.maritalStatus = applicant.getMaritalStatus();
        this.flatType = flatType;
        this.project = project;
    }

    public String generate() {
        return String.format(
            "===== RECEIPT =====\n" +
            "Name (NRIC): %s\n" +
            "Age: %d\n" +
            "Marital Status: %s\n" +
            "Flat Type: %s\n" +
            "Project: %s\n" +
            "===================",
            applicantName, age, maritalStatus, flatType, project.getName()
        );
        
    }
}
