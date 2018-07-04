package data.domain.station;

import data.domain.connection.database.Dcd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pirjo
 */
public class Station {

    private boolean passengerTraffic;
    private String type;
    private String stationName;
    private String stationShortCode;
    private int stationUICCode;
    private String countryCode;
    private double longitude;
    private double latitude;

    public boolean isPassengerTraffic() {
        return passengerTraffic;
    }

    public void setPassengerTraffic(boolean passengerTraffic) {
        this.passengerTraffic = passengerTraffic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public int getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(int stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Stations{" + "passengerTraffic=" + passengerTraffic + ", type=" + type + ", stationName=" + stationName + ", stationShortCode=" + stationShortCode + ", stationUICCode=" + stationUICCode + ", countryCode=" + countryCode + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }

//    public void lisaaTauluun() throws SQLException {
//        try (Connection con
//                = DriverManager.getConnection("jdbc:mysql://localhost:3306/yhteydet?useSSL=false&serverTimezone=UTC",
//                        "root", "salasana")) {
//            System.out.println("Connection saatu");
//            String sql = "INSERT INTO station (passengerTraffic, type, stationName, stationShortCode, stationUICCode, countryCode, longitude, latitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setString(1, String.valueOf(this.passengerTraffic));
//            stmt.setString(2, this.type);
//            stmt.setString(3, this.stationName);
//            stmt.setString(4, this.stationShortCode);
//            stmt.setInt(5, this.stationUICCode);
//            stmt.setString(6, this.countryCode);
//            stmt.setFloat(7, (float) this.longitude);
//            stmt.setFloat(8, (float) this.latitude);
//            System.out.println(stmt.executeUpdate());
//        } catch (SQLException ex) {
//            System.out.println("Tämä");
//            Logger.getLogger(Dcd.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

}
