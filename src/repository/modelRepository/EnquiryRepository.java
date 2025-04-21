package repository.modelRepository;

import entity.model.BTOProject;
import entity.model.Enquiry;
import entity.user.Applicant;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import repository.ICRUDRepository;
import utils.FileUtils;

public class EnquiryRepository implements ICRUDRepository<Enquiry> {

    private List<Enquiry> enquiries;
    private List<Applicant> applicants;
    private List<BTOProject> projects;
    private String enquiryfilePath; // Add this field
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Date format for parsing

    public EnquiryRepository(String enquiryfilePath, List<Applicant> applicants, List<BTOProject> projects) {
        this.enquiries = new ArrayList<>(); // Initialize the list of enquiries
        this.enquiryfilePath = enquiryfilePath; // Initialize the file path
        this.applicants = applicants;
        this.projects = projects;
    }

    @Override
    public void load() {
        List<String[]> lines = FileUtils.readCSV(this.enquiryfilePath);
         for (String[] tokens : lines) {
            // tokens: applicantName, applicantNric, projectId, message, dateMillis, reply
            if (tokens.length < 6) {
                continue; // Skip this line if it doesn't have enough tokens
            }
            String Name      = tokens[0];
            String Nric      = tokens[1];
            int projId       = Integer.parseInt(tokens[2]);
            String msg       = tokens[3];
            Date date;
            try {
                date = dateFormat.parse(tokens[4]);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date(); // Default to current date in case of error
            }
            String reply     = tokens.length>5 ? tokens[5] : "";

            Enquiry e = new Enquiry(applicants.stream()
                                    .filter(a -> a.getNric().equals(Nric))
                                    .findFirst()
                                    .orElse(null),
                                    projects.stream()
                                    .filter(p -> p.getProjectId() == projId)
                                    .findFirst()
                                    .orElse(null),
                                    msg, reply, date);
            enquiries.add(e);
        }
    }

    @Override
    public void update() {
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Applicant Name", "Applicant NRIC", "Project ID", "Message", "Date", "Reply"}); // Header
        for (Enquiry e : enquiries) {
            String[] tokens = new String[6];
            tokens[0] = e.getApplicant().getName();
            tokens[1] = e.getApplicant().getNric();
            tokens[2] = String.valueOf(e.getProject().getProjectId());
            tokens[3] = e.getMessage();
            tokens[4] = dateFormat.format(e.getDate()); // Convert Date to String
            tokens[5] = e.getReply();
            lines.add(tokens);
        }
        FileUtils.writeCSV(this.enquiryfilePath, lines);
    }

    @Override
    public void add(Enquiry enquiry) {
        enquiries.add(enquiry);
    }
    
    @Override
    public void remove(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }

    @Override
    public List<Enquiry> getAll() {
        return enquiries;
    }

    @Override
    public Enquiry getById(Object id) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId() == (int) id) {
                return enquiry;
            }
        }
        return null;
    }

}
