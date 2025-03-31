package controller;

import java.util.List;
import model.Applicant;
import model.Enquiry;
import repository.EnquiryRepository; 

public class EnquiryController {
    private Applicant currentApplicant; 

    public EnquiryController(Applicant applicant) {
        this.currentApplicant = applicant;
    }
    
    // Controller method that accepts parameters from the Boundary.
    public void submitEnquiry(int projectId, String message) {
        // In a full implementation, you would retrieve the project by ID.
        // Here we assume the enquiry is linked to null.
        Enquiry enquiry = new Enquiry(currentApplicant, null, message);
        EnquiryRepository.addEnquiry(enquiry);
    }
    
    public void editEnquiry(int enquiryId, String newMessage) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        for (Enquiry enquiry : allEnquiries) {
            if(enquiry.getEnquiryId() == enquiryId &&
               enquiry.getApplicant().getNric().equals(currentApplicant.getNric())) {
                enquiry.setMessage(newMessage);
                return;
            }
        }
        System.out.println("Enquiry not found or permission denied.");
    }
    
    public void deleteEnquiry(int enquiryId) {
        List<Enquiry> allEnquiries = EnquiryRepository.getEnquiries();
        boolean removed = allEnquiries.removeIf(e -> e.getEnquiryId() == enquiryId &&
                e.getApplicant().getNric().equals(currentApplicant.getNric()));
        if(!removed) {
            System.out.println("Enquiry not found or permission denied.");
        }
    }
    
    public List<Enquiry> getEnquiries() {
        return EnquiryRepository.getEnquiries();
    }
}
