package repository.modelrepository;

import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.user.Applicant;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
import repository.ICRUDRepository;
import utils.FileUtils;

public class ApplicationRepository implements ICRUDRepository<Application> {
    private String applicationFilePath;
    private List<Application> applications;
    private List<Applicant> applicants;
    private List<HDBOfficer> officers;
    private List<BTOProject> projects;
    
    public ApplicationRepository(String applicationFilePath, List<Applicant> applicants, List<HDBOfficer> officers, List<BTOProject> projects) {
        this.applicationFilePath = applicationFilePath;
        this.applicants = applicants;
        this.officers = officers;
        this.projects = projects;
        applications = new ArrayList<>();
    }
    
    @Override
    public void add(Application application) {
        applications.add(application);
    }
    
    @Override
    public List<Application> getAll() {
        return applications;
    }
    
    @Override
    public Application getById(Object Nric) {
        for (Application app : applications) {
            if (app.getApplicant().getNric().equals(String.valueOf(Nric))) {
                return app;
            }
        }
        return null;
    }

    @Override
    public void load() {
        // Load applications from a data source (e.g., database, file)
        // This is a placeholder for the actual implementation
        List<String[]> lines = FileUtils.readCSV(this.applicationFilePath);
        for (String[] tokens : lines) {
            if (tokens.length < 6) {
                continue; // Skip this line if it doesn't have enough tokens
            }
            String Name = tokens[0];
            String NRIC = tokens[1];
            String projectId = tokens[2]; 
            String projectName = tokens[3];
            String flatType = tokens[4];
            ApplicationStatus status = ApplicationStatus.valueOf(tokens[5].toUpperCase());

            // Find the applicant by NRIC
            Applicant applicant = null;
            for (Applicant a : applicants) {
                if (a.getNric().equals(NRIC)) {
                    applicant = a;
                    break;
                }
            }
            for (HDBOfficer o : officers) {
                if (o.getNric().equals(NRIC)) {
                    applicant = o;
                    break;
                }
            }

            //Find the project by projectId
            BTOProject project = null;
            for (BTOProject p : projects) {
                if (p.getProjectId() == Integer.parseInt(projectId)) {
                    project = p;
                    break;
                }
            }
            if (applicant != null) {
                Application application = new Application(applicant, project, flatType, status);
                applicant.setApplication(application);
                applications.add(application);
            }
        }
    }

    @Override
    public void update() {
        List<String[]> data = new ArrayList<>();
        // Add a header
        data.add(new String[]{"Applicant Name", "NRIC", "Project ID", "Project Name", "Flat Type", "Status"});
        for (Application application : applications) {
            String[] line = new String[6];
            line[0] = application.getApplicant().getName();
            line[1] = application.getApplicant().getNric();
            line[2] = String.valueOf(application.getProject().getProjectId());
            line[3] = application.getProject().getProjectName();
            line[4] = application.getFlatType();
            line[5] = application.getStatus().toString();
            data.add(line);
        }
        FileUtils.writeCSV(this.applicationFilePath, data);
    }

    @Override
    public void remove(Application application) {
        applications.remove(application);
    }

}
