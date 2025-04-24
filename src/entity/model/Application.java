package entity.model;

import entity.user.Applicant;

public class Application { 
    private Applicant applicant;
    private BTOProject project;
    private ApplicationStatus status; // Default status is PENDING
    private String flatType; // e.g., "2-Room" or "3-Room"

    public Application(Applicant applicant, BTOProject project, String flatType, ApplicationStatus status) {
        this.applicant = applicant;
        this.project = project;
        this.status = status;
        this.flatType = flatType;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public BTOProject getProject() {
        return project;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public Float getFlatTypePrice() {
        if (flatType.equals("2-Room")) {
            return project.getSellingPrice2Room();
        }
        else return project.getSellingPrice3Room();
    }

    public String getApplicationDetail() {
        return 
               String.format("%-15s: %s%n", "Project ID", project.getProjectId()) +
               String.format("%-15s: %s%n", "Project Name", project.getProjectName()) +
               String.format("%-15s: %s%n", "Flat Type", flatType) +
                String.format("%-15s: %s%n", "Flat Type Price", getFlatTypePrice()) +
               String.format("%-15s: %s%n", "Status", status) +
               "---------------------------------\n";
    }
}