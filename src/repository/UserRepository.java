package repository;

import java.util.ArrayList;
import java.util.List;
import model.Applicant;
import model.HDBManager;
import model.HDBOfficer; 
import utils.FileUtils;

public class UserRepository { 
    private List<Applicant> applicants; 
    private List<HDBOfficer> officers; 
    private List<HDBManager> managers;
    public UserRepository() {
        applicants = new ArrayList<>();
        officers = new ArrayList<>();
        managers = new ArrayList<>();
    }
    
    public void loadApplicants(String filePath) {
        List<String[]> lines = FileUtils.readCSV(filePath);
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
    
    public void loadOfficers(String filePath) {
        List<String[]> lines = FileUtils.readCSV(filePath);
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
    
    public void loadManagers(String filePath) {
        List<String[]> lines = FileUtils.readCSV(filePath);
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
}