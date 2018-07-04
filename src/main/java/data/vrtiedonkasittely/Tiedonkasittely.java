package data.vrtiedonkasittely;

import data.domain.station.Station;
import data.domain.station.Raideosuus;
import data.domain.station.Asema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import data.domain.connection.database.Dcd;
import data.domain.station.Stations;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Pirjo/Tommin antaman JSON_pohjan mukaan
 */
public class Tiedonkasittely {

    private List<Raideosuus> raideosuudet;
    private List<Station> stationsList;
    private Stations stations;
    private Set<Asema> asemat;
    private List<Asema> risteysasemat = new ArrayList<>();
    private Map<String, String> asematLyhenteineen;

    public Tiedonkasittely() {
        lueRaideosuuksienJSONData();//alustetaan raideosuudet
        lueAsemienJSONData();//alustetaan asemat
        matkustajaAsematLyhenteineen();//etsitään matkustaja-asemat ja niiden lyhenteet
        irrotaRatatiedot();//siirretään ratatiedot kunkin aseman alle
        lisaaAsemienNimet();// lisätään asemien nimet
        selvitäRisteysasemat();//selvitetään risteysasemat
//        asematJsoniksi(); Käytettiin rata-asemalistan JSON-muotoon kirjoittamiseen.
//        risteysAsematJsoniksi(); Käytettiin risteysasemien JSON-muotoon kirjoittamiseen.
    }

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
            this.stationsList = mapper.readValue(url, tarkempiListanTyyppi);
            Iterator<Station> it = stationsList.iterator();
            while(it.hasNext()) {
                if (!it.next().isPassengerTraffic()) {
                    it.remove();
                }
            }
            this.stations = new Stations();
            this.stations.addStations(stationsList);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    private List<String> shortcodet() throws SQLException {
        List<String> shortcodet = new ArrayList<>();
        try (Connection con
                = DriverManager.getConnection("jdbc:mysql://localhost:3306/yhteydet?useSSL=false&serverTimezone=UTC",
                        "root", "salasana")) {
            System.out.println("Connection saatu");
            String sql = "SELECT stationShortCode FROM station WHERE passengerTraffic='true'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                shortcodet.add(rs.getString("stationShortCode"));
            }
            return shortcodet;
        }
    }

    public void matkustajaAsematLyhenteineen() {
        asematLyhenteineen = new HashMap<>();
        for (Station station : stationsList) {
            if (station.isPassengerTraffic()) {
                this.asematLyhenteineen.put(station.getStationShortCode(), station.getStationName());
            }
        }
    }

    public void irrotaRatatiedot() {
        asemat = new HashSet<>();
        Asema yv = new Asema("YV");//Ylivieska puuttuu (jostain tuntemattomasta syystä) risteysasemien joukosta, vaikka siellä kohtaavat kaksi rataa. Toinen rata lisätty tässä.
        yv.addTrack("087");
        asemat.add(yv);
        for (Raideosuus r : raideosuudet) {
            String tunnus = r.getStation();
            if (tarkistetaanRelevanssi(tunnus)) {
                String rata = r.getRanges().get(0).getStartLocation().getTrack();
                while (rata.length() < 3) {
                    rata = "0" + rata;
                }
                if (!asemat.stream().anyMatch(a -> a.getShortCode().equals(tunnus))) {
                    Asema asema = new Asema(tunnus);
                    asema.addTrack(rata);
                    asemat.add(asema);
                } else {
                    for (Asema a : asemat) {
                        if (a.getShortCode().equals(tunnus)) {
                            a.addTrack(rata);
                        }
                    }
                }
            }
        }
    }

    public void lisaaAsemienNimet() {
        for (Asema a : asemat) {
            String tunnus = a.getShortCode();
            a.setName(asematLyhenteineen.get(tunnus));
        }
    }

    public void selvitäRisteysasemat() {
        this.risteysasemat = asemat.stream().filter(a -> a.getTrack().size() > 1).collect(Collectors.toList());
        Set<String> poistettavat = new HashSet<>();
        poistettavat.add("ILR 650");//Ilmalan ratapihan, Käpylän ja Pasilan yhdistävä rata. Tätä ei voi suodattaa millään järkevällä muulla konstilla.
        poistettavat.add("141");//Hyvinkään ja Karjaan välinen rata, ei matkustajaliikennettä.
        poistettavat.add("123");//Kehärata, joka sotkee kaukojunahaun; tämä on lisättävä myöhemmin takaisin.
        for (String rata : radatSettiin()) {
            List<String> paikat = etsiRadanVarrellaOlevatAsemat(rata);
            if (paikat.size() < 2) {
                poistettavat.add(rata);
            }
        }
        poistettavat.stream().forEach(p -> {
            this.risteysasemat.stream().forEach(r -> {
                if (r.getTrack().contains(p)) {
                    r.removeTrack(p);
                }
            });
        });
        this.risteysasemat = this.risteysasemat.stream().filter(a -> a.getTrack().size() > 1).collect(Collectors.toList());
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
        for (Station st : this.stationsList) {
            if (st.getStationShortCode().equals(tunnus)) {
                if (st.isPassengerTraffic() && st.getType().equals("STATION") || st.getType().equals("STOPPING_POINT")) {
                    return true;
                }
            }
        }
        return false;
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

    public List<Asema> listaaRisteysasemat() {
        return this.risteysasemat;
    }
    
    public Set<Asema> listaaAsemat() {
        return this.asemat;
    }
    
    public Map<String, String> mappaaAsematLyhenteineen() {
        return this.asematLyhenteineen;
    }
    
    public List<Station> listaaStations() {
        return this.stationsList;
    }
    
    public Stations annaAsematMapissa() {
        return this.stations;
    }
}
