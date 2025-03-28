
package model;

import utils.MaritalStatus;
import utils.Role;

public class HDBManager extends User {

    public HDBManager(String nric, String password, int age, MaritalStatus maritalStatus) {
        super(nric, password, age, maritalStatus, Role.HDB_MANAGER);
    }

    public void createProject() {}
    public void editProject() {}
    public void deleteProject() {}
    public void toggleProjectVisibility(Project project) {}
    public void approveOfficerRegistration(HDBOfficer officer, Project project) {}
    public void approveApplication(Application application) {}
    public void rejectApplication(Application application) {}
    public void approveWithdrawal(Application application) {}
    public void generateReport() {}
    public void replyEnquiry(Enquiry enquiry, String reply) {}
}
