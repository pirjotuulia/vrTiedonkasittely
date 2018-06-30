package data.vrtiedonkasittely;

import data.domain.station.Station;
import data.domain.station.Raideosuus;
import data.domain.station.Asema;
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
import java.util.stream.Collectors;

/**
 *
 * @author Pirjo/Tommin antaman JSON_pohjan mukaan
 */
public class Tiedonkasittely {

    private List<Raideosuus> raideosuudet;
    private List<Station> stations;
    private Set<Asema> asemat;
    private List<Asema> risteysasemat = new ArrayList<>();
    private Map<String, String> asematLyhenteineen;

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
        asemat = new HashSet<>();
        for (Raideosuus r : raideosuudet) {
            String tunnus = r.getStation();
            if (tarkistetaanRelevanssi(tunnus)) {
                String rata = r.getRanges().get(0).getStartLocation().getTrack();
                while (rata.length() < 3) {
                    rata = "0" + rata;
                }
                if (!asemat.stream().anyMatch(a -> a.getStation().equals(tunnus))) {
                    Asema asema = new Asema(tunnus);
                    asema.addTrack(rata);
                    asemat.add(asema);
                } else {
                    for (Asema a : asemat) {
                        if (a.getStation().equals(tunnus)) {
                            a.addTrack(rata);
                        }
                    }
                }
            }
        }
    }

    public void selvitäRisteysasemat() {
        System.out.println("Matkustaja-asemat yhteensä " + asemat.size());
        this.risteysasemat = asemat.stream().filter(a -> a.getTrack().size() > 1).collect(Collectors.toList());
        System.out.println("Risteysasemat poiston jälkeen " + risteysasemat.size());
        Set<String> poistettavat = new HashSet<>();
        poistettavat.add("ILR 650");//Ilmalan ratapihan, Käpylän ja Pasilan yhdistävä rata. Tätä ei voi suodattaa millään järkevällä muulla konstilla.
        for (String rata : radatSettiin()) {
            List<String> paikat = etsiRadanVarrellaOlevatAsemat(rata);
            if (paikat.size() < 2) {
                poistettavat.add(rata);
            }
        }
        System.out.println("Poistettavia ratoja " + poistettavat.size());
        poistettavat.stream().forEach(p -> {
            this.risteysasemat.stream().forEach(r -> {
                if (r.getTrack().contains(p)) {
                    r.removeTrack(p);
                }
            });
        });
        this.risteysasemat = this.risteysasemat.stream().filter(a -> a.getTrack().size() > 1).collect(Collectors.toList());
        System.out.println("Risteysasemat poiston jälkeen " + risteysasemat.size());
    }

    public void lisaaAsemienNimet() {
        for (Asema a : asemat) {
            String tunnus = a.getStation();
            a.setName(asematLyhenteineen.get(tunnus));
        }
    }

    public void risteysAsematJsoniksi() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File("risteysasemat.json"), risteysasemat);
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
        for (Asema a : risteysasemat) {
            for (String rata : a.getTrack()) {
                kaikkiRadat.add(rata);
            }
        }
        return kaikkiRadat;
    }

    public Set<String> radatSettiin() {
        Set<String> kaikkiRadat = new TreeSet();
        for (Asema a : asemat) {
            kaikkiRadat.addAll(a.getTrack());
        }
        return kaikkiRadat;
    }

    private boolean tarkistetaanRelevanssi(String tunnus) {
        for (Station st : this.stations) {
            if (st.getStationShortCode().equals(tunnus)) {
                if (st.isPassengerTraffic() && st.getType().equals("STATION") || st.getType().equals("STOPPING_POINT")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void matkustajaAsematLyhenteineen() {
        asematLyhenteineen = new HashMap<>();
        for (Station station : stations) {
            if (station.isPassengerTraffic()) {
                this.asematLyhenteineen.put(station.getStationShortCode(), station.getStationName());
            }
        }
    }

    public List<String> etsiRadanVarrellaOlevatAsemat(String string) {
        List<String> radanAsemat = new ArrayList<>();
        for (Asema a : asemat) {
            if (a.getTrack().contains(string)) {
                radanAsemat.add(a.getName());
            }
        }
        return radanAsemat;
    }
}
