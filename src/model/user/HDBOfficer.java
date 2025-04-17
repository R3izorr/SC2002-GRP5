package model.user;

import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.BTOProject;

public class HDBOfficer extends Applicant {
    // List of projects for which registration is approved.
    private List<BTOProject> assignedProjects;
    // List of projects for which registration is pending.
    private List<BTOProject> pendingRegistrations;
    
    private Application application;
    
    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        assignedProjects = new ArrayList<>();
        pendingRegistrations = new ArrayList<>();
        this.application = null;
    }
    
    public List<BTOProject> getAssignedProjects() {
        return assignedProjects;
    }
    
    public void addAssignedProject(BTOProject project) {
        assignedProjects.add(project);
    }
    
    public List<BTOProject> getPendingRegistrations() {
        return pendingRegistrations;
    }
    
    public void addPendingRegistration(BTOProject project) {
        pendingRegistrations.add(project);
    }

    public void unAssignedProject(BTOProject project) {
            this.assignedProjects.remove(project);
    }

    // Check if any approved project overlaps with newProject's application period.
    public boolean hasOverlappingApprovedProject(BTOProject newProject) {
        for (BTOProject proj : assignedProjects) {
            if (isOverlap(proj, newProject))
                return true;
        }
        return false;
    }
    
    // Overlap exists if proj1's application period overlaps with proj2's.
    public boolean isOverlap(BTOProject proj1, BTOProject proj2) {
        return !proj1.getApplicationClose().before(proj2.getApplicationOpen()) &&
               !proj2.getApplicationClose().before(proj1.getApplicationOpen());
    }
    
    public String getRole() {
        return "HDBOfficer";
    }
}