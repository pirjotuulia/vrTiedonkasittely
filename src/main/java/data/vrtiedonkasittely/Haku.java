/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.vrtiedonkasittely;

import data.domain.connection.ConnectionSeeker;
import data.domain.train.JunaJson;
import data.domain.train.Train;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Administrator
 */
public class Haku {

    private JunaJson jj;
    private ConnectionSeeker cs;
    private DateTimeFormatter df;
    private Tiedonkasittely tk;

    public Haku(JunaJson jj, ConnectionSeeker cs, Tiedonkasittely tk) {
        this.jj = jj;
        this.cs = cs;
        this.tk = tk;
        this.df = DateTimeFormatter.ofPattern("dd.MM. HH.mm", new Locale("fi", "FI"));
    }

    public void suoraHaku(String departure, String arrival) {
        List<Train> junat = jj.lueJunienJSONData(departure, arrival);
        if (junat != null) {
            for (Train juna : junat) {
                LocalDateTime lahtoaika = juna.palautaPyydettyAikaAsemalta("DEPARTURE", departure);
                LocalDateTime saapumisaika = juna.palautaPyydettyAikaAsemalta("ARRIVAL", arrival);
                System.out.println("Lähtö: " + df.format(lahtoaika) + " " + departure + " Saapuminen " + arrival + " " + df.format(saapumisaika) + " " + juna);
            }
        } else {
            System.out.println("Suorahaku: Suoraa yhteyttä ei löytynyt välille " + departure + "-" + arrival + ".");
        }
    }

    public void vaihtohaku(String departure, String arrival) throws SQLException {
        List<String> directConsfromDeparture = cs.findConnectionsFrom(departure);
        List<String> directConsfromArrival = cs.findConnectionsFrom(arrival);
        List<String> commonGround = new ArrayList<>();
        for (String shcd : directConsfromDeparture) {
            if (directConsfromArrival.contains(shcd)) {
                commonGround.add(shcd);
            }
        }
        System.out.println("Common ground: ");
        commonGround.stream().forEach(System.out::println);
        if (!commonGround.isEmpty()) {//TODO: siilaa näitä jollain konstilla niin, että on jäljellä hallitusti vain yksi.
            suoraHaku(departure, commonGround.get(0));
            suoraHaku(commonGround.get(0), arrival);
        } else {
            System.out.println("Ei pääse yhdellä vaihdolla");
            List<Train> junat = new ArrayList<>();
            List<String> connections = cs.findConnection(departure, arrival, new ArrayList<String>());
            if (connections.isEmpty()) {
                System.out.println("Valitulle välille ei löydy junayhteyksiä.");
                return;
            } else {
                for (int i = 0; i < connections.size() - 1; i++) {
                    for (int j = connections.size() - 1; j > i; j--) {
                        if (cs.findDirectConnection(connections.get(i), connections.get(j))) {
                            junat = jj.lueJunienJSONData(connections.get(i), connections.get(j));
                            String lahtoasema = connections.get(i);
                            String saapumisasema = connections.get(j);
                            System.out.println("Lähtö " + lahtoasema);
                            for (Train juna : junat) {
                                LocalDateTime lahtoaika = juna.palautaPyydettyAikaAsemalta("DEPARTURE", lahtoasema);
                                LocalDateTime saapumisaika = juna.palautaPyydettyAikaAsemalta("ARRIVAL", saapumisasema);
                                System.out.println("Lähtö: " + df.format(lahtoaika) + " " + lahtoasema + " Saapuminen " + saapumisasema + " " + df.format(saapumisaika) + " " + juna);
                            }
                            i = j - 1;
                            break;
                        }
                    }
                }
                if (junat == null) {
                    System.out.println("Valitulle välille ei löydy junayhteyksiä.");
                    return;
                }
            }
        }

    }
}

//    private void lisaaYhteysTietokantaan(String departure, String arrival) {//Käytettiin suorien yhteyksien tietokannan luomiseen.
//        try (Connection con
//                = DriverManager.getConnection("jdbc:mysql://localhost:3306/yhteydet?useSSL=false&serverTimezone=UTC",
//                        "root", "salasana")) {
//            System.out.println("Connection saatu");
//            String depId = "SELECT id FROM station WHERE stationShortCode=?";
//            PreparedStatement stmtDep = con.prepareStatement(depId);
//            stmtDep.setString(1, departure);
//            ResultSet rs = stmtDep.executeQuery();
//            if (rs.next()) {
//                departure = rs.getString("id");
//            }
//            String arrId = "SELECT id FROM station WHERE stationShortCode=?";
//            PreparedStatement stmtArr = con.prepareStatement(arrId);
//            stmtDep.setString(1, arrival);
//            rs = stmtDep.executeQuery();
//            if (rs.next()) {
//                arrival = rs.getString("id");
//            }
//            String sql = "INSERT INTO directConnection (paa, hanta) VALUES (?, ?)";
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setString(1, departure);
//            stmt.setString(2, arrival);
//            System.out.println(stmt.executeUpdate() + " lisätty yhteys välille " + departure + "-" + arrival);
//        } catch (SQLException ex) {
//            System.out.println("Tämä");
//        }
//    }

