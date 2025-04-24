package entity.model;
import entity.user.Applicant;
import java.util.Date;

public class Enquiry {
    private static int counter = 0; 
    private int enquiryId; 
    private Applicant applicant; 
    private BTOProject project; 
    private String message; 
    private Date date;
    private String reply;
  
    public Enquiry(Applicant applicant, BTOProject project, String message, String reply, Date date) {
        this.enquiryId = ++counter;
        this.applicant = applicant;
        this.project = project;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }
    
    public int getEnquiryId() {
        return enquiryId;
    }
    
    public Applicant getApplicant() {
        return applicant;
    }
    
    public BTOProject getProject() {
        return project;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Date getDate() {
        return date;
    }
    
    public String getReply() {
        return reply;
    }
    
    public void setReply(String reply) {
        this.reply = reply;
    }
    
    @Override
    public String toString() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return "ID: " + enquiryId + ". Enquiry for Project: " + project.getProjectName() + "(ID:" + project.getProjectId() + ")" +  "| Applicant: " + applicant.getName() +
            " | Message: " + message + " | Date: " + dateFormat.format(date) + 
            (reply.isEmpty() ? "" : " | Reply: " + reply);
    }
}
