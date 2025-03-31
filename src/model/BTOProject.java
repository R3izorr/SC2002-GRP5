package model;

import java.util.Date;
import java.util.List;


public class BTOProject {
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

    private boolean isVisible;
    public BTOProject(String projectName, String neighborhood,float sellingPrice2Room ,int units2Room, float sellingPrice3Room, int units3Room,
                  Date applicationOpen, Date applicationClose, HDBManager manager, int officerSlots, boolean isVisible) {
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

    public void setOfficers(HDBOfficer officer) {
        this.officers.add(officer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Project Name: ").append(projectName).append("\n")
          .append("Neighborhood: ").append(neighborhood).append("\n")
          .append("2-Room Units: ").append(units2Room).append("\n")
          .append("3-Room Units: ").append(units3Room).append("\n")
          .append("Selling Price for 2-Room: ").append(sellingPrice2Room).append("\n")
          .append("Selling Price for 3-Room: ").append(sellingPrice3Room).append("\n")
          .append("Application Open Date: ").append(applicationOpen).append("\n")
          .append("Application Close Date: ").append(applicationClose).append("\n")
          .append("Manager: ").append(manager != null ? manager.getNric() : "None").append("\n")
          .append("Officer Slots: ").append(officerSlots).append("\n")
            .append("Officers: ").append(officers != null ? officers.size() : 0).append("\n")
          .append("Is Visible: ").append(isVisible).append("\n");
        return sb.toString();
    }
}
