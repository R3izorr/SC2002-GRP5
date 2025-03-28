package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.HDBManager;
import model.HDBOfficer;
import model.Project;
import utils.FlatType;
import utils.MaritalStatus;

public class ProjectManager {
    private List<Project> projects;

    public ProjectManager() {
        this.projects = new ArrayList<>();
        loadProjectList("data/ProjectList.csv");
    }
    
    // Updated createProject method (no officer parameter now)
    public Project createProject(String name, String location, LocalDate openDate, LocalDate closeDate,
                                 HDBManager manager, int officerSlots, Map<FlatType, Integer> unitMap) {
        Project project = new Project(name, location, openDate, closeDate, manager, officerSlots);
        for (FlatType type : unitMap.keySet()) {
            project.setUnitCount(type, unitMap.get(type));
        }
        projects.add(project);
        return project;
    }

    private void loadProjectList(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read and discard header line
            String header = br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by commas and trim each column
                String[] columns = line.split(",");
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = columns[i].trim();
                }
                // Expected CSV order:
                // 0: Project Name
                // 1: Neighborhood
                // 2: Type 1 (always TWO_ROOM)
                // 3: Number of units for Type 1
                // 4: Selling price for Type 1
                // 5: Type 2 (always THREE_ROOM)
                // 6: Number of units for Type 2
                // 7: Selling price for Type 2
                // 8: Application opening date (format: M/d/yyyy)
                // 9: Application closing date (format: M/d/yyyy)
                // 10: Manager
                // 11: Officer Slot
                // 12: Officer (if multiple, separated by semicolon)
                
                String projectName = columns[0];
                String neighborhood = columns[1];
                
                // Parse unit counts and selling prices
                int twoRoomCount = Integer.parseInt(columns[3]);
                double twoRoomPrice = Double.parseDouble(columns[4]);
                int threeRoomCount = Integer.parseInt(columns[6]);
                double threeRoomPrice = Double.parseDouble(columns[7]);
                
                // Parse the application dates using the formatter
                LocalDate openDate = LocalDate.parse(columns[8], formatter);
                LocalDate closeDate = LocalDate.parse(columns[9], formatter);
                
                // Process Manager field (create a dummy HDBManager if provided)
                String managerField = columns[10];
                HDBManager manager = null;
                if (!managerField.isEmpty() && !managerField.equalsIgnoreCase("N/A")) {
                    // For simplicity, using managerField as both name and NRIC, with default password and values.
                    manager = new HDBManager(managerField, managerField, "password", 0, MaritalStatus.SINGLE);
                }
                
                // Parse officer slot
                int officerSlots = Integer.parseInt(columns[11]);
                
                // Prepare unit count map
                Map<FlatType, Integer> unitMap = new HashMap<>();
                unitMap.put(FlatType.TWO_ROOM, twoRoomCount);
                unitMap.put(FlatType.THREE_ROOM, threeRoomCount);
                
                // Create the project
                Project project = createProject(projectName, neighborhood, openDate, closeDate, manager, officerSlots, unitMap);
                
                // Set selling prices
                project.setSellingPrice(FlatType.TWO_ROOM, twoRoomPrice);
                project.setSellingPrice(FlatType.THREE_ROOM, threeRoomPrice);
                
                // Process Officer field (if provided, split by semicolon)
                String officerField = columns[12];
                if (officerField != null && !officerField.isEmpty() && !officerField.equalsIgnoreCase("N/A")) {
                    String[] officerTokens = officerField.split(";");
                    for (String token : officerTokens) {
                        token = token.trim();
                        if (!token.isEmpty()) {
                            // Create a dummy HDBOfficer with default values (using token as name and NRIC)
                            HDBOfficer officer = new HDBOfficer(token, token, "password", 0, MaritalStatus.SINGLE);
                            project.addOfficer(officer);
                        }
                    }
                }
            }
            System.out.println("Projects loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing CSV file: " + e.getMessage());
        }
    }

    public void editProject(Project project, Map<FlatType, Integer> updatedUnits, int officerSlots) {
        for (FlatType type : updatedUnits.keySet()) {
            project.setUnitCount(type, updatedUnits.get(type));
        }
        project.setAvailableOfficerSlots(officerSlots);
    }

    public void deleteProject(Project project) {
        projects.remove(project);
    }

    public void toggleVisibility(Project project) {
        project.toggleVisibility();
    }

    public List<Project> filterVisibleProjects() {
        List<Project> visible = new ArrayList<>();
        for (Project p : projects) {
            if (p.isVisible()) {
                visible.add(p);
            }
        }
        return visible;
    }

    public void registerOfficer(HDBOfficer officer, Project project) {
        int remaining = project.getAvailableOfficerSlots();
        if (remaining > 0) {
            project.setAvailableOfficerSlots(remaining - 1);
            project.addOfficer(officer);
        }
    }

    public List<Project> getAllProjects() {
        return projects;
    }
}