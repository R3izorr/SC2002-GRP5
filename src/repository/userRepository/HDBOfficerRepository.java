package repository.userRepository;

import java.util.List;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import repository.IRepository;
import utils.FileUtils;

public class HDBOfficerRepository implements IRepository<HDBOfficer> {
    private List<HDBOfficer> officers;
    private String filePath;

    public HDBOfficerRepository(String filePath) {
        this.filePath = filePath;
        officers = new ArrayList<>();
    }

    @Override
    public List<HDBOfficer> getAll() {
        return officers;
    }

    @Override
    public void load() {
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

    @Override
    public void update() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name", "NRIC", "Age", "Marital Status", "Password"});
        for (HDBOfficer o : officers) {
            data.add(new String[]{o.getName(), o.getNric(), String.valueOf(o.getAge()), o.getMaritalStatus(), o.getPassword()});
        }
        FileUtils.writeCSV(filePath, data);
    }
}
