package utils;

public class FilterSettings {
    private String neighborhood; // filter by location (neighborhood)
    private String flatType;     // filter by flat type ("2-Room" or "3-Room")
    private boolean sortAlphabetical; // default true

    public FilterSettings() {
        this.neighborhood = null;
        this.flatType = null;
        this.sortAlphabetical = true;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public boolean isSortAlphabetical() {
        return sortAlphabetical;
    }

    public void setSortAlphabetical(boolean sortAlphabetical) {
        this.sortAlphabetical = sortAlphabetical;
    }
}