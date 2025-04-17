package controller;

import java.util.List;
import model.BTOProject;
import model.Enquiry;
import model.user.Applicant;
import repository.EnquiryRepository;
import repository.ProjectRepository;

public class EnquiryController {
    // For applicant mode, currentApplicant is not null; for manager/officer mode, it can be null.
    private Applicant currentApplicant;
    private ProjectRepository projectRepository;
    
    public EnquiryController(Applicant applicant, ProjectRepository projectRepository) {
        this.currentApplicant = applicant;
        this.projectRepository = projectRepository;
    }
    
    // For manager/officer usage (no current applicant needed)
    public EnquiryController(ProjectRepository projectRepository) {
        this.currentApplicant = null;
        this.projectRepository = projectRepository;
    }
    
    // Applicant submits an enquiry for a given project ID.
    public void submitEnquiry(int projectId, String message) {
        BTOProject project = projectRepository.getProjectById(projectId);
        if(project == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        Enquiry enquiry = new Enquiry(currentApplicant, project, message);
        EnquiryRepository.addEnquiry(enquiry);
    }
    
    // For applicant editing their own enquiry message.
    public void editEnquiry(int enquiryId, String newMessage) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        for (Enquiry enquiry : allEnquiries) {
            if(enquiry.getEnquiryId() == enquiryId &&
               enquiry.getApplicant().getNric().equals(currentApplicant.getNric())) {
                enquiry.setMessage(newMessage);
                System.out.println("Enquiry message updated.");
                return;
            }
        }
        System.out.println("Enquiry not found or permission denied.");
    }
    
    // For manager/officer replying to an enquiry.
    public void replyEnquiry(int enquiryId, String newReply) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
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
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
    
    public void deleteEnquiry(int enquiryId) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        boolean removed = allEnquiries.removeIf(e -> {
            if(currentApplicant != null)
                return e.getEnquiryId() == enquiryId && e.getApplicant().getNric().equals(currentApplicant.getNric());
            else
                return e.getEnquiryId() == enquiryId;
        });
        if(removed){
            EnquiryRepository.removeEnquiry(allEnquiries.stream()
                    .filter(e -> e.getEnquiryId() == enquiryId)
                    .findFirst()
                    .orElse(null));
            System.out.println("Enquiry deleted.");
        } else {
            System.out.println("Enquiry not found or permission denied.");
        }
    }
    
    // Get all enquiries: if currentApplicant is set, return only their enquiries; else return all.
    public List<Enquiry> getEnquiries() {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
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
        for(Enquiry e : EnquiryRepository.getEnquiries()) {
            if(e.getProject() != null && e.getProject().getProjectId() == projectId)
                result.add(e);
        }
        return result;
    }
}
