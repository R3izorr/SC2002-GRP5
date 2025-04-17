package repository;

import java.util.ArrayList;
import java.util.List;
import model.Enquiry;

public class EnquiryRepository {

    private static List<Enquiry> enquiries = new ArrayList<>();
    
    public static void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }
    
    public static void removeEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }

    public static List<Enquiry> getEnquiries() {
        return enquiries;
    }
}
