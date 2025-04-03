package boundary;

import controller.ProjectController;
import model.BTOProject;
import java.util.List;
import java.util.Scanner;

public class ProjectBoundary {
    private ProjectController projectController; 
    private Scanner scanner;
    public ProjectBoundary(ProjectController projectController) {
        this.projectController = projectController;
        this.scanner = new Scanner(System.in);
    }
    
    // Display only visible projects.
    public void displayProjects() {
        List<BTOProject> projects = projectController.getVisibleProjects();
        if(projects.isEmpty()){
            System.out.println("No projects available for your criteria.");
            return;
        }
        System.out.println("=== Available BTO Projects ===");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i+1) + ". " + projects.get(i).toString());
        }
    }
}
