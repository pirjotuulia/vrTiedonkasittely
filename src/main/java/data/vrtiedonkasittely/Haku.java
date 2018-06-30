/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.vrtiedonkasittely;

import data.domain.connection.ConnectionSeeker;
import data.domain.train.JunaJson;
import data.domain.train.Train;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Haku {

    private JunaJson jj;
    private ConnectionSeeker cs;

    public Haku(JunaJson jj, ConnectionSeeker cs) {
        this.jj = jj;
        this.cs = cs;
    }

    public void junaHaku(String departure, String arrival) {
        List<Train> junat = jj.lueJunienJSONData(departure, arrival);
        List<String> connections = new ArrayList<>();
        String connection = "";
        List<Train> junat2 = new ArrayList<>();
        if (junat == null) {
            connections = cs.findConnection(departure, arrival);
            if (connections.isEmpty()) {
                System.out.println("Valitulle välille ei löydy junayhteyksiä.");
                return;
            } else {
                for (String c : connections) {
                    junat = jj.lueJunienJSONData(departure, c);
                    junat2 = jj.lueJunienJSONData(c, arrival);
                    if (!junat.isEmpty()&&!junat2.isEmpty()) {
                        connection = c;
                        break;
                    }
                }
            }
        }
        System.out.println("Lähtöasema " + departure);
        junat.stream().forEach(System.out::println);
        if (!junat2.isEmpty()) {
            System.out.println("Vaihtoasema " + connection);
            junat2.stream().forEach(System.out::println);
        }
        System.out.println("Pääteasema " + arrival);
    }
}
