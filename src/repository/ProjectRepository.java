package repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
import model.BTOProject;
import model.HDBManager;
import utils.FileUtils;

public class ProjectRepository {
    private List<BTOProject> projects; 
    private SimpleDateFormat dateFormat;
    public ProjectRepository() {
        projects = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // adjust if needed
    }
    
    public void loadProjects(String filePath, List<HDBManager> managers) {
        List<String[]> lines = FileUtils.readCSV(filePath);
        for (String[] tokens : lines) {
            // Expected tokens: ProjectName, Neighborhood,2-Room,SellingPriceFor2Room units2Room,3-Room,SellingPricefor3Room, units3Room, applicationOpen, applicationClose, ManagerNRIC, officerSlots, isVisible
            String projectName = tokens[0];
            String neighborhood = tokens[1];
            int units2Room = Integer.parseInt(tokens[3]);
            int units3Room = Integer.parseInt(tokens[6]);
            float sellingPrice2Room = Float.parseFloat(tokens[4]);
            float sellingPrice3Room = Float.parseFloat(tokens[7]);
            Date applicationOpen = null;
            Date applicationClose = null;
            try {
                applicationOpen = dateFormat.parse(tokens[8]);
                applicationClose = dateFormat.parse(tokens[9]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String managerNRIC = tokens[10];
            HDBManager manager = null;
            for(HDBManager m : managers) {
                if(m.getNric().equals(managerNRIC)) {
                    manager = m;
                    break;
                }
            }
            int officerSlots = Integer.parseInt(tokens[11]);
            boolean isVisible = true;
            BTOProject project = new BTOProject(projectName, neighborhood, sellingPrice2Room, units2Room, sellingPrice3Room, units3Room,
                    applicationOpen, applicationClose, manager, officerSlots, isVisible);
            projects.add(project);
            if(manager != null) {
                manager.addManagedProject(project);
            }
        }
    }
    
    public List<BTOProject> getProjects() {
        return projects;
    }
}
