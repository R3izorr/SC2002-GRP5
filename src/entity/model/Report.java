package entity.model;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private List<Receipt> receipts;
    
    public Report(List<Application> bookedApplications) {
        receipts = new ArrayList<>();
        for (Application app : bookedApplications) {
            receipts.add(new Receipt(app));
        }
    }
    
    public List<Receipt> getReceipts() {
        return receipts;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================================================\n");
        sb.append("                                             BOOKING REPORT                                        \n");
        sb.append("====================================================================================================\n");
        sb.append(String.format("%-15s | %-15s | %-5s | %-15s | %-10s | %-10s | %-20s\n", 
                "Applicant NRIC", "Name", "Age", "Marital Status", "Flat Type", "Proj ID", "Project Name"));
        sb.append("----------------------------------------------------------------------------------------------------\n");
        for (Receipt r : receipts) {
            sb.append(String.format("%-15s | %-15s | %-5d | %-15s | %-10s | %-10d | %-20s\n",
                    r.getApplicantNric(),
                    r.getApplicantName(),
                    r.getAge(),
                    r.getMaritalStatus(),
                    r.getFlatType(),
                    r.getProjectId(),
                    r.getProjectName()));
        }
        sb.append("====================================================================================================\n");
        return sb.toString();
    }
}