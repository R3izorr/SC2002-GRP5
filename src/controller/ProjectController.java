package controller;

import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.user.Applicant;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import repository.ICRUDRepository;

public class ProjectController {
    private ICRUDRepository<BTOProject> projectRepository;
    private ICRUDRepository<Application> applicationRepository;
    private Applicant currentApplicant;

    // Constructor for ProjectController
    public ProjectController(ICRUDRepository<BTOProject> projectRepository, ICRUDRepository<Application> applicationRepository, Applicant currentApplicant) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.currentApplicant = currentApplicant;
    }
    
    public ProjectController(ICRUDRepository<BTOProject> projectRepository, ICRUDRepository<Application> applicationRepository) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.currentApplicant = null;
    }
    
    public List<BTOProject> getVisibleProjects() {
        return projectRepository.getAll().stream()
                .filter(BTOProject::isVisible)
                .toList();
    }

    public List<BTOProject> getAvailaBTOProjects() {
        int age = currentApplicant.getAge();
        boolean isSingle = currentApplicant.getMaritalStatus().equalsIgnoreCase("Single");
        // Check if the applicant is eligible based on age and marital status
        if ((isSingle && age < 35) || (!isSingle && age < 21)) {
            return new ArrayList<BTOProject>();
        }

        // Get all projects that are visible and within the application open/close dates.
        List<BTOProject> availableProjects = new ArrayList<BTOProject>(projectRepository.getAll().stream()
        .filter(BTOProject::isVisible)
        .toList());
        availableProjects.removeIf(project -> project.getApplicationOpen().after(new Date()) || project.getApplicationClose().before(new Date()));

        return availableProjects;
    }
    
    public BTOProject getProjectById(int projectId) {
        return projectRepository.getById(projectId);
    }

    public List<BTOProject> getAllProjects() {
        return projectRepository.getAll();
    }

    // Apply for a project with a chosen flat type.
    public boolean applyForProject(int projectId, String flatType) {
        if(currentApplicant.getApplication() != null && 
           currentApplicant.getApplication().getStatus() != ApplicationStatus.UNSUCCESSFUL && 
           currentApplicant.getApplication().getStatus() != ApplicationStatus.WITHDRAWN
          ) {
            System.out.println("You have already applied for a Project. Cannot apply for multiple projects!");
            return false;
        }
        
        // Check if the project ID is valid and if the project is visible.
        BTOProject project = projectRepository.getById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return false;
        }
        if(project.isVisible() == false) {
            System.out.println("Project is not visible.");
            return false;
        }
        // If an application exists and its status is not UNSUCCESSFUL or WITHDRAWN, do not allow re-application.
        // Enforce eligibility rules.
        if(currentApplicant.CheckEligiblity(project, flatType)) {
            Application application = new Application(currentApplicant, project, flatType, ApplicationStatus.PENDING);
            applicationRepository.remove(currentApplicant.getApplication());
            currentApplicant.setApplication(application);
            applicationRepository.add(application);
            applicationRepository.update();
            return true;
        }
        return false;
    }
    
    // Overloaded method to allow HDBOfficer to apply for a project.
    public boolean applyForProject(int projectId, String flatType, HDBOfficer officer) {
        // Check if the project ID is valid and if the project is visible.
        BTOProject project = projectRepository.getById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return false;
        }
        if(project.isVisible() == false) {
            System.out.println("Project is not visible.");
            return false;
        }
        
        // Check if officer already has an application
        if(officer.getApplication() != null && 
            officer.getApplication().getStatus() == ApplicationStatus.UNSUCCESSFUL &&
            officer.getApplication().getStatus() == ApplicationStatus.WITHDRAWN
         ) {
            System.out.println("You have already applied for a project.");
            return false;
        }
       
        // Check if officer is handling the project or registering for it
        for(BTOProject proj: officer.getAssignedProjects()) {
            if(proj.getProjectId() == projectId) {
                System.out.println("Cannot apply as Applicant for a project that you are handling.");
                return false;
            }
        }
        for(BTOProject proj: officer.getPendingRegistrations()) {
            if(proj.getProjectId() == projectId) {
                System.out.println("Cannot apply as Applicant for a project that you are registering for.");
                return false;
            }
        }
       
        // Eligibility rules for officer as applicant (you may adjust as needed)
        if(currentApplicant.CheckEligiblity(project, flatType)) {
            Application application = new Application(currentApplicant, project, flatType, ApplicationStatus.PENDING);
            applicationRepository.remove(currentApplicant.getApplication());
            currentApplicant.setApplication(application);
            applicationRepository.add(application);
            applicationRepository.update();
            return true;
        }
        return false;
    }

    public void createProject(BTOProject project) {
        projectRepository.add(project);
        projectRepository.update();
    }

    public void addProject(BTOProject project) {
        projectRepository.add(project);
    }

    public void removeProject(BTOProject project) {
        projectRepository.remove(project);
    }

    public void updateProject() {
        projectRepository.update();
    }

}
