package model;

public class HDBOfficer extends Applicant {
    private boolean isRegistered; 
    private BTOProject assignedProject; 
    private String registrationStatus; // "NONE", "PENDING", "APPROVED", or "REJECTED"
    
    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.isRegistered = false;
        this.assignedProject = null;
        this.registrationStatus = "NONE";
    }
    

    public boolean isRegistered() {
        return isRegistered;
    }
    
    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }
    
    public BTOProject getAssignedProject() {
        return assignedProject;
    }
    
    public void setAssignedProject(BTOProject assignedProject) {
        this.assignedProject = assignedProject;
    }
    
    public String getRegistrationStatus() {
        return registrationStatus;
    }
    
    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
    
    // Override setApplication to disallow applicant registration if already registered as officer.
    @Override
    public void setApplication(Application application) {
        if (!registrationStatus.equals("NONE")) {
            System.out.println("HDBOfficer cannot apply as an Applicant once registered for a project.");
        } else {
            super.setApplication(application);
        }
    }
    
    @Override
    public String getRole() {
        return "HDBOfficer";
    }
}