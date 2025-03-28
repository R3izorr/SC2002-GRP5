package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.FlatType;

public class Project {
    private String name;
    private String location; // Represents Neighborhood
    private Map<FlatType, Integer> unitCount;
    private Map<FlatType, Double> sellingPrice;
    private LocalDate applicationOpenDate;
    private LocalDate applicationCloseDate;
    private boolean visibility;
    private HDBManager manager;
    private int availableOfficerSlots;
    private List<HDBOfficer> officers; // Changed from a single officer to a list

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
        this.sellingPrice = new HashMap<>();
        this.officers = new ArrayList<>();
        // Initialize all flat types with default values
        for (FlatType type : FlatType.values()) {
            unitCount.put(type, 0);
            sellingPrice.put(type, 0.0);
        }
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDate getApplicationOpenDate() { return applicationOpenDate; }
    public LocalDate getApplicationCloseDate() { return applicationCloseDate; }
    public boolean isVisible() { return visibility; }
    public void toggleVisibility() { visibility = !visibility; }
    public HDBManager getManager() { return manager; }
    
    public int getAvailableOfficerSlots() { return availableOfficerSlots; }
    public void setAvailableOfficerSlots(int slots) { this.availableOfficerSlots = slots; }

    public Map<FlatType, Integer> getUnitCount() { return unitCount; }
    public void setUnitCount(FlatType type, int count) { unitCount.put(type, count); }
    
    public Map<FlatType, Double> getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(FlatType type, double price) { sellingPrice.put(type, price); }
    
    public List<HDBOfficer> getOfficers() { return officers; }
    public void setOfficers(List<HDBOfficer> officers) { this.officers = officers; }
    public void addOfficer(HDBOfficer officer) { this.officers.add(officer); }
    
    public String getDetails() {
        String type1 = FlatType.TWO_ROOM.toString();
        String type2 = FlatType.THREE_ROOM.toString();
        String officerStr;
        if (officers.isEmpty()) {
            officerStr = "N/A";
        } else {
            StringBuilder sb = new StringBuilder();
            for (HDBOfficer officer : officers) {
                sb.append(officer.toString()).append("; ");
            }
            officerStr = sb.toString();
        }
        return String.format("Project Name: %s, Neighborhood: %s, %s: Units=%d, Price=%.2f, %s: Units=%d, Price=%.2f, Open: %s, Close: %s, Manager: %s, Officer Slot: %d, Officers: %s",
                name,
                location,
                type1,
                unitCount.get(FlatType.TWO_ROOM),
                sellingPrice.get(FlatType.TWO_ROOM),
                type2,
                unitCount.get(FlatType.THREE_ROOM),
                sellingPrice.get(FlatType.THREE_ROOM),
                applicationOpenDate.toString(),
                applicationCloseDate.toString(),
                (manager != null ? manager.toString() : "N/A"),
                availableOfficerSlots,
                officerStr
        );
    }
}