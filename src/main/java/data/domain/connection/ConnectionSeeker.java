/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.connection;

import data.domain.connection.database.Dcd;
import data.domain.station.Asema;
import data.domain.station.Station;
import data.domain.station.Stations;
import data.domain.train.JunaJson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Administrator
 */
public class ConnectionSeeker {

    private List<Asema> risteysasemat;
    private Set<Asema> asemat;
    private Map<String, List<String>> radatRisteysasemilla;
    private Stations stations;
    private Distance dist;
    private JunaJson jj;
    private Dcd dcd;

    public ConnectionSeeker(List<Asema> risteysasemat, Set<Asema> asemat, Stations stations, JunaJson jj) {
        this.risteysasemat = risteysasemat;
        this.asemat = asemat;
        this.stations = stations;
        radatMappiin();
        this.dist = new Distance();
        this.jj = jj;
        this.dcd = new Dcd();
    }

    public ConnectionSeeker() {

    }

    public boolean findDirectConnection(String departure, String arrival) throws SQLException {
        try (Connection con = connect()) {
            int departureId = idFinder(departure);
            int arrivalId = idFinder(arrival);
            String connectionsql = "SELECT paa, hanta FROM directconnection WHERE paa=? AND hanta=? OR hanta=? AND paa=?";
            PreparedStatement stmt = con.prepareStatement(connectionsql);
            stmt = con.prepareStatement(connectionsql);
            stmt.setInt(1, departureId);
            stmt.setInt(2, arrivalId);
            stmt.setInt(3, departureId);
            stmt.setInt(4, arrivalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        return false;
    }

    public List<String> findConnectionsFrom(String shortcode) throws SQLException {
        List<String> connectedShortcodes = new ArrayList<>();
        int id = idFinder(shortcode);
        try (Connection con = connect()) {
            String connectionsql = "SELECT paa, hanta FROM directconnection WHERE paa=? OR hanta=?";
            PreparedStatement stmt = con.prepareStatement(connectionsql);
            stmt = con.prepareStatement(connectionsql);
            stmt.setInt(1, id);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("paa") == id) {
                    connectedShortcodes.add(shortCodeFinder(rs.getInt("hanta")));
                } else {
                    connectedShortcodes.add(shortCodeFinder(rs.getInt("paa")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        return connectedShortcodes;
    }

    public int idFinder(String shortcode) throws SQLException {
        int id = 0;
        try (Connection con = connect()) {
            String stationsql = "SELECT id, stationName FROM station WHERE stationShortcode=?";
            PreparedStatement stmt = con.prepareStatement(stationsql);
            stmt.setString(1, shortcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            return id;
        }
    }

    public String shortCodeFinder(int id) throws SQLException {
        String shortcode = "";
        try (Connection con = connect()) {
            String stationsql = "SELECT id, stationShortCode FROM station WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(stationsql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                shortcode = rs.getString("stationShortCode");
            }
        }
        return shortcode;
    }

    public List<String> findConnection(String departure, String arrival, List<String> exchangeStations) throws SQLException {
        if (findDirectConnection(departure, arrival)) {
            exchangeStations.add(arrival);
            return exchangeStations;
        } else if (exchangeStations.isEmpty()) {
            exchangeStations.add(departure);
        }
        List<String> fromDep = findConnectionsFrom(departure);//etsitään yhteyksiä lähto-
        List<String> fromArr = findConnectionsFrom(arrival);//ja saapumisasemilta
        List<String> commonGround = new ArrayList<>();
        for (String shcd : fromDep) {//jos näistä löytyy yhteisiä, lisätään listaan
            if (fromArr.contains(shcd)) {
                commonGround.add(shcd);
            }
        }
        System.out.println("Common ground: ");
        commonGround.stream().forEach(System.out::println);
        if (commonGround.isEmpty()) {//jos yhteyksiä ei löytynyt, 
            for (String arr : fromArr) {//etsitään suoria yhteyksiä lähtö- ja saapmisasemien yhteyksien välillä
                for (String dep : fromDep) {
                    if (!arr.equals(dep) && findDirectConnection(dep, arr)) {
                        commonGround.add(dep);//TODO: muu rajoite kuin se, mitä löytää ensimmäisenä
                        break;
                    }
                }
            }
            System.out.println("Common ground: ");
            commonGround.stream().forEach(System.out::println);
        }
        if (commonGround.isEmpty()) {
            if (findDirectConnection(departure, "HKI")) {
                commonGround.add("HKI");
            } else {
                commonGround.add(fromDep.get(0));
            }
        }
        exchangeStations.add(commonGround.get(0));
        departure = exchangeStations.get(exchangeStations.size() - 1);
        return findConnection(departure, arrival, exchangeStations);
    }
    //TÄSSÄ ETÄISYYTEEN PERUSTUVAN VALINNAN PROTOTYYPPI
//            double initialDistance = dist.calculateDistance(departureStation.getLatitude(), departureStation.getLongitude(), arrivalStation.getLatitude(), arrivalStation.getLongitude());
    //            double pienin = initialDistance;
    //        while (mahdollisetRisteysasemat.size() > 1) {
    //            Iterator<String> it = mahdollisetRisteysasemat.iterator();
    //            while (it.hasNext() && mahdollisetRisteysasemat.size() > 1) {
    //                String shortcode = it.next();
    //                Station st = stations.getStation(shortcode);
    //                double distance = dist.calculateDistance(st.getLatitude(), st.getLongitude(), arrivalStation.getLatitude(), arrivalStation.getLongitude());
    //                if (distance > pienin) {
    //                    it.remove();
    //                } else {
    //                    pienin = distance;
    //                }
    //            }
    //        }

//    public String findConnection(String departure, String arrival, String path) throws SQLException {
//        if (findDirectConnection(departure, arrival) || path.endsWith(arrival)) {
//            path = path + "," + arrival;
//            return path;
//        }
//        Station departureStation = this.stations.getStation(departure);
//        Station arrivalStation = this.stations.getStation(arrival);
//        double initialDistance = dist.calculateDistance(departureStation.getLatitude(), departureStation.getLongitude(), arrivalStation.getLatitude(), arrivalStation.getLongitude());
//        Set<String> lahtoradat = haeRadat(departure);
//        List<String> mahdollisetRisteysasemat = new ArrayList<>();
//        for (String r : lahtoradat) {
//            mahdollisetRisteysasemat.addAll(radatRisteysasemilla.get(r));
//        }
//        mahdollisetRisteysasemat = mahdollisetRisteysasemat.stream().distinct().collect(Collectors.toList());
//        if (mahdollisetRisteysasemat.contains(departure)) {
//            mahdollisetRisteysasemat.remove(departure);
//        }
//        double pienin = initialDistance;
//        while (mahdollisetRisteysasemat.size() > 1) {
//            Iterator<String> it = mahdollisetRisteysasemat.iterator();
//            while (it.hasNext() && mahdollisetRisteysasemat.size() > 1) {
//                String shortcode = it.next();
//                Station st = stations.getStation(shortcode);
//                double distance = dist.calculateDistance(st.getLatitude(), st.getLongitude(), arrivalStation.getLatitude(), arrivalStation.getLongitude());
//                if (distance > pienin) {
//                    it.remove();
//                } else {
//                    pienin = distance;
//                }
//            }
//        }
//        path = path + "," + mahdollisetRisteysasemat.get(0);
//
//        return findConnection(mahdollisetRisteysasemat.get(0), arrival, path);
//
////        List<String> connections = new ArrayList<>();
////        while (connections.isEmpty()) {
////            Set<String> lahtoradat = haeRadat(departure);
////            Set<String> saapumisradat = haeRadat(arrival);
////            for (Asema a : risteysasemat) {
////                for (String l : lahtoradat) {
////                    for (String s : saapumisradat) {
////                        if (a.getTrack().contains(l) || a.getTrack().contains(s)) {
////                            connections.add(a.getShortCode());
////                        }
////                    }
////                }
////            }
////        }
////        return connections;
//    }
    private Set<String> haeRadat(String shortCode) {
        for (Asema a : asemat) {
            if (a.getShortCode().equals(shortCode)) {
                return a.getTrack();
            }
        }
        return null;
    }

    private void radatMappiin() {
        this.radatRisteysasemilla = new HashMap<>();
        for (Asema a : this.risteysasemat) {
            a.getTrack().stream().forEach(r -> {
                this.radatRisteysasemilla.putIfAbsent(r, new ArrayList<>());
                this.radatRisteysasemilla.get(r).add(a.getShortCode());
            });
        }
    }

    private Connection connect() {
        return dcd.connect();
    }
}
