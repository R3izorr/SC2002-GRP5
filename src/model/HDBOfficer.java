package model;

import java.util.ArrayList;
import java.util.List;

public class HDBOfficer extends Applicant {
    // List of projects for which registration is approved.
    private List<BTOProject> assignedProjects;
    // List of projects for which registration is pending.
    private List<BTOProject> pendingRegistrations;
    
    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        assignedProjects = new ArrayList<>();
        pendingRegistrations = new ArrayList<>();
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
    
    // Check if any approved project overlaps with newProject's application period.
    public boolean hasOverlappingApprovedProject(BTOProject newProject) {
        for (BTOProject proj : assignedProjects) {
            if (isOverlap(proj, newProject))
                return true;
        }
        return false;
    }
    
    // Overlap exists if proj1's application period overlaps with proj2's.
    private boolean isOverlap(BTOProject proj1, BTOProject proj2) {
        return !proj1.getApplicationClose().before(proj2.getApplicationOpen()) &&
               !proj2.getApplicationClose().before(proj1.getApplicationOpen());
    }
    
    // Override setApplication: disallow setting an application if there's an approved registration overlapping.
    @Override
    public void setApplication(Application application) {
        if (application != null && hasOverlappingApprovedProject(application.getProject())) {
            System.out.println("Cannot apply as Applicant for a project overlapping with your approved registration.");
        } else {
            super.setApplication(application);
        }
    }
    
    @Override
    public String getRole() {
        return "HDBOfficer";
    }
}