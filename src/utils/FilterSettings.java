package utils;

public class FilterSettings {
    private String neighborhood;      // Filter by project neighborhood
    private Integer projectId;        // Filter by a specific project ID
    private Float minPrice2Room;     // Minimum price for 2‑Room units
    private Float maxPrice2Room;     // Maximum price for 2‑Room units
    private Float minPrice3Room;     // Minimum price for 3‑Room units
    private Float maxPrice3Room;     // Maximum price for 3‑Room units
    private boolean sortAlphabetical; // Default true (sort by project name)

    public FilterSettings() {
        this.neighborhood = null;
        this.projectId = null;
        this.minPrice2Room = null;
        this.maxPrice2Room = null;
        this.minPrice3Room = null;
        this.maxPrice3Room = null;
        this.sortAlphabetical = true;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Float getMinPrice2Room() {
        return minPrice2Room;
    }

    public void setMinPrice2Room(Float minPrice2Room) {
        this.minPrice2Room = minPrice2Room;
    }

    public Float getMaxPrice2Room() {
        return maxPrice2Room;
    }

    public void setMaxPrice2Room(Float maxPrice2Room) {
        this.maxPrice2Room = maxPrice2Room;
    }

    public Float getMinPrice3Room() {
        return minPrice3Room;
    }

    public void setMinPrice3Room(Float minPrice3Room) {
        this.minPrice3Room = minPrice3Room;
    }

    public Float getMaxPrice3Room() {
        return maxPrice3Room;
    }

    public void setMaxPrice3Room(Float maxPrice3Room) {
        this.maxPrice3Room = maxPrice3Room;
    }

    public boolean isSortAlphabetical() {
        return sortAlphabetical;
    }

    public void setSortAlphabetical(boolean sortAlphabetical) {
        this.sortAlphabetical = sortAlphabetical;
    }
}