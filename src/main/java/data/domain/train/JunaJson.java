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
            System.out.println("Ei suoria junia välille " + departure + "-" + arrival);
            return null;
        }
        return junat;
    }
}
