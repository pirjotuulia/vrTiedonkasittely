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
    private String shortCode;
    private Set<String> tracks;
    private String name;
    
    public Asema(String shortCode) {
        this.shortCode = shortCode;
        this.tracks = new HashSet<>();
        this.name = "";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("(").append(this.shortCode).append(")");
        this.tracks.stream().forEach(r -> sb.append(", ").append(r));
        return sb.toString();
    }

    public String getShortCode() {
        return shortCode;
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
        hash = 31 * hash + Objects.hashCode(this.shortCode);
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
        if (!Objects.equals(this.shortCode, other.shortCode)) {
            return false;
        }
        return true;
    }
    
    
}
