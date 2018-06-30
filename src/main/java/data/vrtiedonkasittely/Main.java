/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.domain.station.Asema;
import data.vrtiedonkasittely.Tiedonkasittely;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Pirjo/Osittain Tommin antaman koodin pohjalta
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Tiedonkasittely tk = new Tiedonkasittely();
        tk.lueRaideosuuksienJSONData();
        tk.lueAsemienJSONData();
        tk.matkustajaAsematLyhenteineen();
        tk.irrotaRatatiedot();
        tk.lisaaAsemienNimet();
        tk.selvitäRisteysasemat();
        tk.asematJsoniksi();
        tk.risteysAsematJsoniksi();

        Set<String> risteysasemienRadat = tk.risteysasemienRadatSettiin();
        Set<String> kaikkiRadat = tk.radatSettiin();
        System.out.println("Risteysradat" + risteysasemienRadat.toString());
        System.out.println("Kaikki radat" + kaikkiRadat.toString());
        for (String rata : tk.radatSettiin()) {
            List<String> paikat = tk.etsiRadanVarrellaOlevatAsemat(rata);
            if (paikat.size() > 0) {
                System.out.println("Rata " + rata + ": " + paikat);
            }
        }
    }
}
