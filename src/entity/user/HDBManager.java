package entity.user;

import java.util.ArrayList; 
import java.util.List;
import entity.model.BTOProject;

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
    
    public void clearManagedProjects() {
        managedProjects.clear();
    }

    public String displayManagedProject() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Your Managed Projects === \n");
        for (BTOProject project : managedProjects) {
            sb.append("Project ID: ").append(project.getProjectId())
              .append("|| Project Name: ").append(project.getProjectName())
              .append("\n");
        }
        return sb.toString();
    }


    @Override
    public String getRole() {
        return "HDBManager";
    }
}