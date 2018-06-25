package data.domain;

import java.util.*;

/**
 *
 * @author Pirjo
 */
public class Risteysasema {

    private String tunnus;
    private String nimi;
    private TreeSet<String> radat;

    public Risteysasema(String tunnus, String nimi, TreeSet<String> radat) {
        this.tunnus = tunnus;
        this.nimi = nimi;
        this.radat = radat;
    }

    public String getTunnus() {
        return tunnus;
    }

    public void setTunnus(String tunnus) {
        this.tunnus = tunnus;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Set<String> getRadat() {
        return radat;
    }

    public void addRata(String rata) {
        this.radat.add(rata);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nimi).append("(").append(this.tunnus).append(")");
        this.radat.stream().forEach(r -> sb.append(", ").append(r));
        return sb.toString();
    }

}
