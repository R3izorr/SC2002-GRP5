package model;

public class Application { 
    public enum Status { 
        PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKING, BOOKED, WITHDRAWN
    }
    private Applicant applicant;
    private BTOProject project;
    private Status status;
    private String flatType; // e.g., "2-Room" or "3-Room"

    public Application(Applicant applicant, BTOProject project, String flatType) {
        this.applicant = applicant;
        this.project = project;
        this.status = Status.PENDING;
        this.flatType = flatType;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public BTOProject getProject() {
        return project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
}