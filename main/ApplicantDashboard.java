package main;

import controller.ApplicationManager;
import controller.ProjectManager;
import java.util.List;
import java.util.Scanner;
import model.Applicant;
import model.Application;
import model.Project;

public class ApplicantDashboard {
    private final ProjectManager projectManager;
    private final ApplicationManager applicationManager;
    private final Scanner scanner;

    public ApplicantDashboard(ProjectManager pm, ApplicationManager am, Scanner sc) {
        this.projectManager = pm;
        this.applicationManager = am;
        this.scanner = sc;
    }

    public void launch(Applicant applicant) {
        while (true) {
            System.out.println("\n=== Applicant Dashboard ===");
            System.out.println("1. View Eligible Projects");
            System.out.println("2. Apply for Project");
            System.out.println("3. View Application Status");
            System.out.println("4. Withdraw Application");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewProjects();
                case "2" -> applyForProject(applicant);
                case "3" -> viewApplicationStatus(applicant);
                case "4" -> withdrawApplication(applicant);
                case "5" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void viewProjects() {
        List<Project> visible = projectManager.filterVisibleProjects();
        if (visible.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }
        for (Project p : visible) {
            System.out.println(p.getDetails());
        }
    }

    private void applyForProject(Applicant applicant) {
        List<Project> visible = projectManager.filterVisibleProjects();
        if (visible.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }

        for (int i = 0; i < visible.size(); i++) {
            System.out.println((i + 1) + ". " + visible.get(i).getDetails());
        }

        System.out.print("Enter project number: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index >= 0 && index < visible.size()) {
            Project selected = visible.get(index);
            applicationManager.submitApplication(applicant, selected);
            System.out.println("Application submitted.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private void viewApplicationStatus(Applicant applicant) {
        List<Application> apps = applicationManager.getApplicationsByApplicant(applicant);
        if (apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }
        for (Application app : apps) {
            System.out.printf("Project: %s | Status: %s%n",
                    app.getProject().getName(),
                    app.getStatus());
        }
    }

    private void withdrawApplication(Applicant applicant) {
        List<Application> apps = applicationManager.getApplicationsByApplicant(applicant);
        if (apps.isEmpty()) {
            System.out.println("No applications to withdraw.");
            return;
        }

        for (int i = 0; i < apps.size(); i++) {
            System.out.printf("%d. %s (Status: %s)%n", i + 1,
                    apps.get(i).getProject().getName(),
                    apps.get(i).getStatus());
        }

        System.out.print("Enter number: ");
        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (index >= 0 && index < apps.size()) {
            Application selected = apps.get(index);
            applicationManager.withdrawApplication(selected);
            System.out.println("Application withdrawn.");
        } else {
            System.out.println("Invalid selection.");
        }
    }
}
