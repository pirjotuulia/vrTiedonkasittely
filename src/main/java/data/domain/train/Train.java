package data.domain.train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Pirjo
 */
public class Train {

    private int trainNumber;
    private String departureDate;
    private int operatorUICCode;
    private String operatorShortCode;
    private String trainType;
    private String trainCategory;
    private String commuterLineID;
    private boolean runningCurrently;
    private boolean cancelled;
    private long version;
    private String timetableType;
    private String timetableAcceptanceDate;
    private List<TimeTableRows> timeTableRows;

    @Override
    public String toString() {
        return this.trainCategory + " "
                + this.commuterLineID + " "
                + this.trainType + " "
                + this.trainNumber
                + " (" + "lähtöasema: " + palautaLahtoaikaLahtoasemalta()
                + ") (määräasema: " + palautaSaapumisaikaPaateasemalle() + ")";
    }

    public boolean nollastaLoppuun() {
        LocalDateTime ekaRivi = this.timeTableRows.get(0).palautaLahtoAika();
        LocalDateTime vikaRivi = this.timeTableRows.get(this.timeTableRows.size() - 1).palautaLahtoAika();
        if (ekaRivi.compareTo(vikaRivi) < 0) {
            return true;
        }
        return false;
    }

    public LocalDateTime palautaLahtoaikaLahtoasemalta() {
        LocalDateTime ekaRivi = this.timeTableRows.get(0).palautaLahtoAika();
        LocalDateTime vikaRivi = this.timeTableRows.get(this.timeTableRows.size() - 1).palautaLahtoAika();
        if (nollastaLoppuun()) {
            return ekaRivi;
        } else {
            return vikaRivi;
        }
    }

    public LocalDateTime palautaSaapumisaikaPaateasemalle() {
        LocalDateTime ekaRivi = this.timeTableRows.get(0).palautaLahtoAika();
        LocalDateTime vikaRivi = this.timeTableRows.get(this.timeTableRows.size() - 1).palautaLahtoAika();
        if (nollastaLoppuun()) {
            return vikaRivi;
        }
        return vikaRivi;
    }

    public LocalDateTime palautaPyydettyAikaAsemalta(String lahtoTaiSaapuminen, String shortcode) {
        return this.timeTableRows.stream()
                .filter(r -> r.getStationShortCode()
                .equals(shortcode)).filter(r -> r.getType()
                .equals(lahtoTaiSaapuminen)).findFirst().get().palautaLahtoAika();
    }

    public LocalDate palautaLahtoPvm() {
        LocalDate departureDateAsLocalDate = LocalDate.parse(this.departureDate);
        return departureDateAsLocalDate;
    }

    public String palautaLahtoasema() {
        if (nollastaLoppuun()) {
            return this.timeTableRows.get(0).getStationShortCode();
        }
        return this.timeTableRows.get(this.timeTableRows.size()-1).getStationShortCode();
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public int getOperatorUICCode() {
        return operatorUICCode;
    }

    public void setOperatorUICCode(int operatorUICCode) {
        this.operatorUICCode = operatorUICCode;
    }

    public String getOperatorShortCode() {
        return operatorShortCode;
    }

    public void setOperatorShortCode(String operatorShortCode) {
        this.operatorShortCode = operatorShortCode;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterlineID) {
        this.commuterLineID = commuterlineID;
    }

    public boolean isRunningCurrently() {
        return runningCurrently;
    }

    public void setRunningCurrently(boolean runningCurrently) {
        this.runningCurrently = runningCurrently;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(String timetableType) {
        this.timetableType = timetableType;
    }

    public String getTimetableAcceptanceDate() {
        return timetableAcceptanceDate;
    }

    public void setTimetableAcceptanceDate(String timetableAcceptanceDate) {
        this.timetableAcceptanceDate = timetableAcceptanceDate;
    }

    public List<TimeTableRows> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRows> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

}
