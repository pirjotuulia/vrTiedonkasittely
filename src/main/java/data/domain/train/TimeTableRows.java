/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.train;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 *
 * @author Pirjo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeTableRows {

    private String stationShortCode;
    private int stationUICCode;
    private String countryCode;
    private String type;
    private boolean trainStopping;
    private boolean commercialStop;
    private String commercialTrack;
    private boolean cancelled;
    private String scheduledTime;
    private String actualTime;
    private int differenceInMinutes;
    private List<Causes> causes;
    private TrainReady trainReady;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTrainStopping() {
        return trainStopping;
    }

    public void setTrainStopping(boolean trainStopping) {
        this.trainStopping = trainStopping;
    }

    public boolean isCommercialStop() {
        return commercialStop;
    }

    public void setCommercialStop(boolean commercialStop) {
        this.commercialStop = commercialStop;
    }

    public String getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public int getDifferenceInMinutes() {
        return differenceInMinutes;
    }

    public void setDifferenceInMinutes(int differenceInMinutes) {
        this.differenceInMinutes = differenceInMinutes;
    }

    public List<Causes> getCauses() {
        return causes;
    }

    public void setCauses(List<Causes> causes) {
        this.causes = causes;
    }

    public TrainReady getTrainReady() {
        return trainReady;
    }

    public void setTrainReady(TrainReady trainReady) {
        this.trainReady = trainReady;
    }
}
