package main;

import controller.ProjectManager;
import controller.ApplicationManager;
import controller.EnquiryManager;
import model.*;
import utils.ApplicationStatus;

import java.util.List;
import java.util.Scanner;

public class OfficerDashboard {
    private final ProjectManager projectManager;
    private final ApplicationManager applicationManager;
    private final EnquiryManager enquiryManager;
    private final Scanner scanner;

    public OfficerDashboard(ProjectManager projectManager,
                            ApplicationManager applicationManager,
                            EnquiryManager enquiryManager,
                            Scanner scanner) {
        this.projectManager = projectManager;
        this.applicationManager = applicationManager;
        this.enquiryManager = enquiryManager;
        this.scanner = scanner;
    }

    public void launch(HDBOfficer officer) {
        while (true) {
            System.out.println("\n=== HDB Officer Dashboard ===");
            System.out.println("1. Register for Project");
            System.out.println("2. Book Flat for Applicant");
            System.out.println("3. Generate Receipt");
            System.out.println("4. Reply to Enquiry");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerForProject(officer);
                case "2" -> bookFlat();
                case "3" -> generateReceipt();
                case "4" -> replyToEnquiry();
                case "5" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void registerForProject(HDBOfficer officer) {
        var projects = projectManager.filterVisibleProjects();
        if (projects.isEmpty()) {
            System.out.println("No visible projects available.");
            return;
        }

        System.out.println("\nSelect a project to register for:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getDetails());
        }

        System.out.print("Enter project number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < projects.size()) {
                Project selected = projects.get(index);
                projectManager.registerOfficer(officer, selected);
                System.out.println("Registration submitted to manager for approval.");
            } else {
                System.out.println("Invalid project selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void bookFlat() {
        List<Application> apps = applicationManager.getAllApplications();
        if (apps.isEmpty()) {
            System.out.println("No applications to process.");
            return;
        }

        System.out.println("Select application to book flat for:");
        for (int i = 0; i < apps.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1,
                    apps.get(i).getApplicant().getNric(),
                    apps.get(i).getProject().getName());
        }

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < apps.size()) {
                Application selected = apps.get(index);
                System.out.print("Enter flat type (e.g., TWO_ROOM, THREE_ROOM): ");
                String flatType = scanner.nextLine().trim();
                selected.updateStatus(ApplicationStatus.BOOKED);
                System.out.println("Flat booked successfully for " + selected.getApplicant().getNric());
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void generateReceipt() {
        List<Application> apps = applicationManager.getAllApplications();
        if (apps.isEmpty()) {
            System.out.println("No applications to generate receipt for.");
            return;
        }

        System.out.println("Select application to generate receipt:");
        for (int i = 0; i < apps.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1,
                    apps.get(i).getApplicant().getNric(),
                    apps.get(i).getProject().getName());
        }

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < apps.size()) {
                Application app = apps.get(index);
                Applicant a = app.getApplicant();
                String receipt = "\n===== RECEIPT =====\n"
                        + "Name (NRIC): " + a.getNric() + "\n"
                        + "Age: " + a.getAge() + "\n"
                        + "Marital Status: " + a.getMaritalStatus() + "\n"
                        + "Flat Type: " + app.getStatus() + "\n"
                        + "Project: " + app.getProject().getName() + "\n"
                        + "===================";
                System.out.println(receipt);
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void replyToEnquiry() {
        List<Enquiry> enquiries = enquiryManager.getAllEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries to reply to.");
            return;
        }

        for (int i = 0; i < enquiries.size(); i++) {
            System.out.printf("%d. [%s] %s%n", i + 1,
                    enquiries.get(i).getApplicant().getNric(),
                    enquiries.get(i).getContent());
        }

        System.out.print("Select enquiry to reply: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < enquiries.size()) {
                Enquiry e = enquiries.get(index);
                System.out.print("Enter your reply: ");
                String reply = scanner.nextLine().trim();
                enquiryManager.replyEnquiry(e, reply);
                System.out.println("Reply submitted.");
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
