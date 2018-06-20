/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.vrtiedonkasittely;

/**
 *
 * @author Administrator
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
    
    
}
