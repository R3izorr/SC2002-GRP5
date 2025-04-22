package controller;

import entity.model.BTOProject;
import entity.model.Enquiry;
import entity.user.Applicant;
import java.util.List;
import repository.ICRUDRepository;

public class EnquiryController {
    // For applicant mode, currentApplicant is not null; for manager/officer mode, it can be null.
    private Applicant currentApplicant;
    private ICRUDRepository<BTOProject> projectRepository;
    private ICRUDRepository<Enquiry> enquiryRepository;
    
    // Constructor for applicant mode (currentApplicant is set)
    public EnquiryController(ICRUDRepository<BTOProject> projectRepository, ICRUDRepository<Enquiry> enquiryRepository, Applicant currentApplicant) {
        this.currentApplicant = currentApplicant;
        this.projectRepository = projectRepository;
        this.enquiryRepository = enquiryRepository;
    }
    
    // For manager/officer usage (no current applicant needed)
    public EnquiryController(ICRUDRepository<BTOProject> projectRepository, ICRUDRepository<Enquiry> enquiryRepository) {
        this.currentApplicant = null; // No current applicant in this mode
        this.projectRepository = projectRepository;
        this.enquiryRepository = enquiryRepository;
    }

    // Applicant submits an enquiry for a given project ID.
    public void submitEnquiry(int projectId, String message) {
        BTOProject project = projectRepository.getById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        Enquiry enquiry = new Enquiry(currentApplicant, project, message, "", new java.util.Date());
        enquiryRepository.add(enquiry);
        enquiryRepository.update(); // Save the enquiry to the repository
    }
    
    // For applicant editing their own enquiry message.
    public void editEnquiry(int enquiryId, String newMessage) {
        List<Enquiry> allEnquiries = enquiryRepository.getAll();
        for (Enquiry enquiry : allEnquiries) {
            if(enquiry.getEnquiryId() == enquiryId &&
               enquiry.getApplicant().getNric().equals(currentApplicant.getNric())) {
                enquiry.setMessage(newMessage);
                enquiryRepository.update(); // Save the updated enquiry to the repository
                System.out.println("Enquiry message updated.");
                return;
            }
        }
        System.out.println("Enquiry not found or permission denied.");
    }
    
    // For manager/officer replying to an enquiry.
    public void replyEnquiry(int enquiryId, String newReply) {
        List<Enquiry> allEnquiries = enquiryRepository.getAll();
        for (Enquiry enquiry : allEnquiries) {
            if(enquiry.getEnquiryId() == enquiryId) {
                String currentReply = enquiry.getReply();
                if(currentReply == null || currentReply.isEmpty()){
                    enquiry.setReply(newReply);
                    
                } else {
                    // Append new reply without overwriting existing reply.
                    enquiry.setReply(currentReply + "\n" + newReply);
                }
                System.out.println("Reply sent successfully.");
                enquiryRepository.update(); // Save the updated enquiry to the repository
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
    
    // For applicant deleting their own enquiry.
    public void deleteEnquiry(int enquiryId) {
       Enquiry enquiry = enquiryRepository.getById(enquiryId);
         if(enquiry != null) {
              enquiryRepository.remove(enquiry); // Remove the enquiry from the repository
              enquiryRepository.update(); // Save the updated enquiry to the repository
              System.out.println("Enquiry deleted successfully.");
         } else {
              System.out.println("Enquiry not found.");
         }
    }
    
    // Get all enquiries: if currentApplicant is set, return only their enquiries; else return all.
    public List<Enquiry> getEnquiries() {
        List<Enquiry> allEnquiries = enquiryRepository.getAll();
        if(currentApplicant != null) {
            return allEnquiries.stream()
                    .filter(e -> e.getApplicant().getNric().equals(currentApplicant.getNric()))
                    .toList();
        } else {
            return allEnquiries;
        }
    }
    
    // For manager/officer: get enquiries for a specific project.
    public List<Enquiry> getEnquiriesForProject(int projectId) {
        List<Enquiry> result = allEnquiriesForProject(projectId);
        return result;
    }
    
    private List<Enquiry> allEnquiriesForProject(int projectId) {
        List<Enquiry> result = new java.util.ArrayList<>();
        for(Enquiry e : enquiryRepository.getAll()) {
            if(e.getProject() != null && e.getProject().getProjectId() == projectId)
                result.add(e);
        }
        return result;
    }
}
