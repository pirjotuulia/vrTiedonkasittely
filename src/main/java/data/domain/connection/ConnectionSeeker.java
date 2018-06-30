/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.connection;

import data.domain.station.Asema;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class ConnectionSeeker {
    private List<Asema> risteysasemat;
    private Set<Asema> asemat;
    
    public ConnectionSeeker(List<Asema> risteysasemat, Set<Asema> asemat) {
        this.risteysasemat = risteysasemat;
        this.asemat = asemat;
    }

    public List<String> findConnection(String departure, String arrival) {
        List<String> connections = new ArrayList<>();
        Set<String> lahtoradat = haeRadat(departure);
        Set<String> saapumisradat = haeRadat(arrival);
        for (Asema a : risteysasemat) {
            for (String l : lahtoradat) {
                for (String s : saapumisradat ) {
                    if (a.getTrack().contains(l)&&a.getTrack().contains(s)) {
                        connections.add(a.getStation());
                    }
                }
            }
        }
        if (departure!="TPE"&&arrival!="TPE"&&connections.isEmpty()) {
            connections.add("TPE");
        }
        return connections;
    }

    private Set<String> haeRadat(String departure) {
        for (Asema a : asemat) {
            if (a.getStation().equals(departure)) {
                return a.getTrack();
            }
        }
        return null;
    }
}
