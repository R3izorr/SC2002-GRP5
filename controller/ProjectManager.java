package controller;

import model.Project;
import model.HDBManager;
import model.HDBOfficer;
import utils.FlatType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectManager {
    private List<Project> projects;

    public ProjectManager() {
        this.projects = new ArrayList<>();
    }

    public Project createProject(String name, String location, LocalDate openDate, LocalDate closeDate,
                                 HDBManager manager, int officerSlots, Map<FlatType, Integer> unitMap) {
        Project project = new Project(name, location, openDate, closeDate, manager, officerSlots);
        for (FlatType type : unitMap.keySet()) {
            project.setUnitCount(type, unitMap.get(type));
        }
        projects.add(project);
        return project;
    }

    public void editProject(Project project, Map<FlatType, Integer> updatedUnits, int officerSlots) {
        for (FlatType type : updatedUnits.keySet()) {
            project.setUnitCount(type, updatedUnits.get(type));
        }
        project.setAvailableOfficerSlots(officerSlots);
    }

    public void deleteProject(Project project) {
        projects.remove(project);
    }

    public void toggleVisibility(Project project) {
        project.toggleVisibility();
    }

    public List<Project> filterVisibleProjects() {
        List<Project> visible = new ArrayList<>();
        for (Project p : projects) {
            if (p.isVisible()) {
                visible.add(p);
            }
        }
        return visible;
    }

    public void registerOfficer(HDBOfficer officer, Project project) {
        int remaining = project.getAvailableOfficerSlots();
        if (remaining > 0) {
            project.setAvailableOfficerSlots(remaining - 1);
            // Logic to link officer to project could be added here
        }
    }

    public List<Project> getAllProjects() {
        return projects;
    }
}
