package data.domain.station;

/**
 *
 * @author Pirjo
 */
class EndLocation {

    private String track;
    private int kilometres;
    private int metres;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getKilometres() {
        return kilometres;
    }

    public void setKilometres(int kilometres) {
        this.kilometres = kilometres;
    }

    public int getMetres() {
        return metres;
    }

    public void setMetres(int metres) {
        this.metres = metres;
    }

    @Override
    public String toString() {
        return "EndLocation{" + "track=" + track + ", kilometres=" + kilometres + ", metres=" + metres + '}';
    }

    
}
