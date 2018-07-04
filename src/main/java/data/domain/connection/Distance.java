package data.domain.connection;

/**
 *
 * @author Johannes, refactoring Pirjo
 */
public class Distance {

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // great circle distance in radians
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // convert back to degrees
        angle = Math.toDegrees(angle);

        // each degree on a great circle of Earth is 60 nautical miles
        double distance = 60 * angle * 1.852001;

        return distance;
    }
}
