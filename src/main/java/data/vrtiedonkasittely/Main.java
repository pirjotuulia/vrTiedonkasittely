/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.domain.connection.ConnectionSeeker;
import data.domain.connection.database.Dcd;
import data.domain.station.Asema;
import data.domain.station.Station;
import data.domain.train.JunaJson;
import data.domain.train.Train;
import data.vrtiedonkasittely.Haku;
import data.vrtiedonkasittely.Tiedonkasittely;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pirjo/Osittain Tommin antaman koodin pohjalta
 */
public class Main {

    private static Tiedonkasittely tk;
    private static JunaJson jj;
    private static ConnectionSeeker cs;
    private static Haku haku;

    public static void main(String[] args) throws IOException, SQLException {
        alustaJunahaku();
        kaynnistaJunahaku();
    }

    private static void alustaJunahaku() {
        tk = new Tiedonkasittely();//alustetaan paljon taustadataa, josta ehkä kaikkea ei enää käytetä.
        jj = new JunaJson();//alustetaan hakutoiminnallisuus apista
        cs = new ConnectionSeeker(tk.listaaRisteysasemat(), tk.listaaAsemat(), tk.annaAsematMapissa(), jj);//TODO: refaktoroi
        haku = new Haku(jj, cs, tk);
    }

    private static void kaynnistaJunahaku() throws SQLException {
        Scanner lukija = new Scanner(System.in);
        while (true) {
            System.out.print("Anna lähtöasema: ");
            String departure = lukija.nextLine();
            System.out.print("Anna pääteasema: ");
            String arrival = lukija.nextLine();
            if (cs.findDirectConnection(departure, arrival)) {
                haku.suoraHaku(departure, arrival);
            } else {
                haku.vaihtohaku(departure, arrival);
            }

        }
    }

//    private static void kaynnistaJunahaku() throws SQLException {//käytettiin tietokannan luomisessa
//        List<String> shortcodet = shortcodet();
//        int indeksi= shortcodet.indexOf("MI");
//        for (int i = indeksi; i < shortcodet.size() - 1; i++) {
//            for (int j = i + 1; j < shortcodet.size(); j++) {
//                if (!shortcodet.get(i).equals(shortcodet.get(j))) {
//                    haku.junaHaku(shortcodet.get(i), shortcodet.get(j));
//                }
//            }
//        }
//    }
}
