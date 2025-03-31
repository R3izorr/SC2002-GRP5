package model;

public class HDBOfficer extends User { 
    private boolean isRegistered;
    private BTOProject assignedProject; // The project assigned to the officer
    private String registrationStatus; // The registration status of the officer
    public HDBOfficer(String name,String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.isRegistered = false;
        this.assignedProject = null;
        this.registrationStatus = "NONE";
    }
    
    public boolean isRegistered() {
        return isRegistered;
    }
    
    public void setRegistered(boolean registered) {
        isRegistered = registered;
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
    
    @Override
    public String getRole() {
        return "HDBOfficer";
    }
}