package utils;

public class FilterSettings {
    private String neighborhood;      // Filter by project neighborhood
    private Integer projectId;        // Filter by a specific project ID
    private Double minPrice2Room;     // Minimum price for 2‑Room units
    private Double maxPrice2Room;     // Maximum price for 2‑Room units
    private Double minPrice3Room;     // Minimum price for 3‑Room units
    private Double maxPrice3Room;     // Maximum price for 3‑Room units
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

    public Double getMinPrice2Room() {
        return minPrice2Room;
    }

    public void setMinPrice2Room(Double minPrice2Room) {
        this.minPrice2Room = minPrice2Room;
    }

    public Double getMaxPrice2Room() {
        return maxPrice2Room;
    }

    public void setMaxPrice2Room(Double maxPrice2Room) {
        this.maxPrice2Room = maxPrice2Room;
    }

    public Double getMinPrice3Room() {
        return minPrice3Room;
    }

    public void setMinPrice3Room(Double minPrice3Room) {
        this.minPrice3Room = minPrice3Room;
    }

    public Double getMaxPrice3Room() {
        return maxPrice3Room;
    }

    public void setMaxPrice3Room(Double maxPrice3Room) {
        this.maxPrice3Room = maxPrice3Room;
    }

    public boolean isSortAlphabetical() {
        return sortAlphabetical;
    }

    public void setSortAlphabetical(boolean sortAlphabetical) {
        this.sortAlphabetical = sortAlphabetical;
    }
}