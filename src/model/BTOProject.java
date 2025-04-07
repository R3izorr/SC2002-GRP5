package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.user.HDBManager;
import model.user.HDBOfficer;


public class BTOProject {
    private static int counter = 1;
    private int projectId;
    private String projectName; 
    private String neighborhood; 
    private int units2Room; 
    private int units3Room; 
    private float sellingPrice2Room;
    private float sellingPrice3Room;
    private Date applicationOpen; 
    private Date applicationClose; 
    private HDBManager manager; 
    private int officerSlots; 
    private List<HDBOfficer> officers = new java.util.ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private boolean isVisible;
    public BTOProject(String projectName, String neighborhood,float sellingPrice2Room ,int units2Room, float sellingPrice3Room, int units3Room,
                  Date applicationOpen, Date applicationClose, HDBManager manager, int officerSlots, boolean isVisible) {
        this.projectId = counter++;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.units2Room = units2Room;
        this.sellingPrice2Room = sellingPrice2Room;
        this.sellingPrice3Room = sellingPrice3Room;
        this.units3Room = units3Room;
        this.applicationOpen = applicationOpen;
        this.applicationClose = applicationClose;
        this.manager = manager;
        this.officerSlots = officerSlots;
        this.isVisible = isVisible;
    }
    
    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getUnits2Room() {
        return units2Room;
    }

    public void setUnits2Room(int units2Room) {
        this.units2Room = units2Room;
    }

    public int getUnits3Room() {
        return units3Room;
    }

    public void setUnits3Room(int units3Room) {
        this.units3Room = units3Room;
    }

    public Date getApplicationOpen() {
        return applicationOpen;
    }

    public void setApplicationOpen(Date applicationOpen) {
        this.applicationOpen = applicationOpen;
    }

    public Date getApplicationClose() {
        return applicationClose;
    }

    public void setApplicationClose(Date applicationClose) {
        this.applicationClose = applicationClose;
    }

    public HDBManager getManager() {
        return manager;
    }

    public void setManager(HDBManager manager) {
        this.manager = manager;
    }

    public int getOfficerSlots() {
        return officerSlots;
    }

    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public float getSellingPrice2Room() {
        return sellingPrice2Room;
    }

    public void setSellingPrice2Room(float sellingPrice2Room) {
        this.sellingPrice2Room = sellingPrice2Room;
    }

    public float getSellingPrice3Room() {
        return sellingPrice3Room;
    }

    public void setSellingPrice3Room(float sellingPrice3Room) {
        this.sellingPrice3Room = sellingPrice3Room;
    }
    public List<HDBOfficer> getOfficers() {
        return officers;
    }

    public void addOfficers(HDBOfficer officer) {
        this.officers.add(officer);
        }

    public void clearAssignedOfficers() {
        this.officers.clear();
    }

    public String getOfficerNRIC() {
        return officers.isEmpty() ? "None" : "\"" + officers.stream().map(HDBOfficer::getNric).reduce((a, b) -> a + ";" + b).orElse("") + "\"";
    }

    public String getOfficerName() {
        return officers.isEmpty() ? "None" : officers.stream().map(HDBOfficer::getName).reduce((a, b) -> a + ", " + b).orElse("None");
    }
    
    public String toStringForApplicant() {
        return 
               " Project ID: " + projectId + "\n" +
               " Project Name: " + projectName + "\n" +
               " Neighborhood: " + neighborhood + "\n" +
               " 2-Room Units: " + units2Room + "\n" +
               " 3-Room Units: " + units3Room + "\n" +
               " 2-Room Price: $" + sellingPrice2Room + "\n" +
               " 3-Room Price: $" + sellingPrice3Room + "\n" +
               " Application Open: " + dateFormat.format(applicationOpen) + "\n" +
               " Application Close: " + dateFormat.format(applicationClose) + "\n" +
               "----------------------------------------";
        }
        
    // For Manager/Officer: full details
    public String toStringForManager() {
        return 
               " Project ID: " + projectId + "\n" +
               " Project Name: " + projectName + "\n" +
               " Neighborhood: " + neighborhood + "\n" +
               " 2-Room Units: " + units2Room + "\n" +
               " 3-Room Units: " + units3Room + "\n" +
               " 2-Room Price: $" + sellingPrice2Room + "\n" +
               " 3-Room Price: $" + sellingPrice3Room + "\n" +
               " Application Open: " + dateFormat.format(applicationOpen) + "\n" +	
               " Application Close: " + dateFormat.format(applicationClose) + "\n" +
               " Manager Name: " + (manager != null ? manager.getName() : "None") + "\n" +
               " Remaining Officer Slots: " + officerSlots + "\n" +
               " Officers List: " + this.getOfficerName() + "\n" +
               " Visibility: " + (isVisible ? "ON" : "OFF") + "\n" +
               "========================================";
        }
        
    public String toStringforOfficer() {
        return 
            " Project ID: " + projectId + "\n" +
            " Project Name: " + projectName + "\n" +
            " Neighborhood: " + neighborhood + "\n" +
            " 2-Room Units: " + units2Room + "\n" +
            " 3-Room Units: " + units3Room + "\n" +
            " 2-Room Price: $" + sellingPrice2Room + "\n" +
            " 3-Room Price: $" + sellingPrice3Room + "\n" +
            " Application Open: " + dateFormat.format(applicationOpen) + "\n" +	
            " Application Close: " + dateFormat.format(applicationClose) + "\n" +
            " Remaining Officer Slots: " + officerSlots + "\n" +
            " Officers List: " + this.getOfficerName() + "\n" +
            "========================================";

    }

    @Override
    public String toString() {
        return toStringForManager();
    }
}
