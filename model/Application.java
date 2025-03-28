package model;

import utils.ApplicationStatus;

public class Application {
    private Applicant applicant;
    private Project project;
    private ApplicationStatus status;

    public Application(Applicant applicant, Project project) {
        this.applicant = applicant;
        this.project = project;
        this.status = ApplicationStatus.PENDING;
    }

    public Applicant getApplicant() { return applicant; }
    public Project getProject() { return project; }
    public ApplicationStatus getStatus() { return status; }

    public void apply() {
        this.status = ApplicationStatus.PENDING;
    }

    public void withdraw() {
        this.status = ApplicationStatus.UNSUCCESSFUL;
    }

    public void updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }
}
