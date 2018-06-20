/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.vrtiedonkasittely;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class Risteysasema {

    private String tunnus;
    private String nimi;
    private Set<String> radat;

    public Risteysasema(String tunnus, String nimi, Set<String> radat) {
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
