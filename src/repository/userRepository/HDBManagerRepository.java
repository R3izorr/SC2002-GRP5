package repository.userRepository;

import java.util.ArrayList;
import java.util.List;

import entity.user.HDBManager;
import repository.IRepository;
import utils.FileUtils;

public class HDBManagerRepository implements IRepository<HDBManager> {
    private List<HDBManager> managers;
    private String filePath;

    public HDBManagerRepository(String filePath) {
        this.filePath = filePath;
        managers = new ArrayList<>();
    }

    @Override
    public List<HDBManager> getAll() {
        return managers;
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
            HDBManager manager = new HDBManager(name, nric, password, age, maritalStatus);
            managers.add(manager);
        }
    }

    @Override
    public void update() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Name", "NRIC", "Age", "Marital Status", "Password"});
        for (HDBManager m : managers) {
            data.add(new String[]{m.getName(), m.getNric(), String.valueOf(m.getAge()), m.getMaritalStatus(), m.getPassword()});
        }
        FileUtils.writeCSV(filePath, data);
    }
    
}
