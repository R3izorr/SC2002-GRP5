package repository;

import java.util.ArrayList;
import java.util.List;
import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import utils.FileUtils;

public class UserRepository { 
    private List<Applicant> applicants; 
    private List<HDBOfficer> officers; 
    private List<HDBManager> managers;
    private String applicantFilePath;
    private String officerFilePath;
    private String managerFilePath;
    
    public UserRepository(String applicantFilePath, String officerFilePath, String managerFilePath) {
        this.applicantFilePath = applicantFilePath;
        this.officerFilePath = officerFilePath;
        this.managerFilePath = managerFilePath;
        applicants = new ArrayList<>();
        officers = new ArrayList<>();
        managers = new ArrayList<>();

    }
    
    public void loadUsers() {
        loadApplicants();
        loadOfficers();
        loadManagers();
    }

    public void loadApplicants() {
        List<String[]> lines = FileUtils.readCSV(applicantFilePath);
        for (String[] tokens : lines) {
            String name = tokens[0];
            String nric = tokens[1];
            String password = tokens[4];
            int age = Integer.parseInt(tokens[2]);
            String maritalStatus = tokens[3];
            Applicant applicant = new Applicant(name, nric, password, age, maritalStatus);
            applicants.add(applicant);
        }
    }
    
    public void loadOfficers() {
        List<String[]> lines = FileUtils.readCSV(officerFilePath);
        for (String[] tokens : lines) {
            String name = tokens[0];
            String nric = tokens[1];
            String password = tokens[4];
            int age = Integer.parseInt(tokens[2]);
            String maritalStatus = tokens[3];
            HDBOfficer officer = new HDBOfficer(name, nric, password, age, maritalStatus);
            officers.add(officer);
        }
    }
    
    public void loadManagers() {
        List<String[]> lines = FileUtils.readCSV(managerFilePath);
        for (String[] tokens : lines) {
            String name = tokens[0];
            String nric = tokens[1];
            String password = tokens[4];
            int age = Integer.parseInt(tokens[2]);
            String maritalStatus = tokens[3];
            HDBManager manager = new HDBManager(name, nric, password, age, maritalStatus);
            managers.add(manager);
        }
    }
    
    public List<Applicant> getApplicants() {
        return applicants;
    }
    
    public List<HDBOfficer> getOfficers() {
        return officers;
    }
    
    public List<HDBManager> getManagers() {
        return managers;
    }

    public void saveApplicants() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name","NRIC","Age","Marital Status","Password"});
        for (Applicant a : applicants) {
            data.add(new String[]{a.getName(),a.getNric(), String.valueOf(a.getAge()), a.getMaritalStatus(), a.getPassword()});
        }
        FileUtils.writeCSV(applicantFilePath, data);
    }
    
    public void saveOfficers() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name","NRIC","Age","Marital Status","Password"});
        for (HDBOfficer o : officers) {
            data.add(new String[]{o.getName(),o.getNric(), String.valueOf(o.getAge()), o.getMaritalStatus(),o.getPassword()});
        }
        FileUtils.writeCSV(officerFilePath, data);
    }
    
    public void saveManagers() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name","NRIC","Age","Marital Status","Password"});
        for (HDBManager m : managers) {
            data.add(new String[]{m.getName(),m.getNric(), String.valueOf(m.getAge()), m.getMaritalStatus(), m.getPassword()});
        }
        FileUtils.writeCSV(managerFilePath, data);
    }


}