/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.domain.Asema;
import data.domain.Risteysasema;
import data.vrtiedonkasittely.Tiedonkasittely;
import java.io.IOException;
import java.util.Map;
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
        tk.risteysAsematJsoniksi();
        tk.irrotaRatatiedot();
        tk.selvitäRisteysasemat();
        tk.lisaaAsemienNimet();
        tk.risteysAsematJsoniksi();
        tk.asematJsoniksi();
        Set<String> risteysasemienRadat = tk.risteysasemienRadatSettiin();
        Set<String> kaikkiRadat = tk.radatSettiin();
        Map<String, String> asematLyhenteineen = tk.matkustajaAsematLyhenteineen();
        System.out.println("Risteysradat" + risteysasemienRadat.toString());
        System.out.println("Kaikki radat" + kaikkiRadat.toString());
        System.out.println(asematLyhenteineen.toString());
    }
}
