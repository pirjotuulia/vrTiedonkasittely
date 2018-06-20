/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.google.gson.Gson;
import data.vrtiedonkasittely.Asema;
import data.vrtiedonkasittely.Risteysasema;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Administrator
 */
public class Main {

    private static Set<Asema> asemat = new HashSet<>();
    private static Map<String, Set<String>> risteysasemat = new HashMap<>();
    private static List<Risteysasema> risteyspaikat = new ArrayList<>();
    private static JSONArray risteysjson = new JSONArray();

    public static void main(String[] args) throws IOException {
        JSONParser parser = new JSONParser();
        JSONArray radat = new JSONArray();
        JSONArray asemienNimet = new JSONArray();
        try {
            radat = (JSONArray) parser.parse(IOUtils.toString(new URL("https://rata.digitraffic.fi/api/v1/metadata/track-sections"), Charset.forName("UTF-8")));
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            asemienNimet = (JSONArray) parser.parse(IOUtils.toString(new URL("https://rata.digitraffic.fi/api/v1/metadata/stations"), Charset.forName("UTF-8")));
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        irrotaRatatiedot(radat);
        selvitäRisteysasemat();
        lisaaAsemienNimet(asemienNimet);
        //       risteyspaikat.stream().forEach(System.out::println);
        System.out.println("Matkustajaliikenteen mahdollisia risteyspaikkoja " + risteyspaikat.size());
        risteysAsematJsoniksi();
        System.out.println(risteysjson);
        try {
            File risteysasemaJson = new File("risteysasemat.json");
            Files.newBufferedWriter(risteysasemaJson.toPath(), StandardCharsets.UTF_8).write(risteysjson.toString());
            System.out.println("kirjoitettu!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void irrotaRatatiedot(JSONArray json) {
        for (Object o : json) {
            JSONObject asemaTiedot = (JSONObject) o;
            String tunnus = (String) asemaTiedot.get("station");
            Object ranges = asemaTiedot.get("ranges");
            JSONArray r = (JSONArray) ranges;
            for (Object t : r) {
                JSONObject paikka = (JSONObject) t;
                JSONObject alku = (JSONObject) paikka.get("startLocation");
                String rata = (String) alku.get("track");
                while (rata.length() < 3) {
                    rata = "0" + rata;
                }
                Asema asema = new Asema(tunnus, rata);
                asemat.add(asema);
            }
        }
    }

    private static void selvitäRisteysasemat() {
        for (Asema as : asemat) {
            risteysasemat.putIfAbsent(as.getStation(), new HashSet());
            risteysasemat.get(as.getStation()).add(as.getTrack());
        }
        System.out.println("Risteysasemat ennen poistoa " + risteysasemat.size());
        Iterator it = risteysasemat.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (risteysasemat.get(key).size() == 1) {
                it.remove();
            }
        }
        System.out.println("Risteysasemat poiston jälkeen " + risteysasemat.size());
    }

    private static void lisaaAsemienNimet(JSONArray asemienNimet) {
        for (Object o : asemienNimet) {
            JSONObject asemaTiedot = (JSONObject) o;
            String tunnus = (String) asemaTiedot.get("stationShortCode");
            if (risteysasemat.containsKey(tunnus) && (boolean) asemaTiedot.get("passengerTraffic")) {
                String nimi = (String) asemaTiedot.get("stationName");
                Risteysasema rist = new Risteysasema(tunnus, nimi, risteysasemat.get(tunnus));
                risteyspaikat.add(rist);
            }
        }
    }

    private static void risteysAsematJsoniksi() {
        for (Risteysasema s : risteyspaikat) {
            JSONObject asema = new JSONObject();
            asema.put("stationShortCode", s.getTunnus());
            asema.put("stationName", s.getNimi());
            JSONArray rataluettelo = new JSONArray();
            Set<String> ratalista = s.getRadat();
            for (String r : ratalista) {
                JSONObject ratanumero = new JSONObject();
                ratanumero.put("track", r);
                rataluettelo.add(ratanumero);
            }
            asema.put("tracks", rataluettelo);
            risteysjson.add(asema);
        }
    }
}
