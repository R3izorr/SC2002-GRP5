package model;

import java.util.ArrayList; 
import java.util.List;

public class HDBManager extends User { 
    private List<BTOProject> managedProjects;

    public HDBManager(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.managedProjects = new ArrayList<>();
    }
    
    public List<BTOProject> getManagedProjects() {
        return managedProjects;
    }
    
    public void addManagedProject(BTOProject project) {
        managedProjects.add(project);
    }
    
    public void removeManagedProject(BTOProject project) {
        managedProjects.remove(project);
    }
    
    @Override
    public String getRole() {
        return "HDBManager";
    }
}