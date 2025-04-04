package model;

public class Receipt {
    private String applicantNric;
    private String applicantName;
    private int age;
    private String maritalStatus;
    private String flatType;
    private int projectId;
    private String projectName;
    private Float flatTypePrice;

    // Construct a Receipt from a booked Application.
    public Receipt(Application app) {
        this.applicantName = app.getApplicant().getName();
        this.applicantNric = app.getApplicant().getNric();
        this.age = app.getApplicant().getAge();
        this.maritalStatus = app.getApplicant().getMaritalStatus();
        this.flatType = app.getFlatType();
        this.projectId = app.getProject().getProjectId();
        this.projectName = app.getProject().getProjectName();
        this.flatTypePrice = app.getFlatTypePrice();
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
               "========================================\n" +
               "| Applicant NRIC   : " + applicantNric + "\n" +
               "| Applicant Name   : " + applicantName + "\n" +
               "| Age              : " + age +           "\n" +  
               "| Marital Status   : " + maritalStatus + "\n" +
               "| Flat Type Booked : " + flatType +       "\n" +
               "| Price            : " + flatTypePrice +  "\n" +	
               "| Project ID       : " + projectId +    "\n" +
               "| Project Name     : " + projectName +  "\n" +
               "========================================\n";
    }
}
