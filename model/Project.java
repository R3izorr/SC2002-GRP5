package model;

import utils.FlatType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import model.HDBManager;


public class Project {
    private String name;
    private String location;
    private Map<FlatType, Integer> unitCount;
    private LocalDate applicationOpenDate;
    private LocalDate applicationCloseDate;
    private boolean visibility;
    private HDBManager manager;
    private int availableOfficerSlots;
    private Queue<HDBOfficer> pendingOfficers = new LinkedList<>();
    


public Project(String name, String location, Map<FlatType, Integer> unitCount,
               LocalDate applicationOpenDate, LocalDate applicationCloseDate) {
    this.name = name;
    this.location = location;
    this.unitCount = unitCount;
    this.applicationOpenDate = applicationOpenDate;
    this.applicationCloseDate = applicationCloseDate;
    this.visibility = false; // default visibility
}

    


    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean isVisible() { return visibility; }
    public void setVisible(boolean visible) { this.visibility = visible; }
    public LocalDate getApplicationOpenDate() { return applicationOpenDate; }
    public LocalDate getApplicationCloseDate() { return applicationCloseDate; }
    public int getAvailableOfficerSlots() { return availableOfficerSlots; }
    public void setAvailableOfficerSlots(int slots) { this.availableOfficerSlots = slots; }
    public Map<FlatType, Integer> getUnitCount() { return unitCount; }
    public void setUnitCount(FlatType type, int count) { unitCount.put(type, count); }

    public String getDetails() {
        return String.format("Project: %s, Location: %s, 2-Room: %d, 3-Room: %d",
                name, location,
                unitCount.get(FlatType.TWO_ROOM),
                unitCount.get(FlatType.THREE_ROOM));
    }
    public void addPendingOfficer(HDBOfficer officer) {
        pendingOfficers.add(officer);
    }
    public void setManager(HDBManager manager) {
        this.manager = manager;
    }
    
    public HDBManager getManager() {
        return manager;
    }
    public HDBOfficer getPendingOfficer() {
        return pendingOfficers.poll(); // gets and removes first pending officer
    }

}
