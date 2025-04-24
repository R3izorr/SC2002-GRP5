package controller;

import model.Enquiry;

import java.util.ArrayList;
import java.util.List;

public class EnquiryManager {
    private List<Enquiry> enquiries;

    public EnquiryManager() {
        this.enquiries = new ArrayList<>();
    }

    public void submitEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public void editEnquiry(Enquiry enquiry, String newContent) {
        enquiry.editContent(newContent);
    }

    public void deleteEnquiry(Enquiry enquiry) {
        enquiry.delete();
    }

    public void replyEnquiry(Enquiry enquiry, String reply) {
        enquiry.editContent(enquiry.getContent() + "\n[Reply]: " + reply);
    }

    public List<Enquiry> getAllEnquiries() {
        return enquiries;
    }
}
