package model;

public class Receipt {
    private String applicantNric;
    private String applicantName;
    private int age;
    private String maritalStatus;
    private String flatType;
    private int projectId;
    private String projectName;
    
    // Construct a Receipt from a booked Application.
    public Receipt(Application app) {
        this.applicantName = app.getApplicant().getName();
        // Assuming the Applicant has a name field; if not, using NRIC as placeholder.
        // For this example, we use getNric() as the name.
        this.applicantNric = app.getApplicant().getNric();
        this.age = app.getApplicant().getAge();
        this.maritalStatus = app.getApplicant().getMaritalStatus();
        this.flatType = app.getFlatType();
        this.projectId = app.getProject().getProjectId();
        this.projectName = app.getProject().getProjectName();
    }
    
    public String getApplicantNric() {
        return applicantNric;
    }
    
    public String getApplicantName() {
        return applicantName;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getMaritalStatus() {
        return maritalStatus;
    }
    
    public String getFlatType() {
        return flatType;
    }
    
    public int getProjectId() {
        return projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    @Override
    public String toString() {
        return "========================================\n" +
               "              RECEIPT                   \n" +
               "----------------------------------------\n" +
               "Applicant NRIC   : " + applicantNric + "\n" +
               "Applicant Name   : " + applicantName + "\n" +
               "Age              : " + age + "\n" +
               "Marital Status   : " + maritalStatus + "\n" +
               "Flat Type Booked : " + flatType + "\n" +
               "Project ID       : " + projectId + "\n" +
               "Project Name     : " + projectName + "\n" +
               "========================================\n";
    }
}