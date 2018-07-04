/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.train;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Pirjo
 */
public class JunaJson {
    
        public List<Train> lueJunienJSONData(String departure, String arrival) {
        String baseurl = "https://rata.digitraffic.fi/api/v1/live-trains/station";
        List<Train> junat = new ArrayList<>();
        try {
            URL url = new URL(URI.create(String.format("%s/" + departure + "/" + arrival , baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType tarkempiListanTyyppi = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);
            junat = mapper.readValue(url, tarkempiListanTyyppi);
        } catch (Exception ex) {
            return null;
        }
        Iterator<Train> it = junat.iterator();
        while(it.hasNext()) {
            Train juna = it.next();
            if (!juna.getTrainCategory().equals("Long-distance")||!juna.getTrainCategory().equals("Commuter")&&!juna.getTimetableType().equals("REGULAR")) {
                it.remove();
            }
        }
        if (!junat.isEmpty()) {
            return junat;
        }
        return null;
    }
}
