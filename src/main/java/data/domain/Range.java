package data.domain;

import data.domain.EndLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Pirjo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Range {

    private int id;
    private StartLocation startLocation;
    private EndLocation endLocation;

    public StartLocation getStartLocation() {
        return this.startLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    @Override
    public String toString() {
        return "Range{" + "id=" + id + ", startLocation=" + startLocation + ", endLocation=" + endLocation + '}';
    }

}
