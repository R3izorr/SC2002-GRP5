package repository.userRepository;

import java.util.ArrayList;
import java.util.List;

import entity.user.Applicant;
import repository.IRepository;
import utils.FileUtils;


public class ApplicantRepository implements IRepository<Applicant> {
    // Implement the methods from IRepository
    private List<Applicant> applicants;
    private String filePath;
    
    public ApplicantRepository(String filePath) {
        this.filePath = filePath;
        applicants = new ArrayList<>();
    }

    @Override
    public List<Applicant> getAll() {
        // Implementation to retrieve all applicants from the database
        return applicants;
    }

    @Override
    public void load() {
        // Implementation to load applicants from the database
        // This could involve reading from a file or a database
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

    @Override
    public void update() {
        // Implementation to update an existing applicant in the database
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name","NRIC","Age","Marital Status","Password"});
        for (Applicant a : applicants) {
            data.add(new String[]{a.getName(),a.getNric(), String.valueOf(a.getAge()), a.getMaritalStatus(), a.getPassword()});
        }
        FileUtils.writeCSV(filePath, data);
    }
    
}
