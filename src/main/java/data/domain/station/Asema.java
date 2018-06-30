package data.domain.station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Pirjo
 */
public class Asema {
    private String station;
    private Set<String> tracks;
    private String name;
    
    public Asema(String station) {
        this.station = station;
        this.tracks = new HashSet<>();
        this.name = "";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("(").append(this.station).append(")");
        this.tracks.stream().forEach(r -> sb.append(", ").append(r));
        return sb.toString();
    }

    public String getStation() {
        return station;
    }

    public Set<String> getTrack() {
        return tracks;
    }
    
    public void addTrack(String track) {
        this.tracks.add(track);
    }
    
    public void removeTrack(String track) {
        this.tracks.remove(track);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.station);
        hash = 31 * hash + Objects.hashCode(this.tracks);
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
        return true;
    }
    
    
}
