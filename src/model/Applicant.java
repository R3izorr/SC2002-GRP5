package model;

public class Applicant extends User {
    private Application application;

    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.application = null;
    }
    
    public Application getApplication() {
        return application;
    }
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    @Override
    public String getRole() {
        return "Applicant";
    }
}
