/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Stations {
    private Map<String, Station> stations;
    
    public Stations() {
        this.stations = new HashMap<>();
    }
    
    public void addStations(List<Station> stationlist) {
        for (Station st : stationlist) {
            addStation(st);
        }
    }
    
    public void addStation(Station station) {
        this.stations.put(station.getStationShortCode(), station);
    }
    
    public Station getStation(String shortcode) {
        return this.stations.get(shortcode);
    }
    
    public Map<String, Station> palautaAsemaMappi() {
        return this.stations;
    }
    
}
