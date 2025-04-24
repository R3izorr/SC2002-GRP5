package repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
import model.BTOProject;
import model.HDBManager;
import model.HDBOfficer;
import utils.FileUtils;

public class ProjectRepository {
    private List<BTOProject> projects; 
    private SimpleDateFormat dateFormat;
    private String projectFilePath;
    public ProjectRepository(String projectFilePath) {
        projects = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // adjust if needed
        this.projectFilePath = projectFilePath;
    }
    
    public void loadProjects(String filePath, List<HDBManager> managers, List<HDBOfficer> officers) {
        List<String[]> lines = FileUtils.readCSV(filePath);
        for (String[] tokens : lines) {
            if (tokens.length < 13) {
                continue; // Skip this line if it doesn't have enough tokens
            }
            // Parse the tokens and create a BTOProject object
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
            String lineNric = tokens[12];
            if (lineNric.equals("None")) {
                continue;
            }
            List<String> ListOfficerNric = List.of(lineNric.split(";"));
            for (String Nric: ListOfficerNric) {
                for(HDBOfficer Officer: officers) {
                    if(Officer.getNric().equals(Nric)) {
                        Officer.addAssignedProject(project);
                        project.addOfficers(Officer);
                        break;
                    }
                }
            }
           
        }
    }

    public void saveProjects() {
        List<String[]> data = new ArrayList<>();
        // Optionally, add a header
        data.add(new String[]{"Project Name","Neighborhood","Type 1",
        "Number of units for Type 1","Selling price for Type 1","Type 2,Number of units for Type 2","Selling price for Type 2",
        "Application opening date","Application closing date","Manager","Remaining Officer Slots","Officers NRIC"});
        for (BTOProject proj : projects) {
            String[] row = new String[13];
            row[0] = proj.getProjectName();
            row[1] = proj.getNeighborhood();
            row[2] = "2-Room";
            row[3] = String.valueOf(proj.getUnits2Room());
            row[4] = String.valueOf(proj.getSellingPrice2Room());
            row[5] = "3-Room";
            row[6] = String.valueOf(proj.getUnits3Room());
            row[7] = String.valueOf(proj.getSellingPrice3Room());
            row[8] = dateFormat.format(proj.getApplicationOpen());
            row[9] = dateFormat.format(proj.getApplicationClose());
            row[10] = (proj.getManager() != null ? proj.getManager().getNric() : "");
            row[11] = String.valueOf(proj.getOfficerSlots());
            row[12] = proj.getOfficerNRIC();
            data.add(row);
        }
        FileUtils.writeCSV(projectFilePath, data);
    }


    
    public List<BTOProject> getProjects() {
        return projects;
    }

    public BTOProject getProjectById(int id) {
        for (BTOProject p : projects) {
            if(p.getProjectId() == id)
                return p;
        }
        return null;
    }
}
