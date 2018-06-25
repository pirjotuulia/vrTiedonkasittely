package data.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Pirjo
 */

public class Raideosuus {
    private int id;
    private String station;
    private String trackSectionCode;
    private List<Range> ranges;

    @Override
    public String toString() {
        return "Raideosuus{" + "id=" + id + ", station=" + station + ", trackSectionCode=" + trackSectionCode + ", ranges=" + ranges + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTrackSectionCode() {
        return trackSectionCode;
    }

    public void setTrackSectionCode(String trackSectionCode) {
        this.trackSectionCode = trackSectionCode;
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }
    
    
    
    
}
