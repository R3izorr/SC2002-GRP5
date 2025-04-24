package model;

import utils.MaritalStatus;
import utils.Role;

public class HDBOfficer extends Applicant {

    public HDBOfficer(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.role = Role.HDB_OFFICER; // override role inherited from Applicant
    }

    public void registerForProject(Project project) {}
    public void bookFlat(Application application, String flatType) {}
    public void generateReceipt(Application application) {}
    public void replyEnquiry(Enquiry enquiry, String reply) {}
}
