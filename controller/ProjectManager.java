package controller;

import model.Project;
import model.HDBManager;
import model.HDBOfficer;
import utils.FlatType;
import utils.ProjectStorage;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectManager {
    private List<Project> projects;

    // Constructor: Load projects from CSV file
    public ProjectManager() {
        projects = ProjectStorage.loadProjects(Paths.get("data", "projects.csv").toString());
    }

    // Create and store a new project
    public Project createProject(String name, String location, LocalDate openDate, LocalDate closeDate,
                                 HDBManager manager, int officerSlots, Map<FlatType, Integer> unitMap) 
    {
        for (Project existing : projects) {
            if (existing.getManager() != null && existing.getManager().equals(manager)) {
                boolean overlap =
                    !(closeDate.isBefore(existing.getApplicationOpenDate()) ||
                      openDate.isAfter(existing.getApplicationCloseDate()));
        
                if (overlap) {
                    System.out.println("You already have a project in this application period.");
                    return null;
                }
            }
        }        
        Project project = new Project(name, location, unitMap, openDate, closeDate);
        project.setAvailableOfficerSlots(officerSlots);
        project.setManager(manager);
        projects.add(project);
        ProjectStorage.saveProjects(projects, Paths.get("data", "projects.csv").toString());
        System.out.println("Saved " + projects.size() + " projects to CSV.");
        
        return project;
    }

    // Edit flat units and officer slots
    public void editProject(Project project, Map<FlatType, Integer> updatedUnits, int officerSlots) {
        for (FlatType type : updatedUnits.keySet()) {
            project.setUnitCount(type, updatedUnits.get(type));
        }
        project.setAvailableOfficerSlots(officerSlots);
        ProjectStorage.saveProjects(projects, "data/projects.csv");
    }

    // Delete project
    public void deleteProject(Project project) {
        projects.remove(project);
        ProjectStorage.saveProjects(projects, "data/projects.csv");
    }

    // Toggle visibility (wrapper)
    public void toggleProjectVisibility(Project project) {
        project.setVisible(!project.isVisible());
        ProjectStorage.saveProjects(projects, "data/projects.csv");
    }

    // Filter for only visible projects
    public List<Project> filterVisibleProjects() {
        List<Project> visible = new ArrayList<>();
        for (Project p : projects) {
            if (p.isVisible()) visible.add(p);
        }
        return visible;
    }

    // Return all projects
    public List<Project> getAllProjects() {
        return projects;
    }

    // Register officer to a project (adds to pending list)
    public void registerOfficer(HDBOfficer officer, Project project) {
        project.addPendingOfficer(officer); // assuming this is implemented
    }

    // Manager approves officer registration (assigns one officer)
    public void approveOfficerRegistration(HDBOfficer officer, Project project) {
        if (project.getAvailableOfficerSlots() > 0) {
            project.setAvailableOfficerSlots(project.getAvailableOfficerSlots() - 1);
            System.out.println("Officer " + officer.getNric() + " registered for project " + project.getName());
            ProjectStorage.saveProjects(projects, "data/projects.csv");
        } else {
            System.out.println("No officer slots available in this project.");
        }
    }
}
