package model;

import utils.MaritalStatus;
import utils.Role;
import utils.FlatType;
import controller.ProjectManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private Project appliedProject;
    private String applicationStatus; // "Pending", "Successful", "Unsuccessful", "Booked"
    private FlatType appliedFlatType;
    private List<Enquiry> enquiries;
    public Applicant(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.APPLICANT);
        this.appliedProject = null;
        this.applicationStatus = null;
        this.appliedFlatType = null;
        this.enquiries = new ArrayList<>();
    }

    // Method stubs for now — logic comes in Step 2
    //@param projectManager an instance of ProjectManager used to retrieve all projects
    public void viewEligibleProjects(ProjectManager projectManager) {
       List<Project> eligible = new ArrayList<>();
       LocalDate today = LocalDate.now();
       for (Project p : projectManager.getAllProjects()) {
           // Check if project is visible and within application period
           if (p.isVisible() &&
               (today.equals(p.getApplicationOpenDate()) || today.isAfter(p.getApplicationOpenDate())) &&
               (today.equals(p.getApplicationCloseDate()) || today.isBefore(p.getApplicationCloseDate()))) {
               
               // Eligibility based on marital status and age
               if (this.getMaritalStatus() == MaritalStatus.SINGLE) {
                   if (this.getAge() >= 35 && p.getUnitCount().get(FlatType.TWO_ROOM) > 0) {
                       eligible.add(p);
                   }
               } else if (this.getMaritalStatus() == MaritalStatus.MARRIED) {
                   if (this.getAge() >= 21 &&
                       (p.getUnitCount().get(FlatType.TWO_ROOM) > 0 || p.getUnitCount().get(FlatType.THREE_ROOM) > 0)) {
                       eligible.add(p);
                   }
               }
           }
       }
       System.out.println("Eligible Projects:");
       if (eligible.isEmpty()) {
           System.out.println("No eligible projects found at this time.");
       } else {
           for (Project p : eligible) {
               System.out.println(p.getDetails());
           }
       }
   }
    
   public void applyForProject(Project project) {
        if (this.appliedProject != null) {
            System.out.println("You have already applied for a project.");
            return;
        }
        LocalDate today = LocalDate.now();
        if (!project.isVisible() ||
            today.isBefore(project.getApplicationOpenDate()) ||
            today.isAfter(project.getApplicationCloseDate())) {
            System.out.println("Project is not open for application.");
            return;
        }
        
        // Determine eligibility and select flat type
        if (this.getMaritalStatus() == MaritalStatus.SINGLE) {
            if (this.getAge() < 35) {
                System.out.println("Singles must be at least 35 years old to apply.");
                return;
            }
            if (project.getUnitCount().get(FlatType.TWO_ROOM) <= 0) {
                System.out.println("No available 2-Room units for this project.");
                return;
            }
            // Single applicants can only apply for 2-Room
            this.appliedFlatType = FlatType.TWO_ROOM;
        } else if (this.getMaritalStatus() == MaritalStatus.MARRIED) {
            if (this.getAge() < 21) {
                System.out.println("Married applicants must be at least 21 years old to apply.");
                return;
            }
            // Married applicants: choose 3-Room if available, otherwise 2-Room
            if (project.getUnitCount().get(FlatType.THREE_ROOM) > 0) {
                this.appliedFlatType = FlatType.THREE_ROOM;
            } else if (project.getUnitCount().get(FlatType.TWO_ROOM) > 0) {
                this.appliedFlatType = FlatType.TWO_ROOM;
            } else {
                System.out.println("No available units for this project.");
                return;
            }
        } else {
            System.out.println("Invalid marital status.");
            return;
        }
        
        // Deduct one unit from the project for the chosen flat type
        int currentCount = project.getUnitCount().get(this.appliedFlatType);
        project.setUnitCount(this.appliedFlatType, currentCount - 1);

        this.appliedProject = project;
        this.applicationStatus = "Pending";
        System.out.println("Application submitted successfully for project: " + project.getName() +
                        " with flat type: " + this.appliedFlatType);
        }

    public void viewApplicationStatus() {
        if (this.appliedProject == null) {
            System.out.println("No application found.");
        } else {
            System.out.println("Application Status: " + this.applicationStatus);
            System.out.println("Applied Project Details: " + this.appliedProject.getDetails());
        }
    }

    public void withdrawApplication() {
        if (this.appliedProject == null) {
            System.out.println("No application to withdraw.");
            return;
        }
        int currentCount = this.appliedProject.getUnitCount().get(this.appliedFlatType);
        this.appliedProject.setUnitCount(this.appliedFlatType, currentCount + 1);

        System.out.println("Application for project " + this.appliedProject.getName() + " has been withdrawn.");
        this.appliedProject = null;
        this.applicationStatus = null;
        this.appliedFlatType = null;
    }
    
    public void submitEnquiry(String content) {
        if (this.appliedProject == null) {
            System.out.println("No project associated with your enquiry.");
            return;
        }
        Enquiry enquiry = new Enquiry(this, this.appliedProject, content);
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted: " + enquiry.getContent());
    }

    public void editEnquiry(Enquiry enquiry, String newContent) {
        if (!enquiries.contains(enquiry)) {
            System.out.println("You can only edit your own enquiries.");
            return;
        }
        enquiry.editContent(newContent);
        System.out.println("Enquiry edited successfully.");
    }
    
    public void deleteEnquiry(Enquiry enquiry) {
        if (!enquiries.contains(enquiry)) {
            System.out.println("You can only delete your own enquiries.");
            return;
        }
        enquiry.delete();
        System.out.println("Enquiry deleted successfully.");
    }
}

