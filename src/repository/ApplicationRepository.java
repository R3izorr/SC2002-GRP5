package repository;

import model.Application; 
import model.Applicant;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository {
    private List<Application> applications;
    public ApplicationRepository() {
        applications = new ArrayList<>();
    }
    
    public void addApplication(Application application) {
        applications.add(application);
    }
    
    public List<Application> getApplications() {
        return applications;
    }
    
    public Application getApplicationByApplicant(Applicant applicant) {
        for (Application app : applications) {
            if (app.getApplicant().getNric().equals(applicant.getNric())) {
                return app;
            }
        }
        return null;
    }
}
