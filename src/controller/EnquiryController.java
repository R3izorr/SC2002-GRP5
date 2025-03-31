package controller;

import java.util.ArrayList;
import java.util.List;
import model.Applicant;
import model.BTOProject;
import model.Enquiry;
import repository.EnquiryRepository;
import repository.ProjectRepository;

public class EnquiryController {
    // currentApplicant may be null for Manager/Officer usage.
    private Applicant currentApplicant;
    private ProjectRepository projectRepository;
    
    public EnquiryController(Applicant applicant, ProjectRepository projectRepository) {
        this.currentApplicant = applicant;
        this.projectRepository = projectRepository;
    }
    
    // For Manager/Officer, you can instantiate with currentApplicant as null.
    public EnquiryController(ProjectRepository projectRepository) {
        this.currentApplicant = null;
        this.projectRepository = projectRepository;
    }
    
    // When an applicant submits an enquiry, they enter a project ID.
    public void submitEnquiry(int projectId, String message) {
        BTOProject project = projectRepository.getProjectById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        // If currentApplicant is null, use a dummy applicant (or error) – here we assume currentApplicant is not null.
        Enquiry enquiry = new Enquiry(currentApplicant, project, message);
        EnquiryRepository.addEnquiry(enquiry);
    }
    
    public void editEnquiry(int enquiryId, String newMessage) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        for (Enquiry enquiry : allEnquiries) {
            // If currentApplicant is set, ensure only their enquiries are edited.
            if(currentApplicant != null) {
                if(enquiry.getEnquiryId() == enquiryId &&
                   enquiry.getApplicant().getNric().equals(currentApplicant.getNric())) {
                    enquiry.setMessage(newMessage);
                    return;
                }
            } else { // For Manager/Officer, allow editing any enquiry.
                if(enquiry.getEnquiryId() == enquiryId) {
                    enquiry.setMessage(newMessage);
                    return;
                }
            }
        }
        System.out.println("Enquiry not found or permission denied.");
    }
    
    public void deleteEnquiry(int enquiryId) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        boolean removed = allEnquiries.removeIf(e -> {
            if(currentApplicant != null)
                return e.getEnquiryId() == enquiryId && e.getApplicant().getNric().equals(currentApplicant.getNric());
            else
                return e.getEnquiryId() == enquiryId;
        });
        if(!removed) {
            System.out.println("Enquiry not found or permission denied.");
        }
    }
    
    // Return all enquiries if currentApplicant is null, else only enquiries submitted by the applicant.
    public List<Enquiry> getEnquiries() {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        if(currentApplicant == null) {
            return allEnquiries;
        } else {
            List<Enquiry> result = new ArrayList<>();
            for(Enquiry e : allEnquiries) {
                if(e.getApplicant().getNric().equals(currentApplicant.getNric()))
                    result.add(e);
            }
            return result;
        }
    }
    
    // For Manager/Officer: get enquiries for a specific project.
    public List<Enquiry> getEnquiriesForProject(int projectId) {
        List<Enquiry> result = new ArrayList<>();
        for(Enquiry e : EnquiryRepository.getEnquiries()) {
            if(e.getProject() != null && e.getProject().getProjectId() == projectId)
                result.add(e);
        }
        return result;
    }
}