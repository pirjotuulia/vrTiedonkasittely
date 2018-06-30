/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.domain.connection.ConnectionSeeker;
import data.domain.station.Asema;
import data.domain.train.JunaJson;
import data.domain.train.Train;
import data.vrtiedonkasittely.Haku;
import data.vrtiedonkasittely.Tiedonkasittely;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Pirjo/Osittain Tommin antaman koodin pohjalta
 */
public class Main {

    private static Tiedonkasittely tk;
    private static JunaJson jj;
    private static ConnectionSeeker cs;
    private static Haku haku;

    public static void main(String[] args) throws IOException {

        alustaJunahaku();
        kaynnistaJunahaku();
    }

    private static void alustaJunahaku() {
        tk = new Tiedonkasittely();
        jj = new JunaJson();
        cs = new ConnectionSeeker(tk.listaaRisteysasemat(), tk.listaaAsemat());
        haku = new Haku(jj, cs);
    }

    private static void kaynnistaJunahaku() {
        Scanner lukija = new Scanner(System.in);
        while (true) {
            System.out.print("Anna lähtöasema: ");
            String departure = lukija.nextLine();
            System.out.print("Anna pääteasema: ");
            String arrival = lukija.nextLine();
            haku.junaHaku(departure, arrival);
        }
    }
}
