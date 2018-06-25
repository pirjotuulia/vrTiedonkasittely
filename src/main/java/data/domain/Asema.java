package data.domain;

import java.util.Objects;

/**
 *
 * @author Pirjo
 */
public class Asema {
    private String station;
    private String track;
    
    public Asema(String station, String track) {
        this.station = station;
        this.track = track;
    }

    @Override
    public String toString() {
        return "Asema: " + this.station + ", rata: " + this.track;
    }

    public String getStation() {
        return station;
    }

    public String getTrack() {
        return track;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.station);
        hash = 31 * hash + Objects.hashCode(this.track);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asema other = (Asema) obj;
        if (!Objects.equals(this.station, other.station)) {
            return false;
        }
        if (!Objects.equals(this.track, other.track)) {
            return false;
        }
        return true;
    }
    
    
}
