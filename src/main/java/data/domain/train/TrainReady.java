/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.train;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Pirjo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class TrainReady {
    private String source;
    private boolean accepted;
    private String timestamp;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}
