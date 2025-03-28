package model;

import utils.FlatType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Project {
    private String name;
    private String location;
    private Map<FlatType, Integer> unitCount;
    private LocalDate applicationOpenDate;
    private LocalDate applicationCloseDate;
    private boolean visibility;
    private HDBManager manager;
    private int availableOfficerSlots;

    public Project(String name, String location, LocalDate openDate, LocalDate closeDate,
                   HDBManager manager, int availableOfficerSlots) {
        this.name = name;
        this.location = location;
        this.applicationOpenDate = openDate;
        this.applicationCloseDate = closeDate;
        this.manager = manager;
        this.availableOfficerSlots = availableOfficerSlots;
        this.visibility = true;
        this.unitCount = new HashMap<>();
        for (FlatType type : FlatType.values()) {
            unitCount.put(type, 0);
        }
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDate getApplicationOpenDate() { return applicationOpenDate; }
    public LocalDate getApplicationCloseDate() { return applicationCloseDate; }
    public boolean isVisible() { return visibility; }
    public void toggleVisibility() { visibility = !visibility; }
    public HDBManager getManager() { return manager; }

    public Map<FlatType, Integer> getUnitCount() { return unitCount; }
    public void setUnitCount(FlatType type, int count) { unitCount.put(type, count); }
    public int getAvailableOfficerSlots() { return availableOfficerSlots; }
    public void setAvailableOfficerSlots(int slots) { this.availableOfficerSlots = slots; }

    public String getDetails() {
        return String.format("Project: %s, Location: %s, 2-Room: %d, 3-Room: %d",
                name, location,
                unitCount.get(FlatType.TWO_ROOM),
                unitCount.get(FlatType.THREE_ROOM));
    }
}
