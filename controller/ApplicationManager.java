package controller;

import model.Applicant;
import model.Application;
import model.Project;
import utils.ApplicationStatus;

import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {
    private List<Application> applications;

    public ApplicationManager() {
        this.applications = new ArrayList<>();
    }

    public Application submitApplication(Applicant applicant, Project project) {
        Application app = new Application(applicant, project);
        applications.add(app);
        return app;
    }

    public void withdrawApplication(Application app) {
        app.withdraw();
    }

    public void updateApplicationStatus(Application app, ApplicationStatus status) {
        app.updateStatus(status);
    }

    public List<Application> getAllApplications() {
        return applications;
    }

    public List<Application> getApplicationsByApplicant(Applicant applicant) {
        List<Application> result = new ArrayList<>();
        for (Application app : applications) {
            if (app.getApplicant().equals(applicant)) {
                result.add(app);
            }
        }
        return result;
    }

    public List<Application> getApplicationsByProject(Project project) {
        List<Application> result = new ArrayList<>();
        for (Application app : applications) {
            if (app.getProject().equals(project)) {
                result.add(app);
            }
        }
        return result;
    }
    public List<Application> getWithdrawalRequests() {
        return applications.stream()
            .filter(app -> app.getStatus() == ApplicationStatus.WITHDRAWAL_REQUESTED)
            .toList();
    }
    
    
}
