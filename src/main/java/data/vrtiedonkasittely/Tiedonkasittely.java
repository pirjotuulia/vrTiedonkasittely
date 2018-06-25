package data.vrtiedonkasittely;

import data.domain.Risteysasema;
import data.domain.Station;
import data.domain.Raideosuus;
import data.domain.Asema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Pirjo/Tommin antaman JSON_pohjan mukaan
 */
public class Tiedonkasittely {

    private List<Raideosuus> raideosuudet;
    private List<Station> stations;
    private Set<Asema> asemat = new HashSet<>();
    private Map<String, TreeSet<String>> risteysasemat = new HashMap<>();
    private List<Risteysasema> risteyspaikat = new ArrayList<>();

    public void lueRaideosuuksienJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/metadata/track-sections", baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Raideosuus.class);
            this.raideosuudet = mapper.readValue(url, tarkempiListanTyyppi);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void lueAsemienJSONData() {
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/metadata/stations", baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Station.class);
            this.stations = mapper.readValue(url, tarkempiListanTyyppi);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void irrotaRatatiedot() {
        for (Raideosuus r : raideosuudet) {
            String tunnus = r.getStation();
            String rata = r.getRanges().get(0).getStartLocation().getTrack();
            while (rata.length() < 3) {
                rata = "0" + rata;
            }
            Asema asema = new Asema(tunnus, rata);
            asemat.add(asema);
        }
    }

    public void selvitäRisteysasemat() {
        for (Asema as : asemat) {
            risteysasemat.putIfAbsent(as.getStation(), new TreeSet());
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

    public void lisaaAsemienNimet() {
        for (Station station : stations) {
            String tunnus = station.getStationShortCode();
            if (station.isPassengerTraffic() && risteysasemat.containsKey(tunnus)) {
                String nimi = station.getStationName();
                Risteysasema rist = new Risteysasema(tunnus, nimi, risteysasemat.get(tunnus));
                risteyspaikat.add(rist);
            }
        }
    }

    public Map<String, String> matkustajaAsematLyhenteineen() {
        Map<String, String> asematLyhenteineen = new HashMap<>();
        for (Station station : stations) {
            if (station.isPassengerTraffic()) {
                asematLyhenteineen.put(station.getStationName(), station.getStationShortCode());
            }
        }
        return asematLyhenteineen;
    }

    public void risteysAsematJsoniksi() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File("risteysasemat.json"), risteyspaikat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void asematJsoniksi() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File("asemat.json"), asemat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> risteysasemienRadatSettiin() {
        Set<String> kaikkiRadat = new TreeSet();
        for (Risteysasema a : risteyspaikat) {
            for (String rata : a.getRadat()) {
                kaikkiRadat.add(rata);
            }
        }
        return kaikkiRadat;
    }

    public Set<String> radatSettiin() {
        Set<String> kaikkiRadat = new TreeSet();
        for (Asema a : asemat) {
            kaikkiRadat.add(a.getTrack());
        }
        return kaikkiRadat;
    }
}
