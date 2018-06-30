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
class Causes {
    private String categoryCode;
    private int categoryCodeId;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public int getCategoryCodeId() {
        return categoryCodeId;
    }

    public void setCategoryCodeId(int categoryCodeId) {
        this.categoryCodeId = categoryCodeId;
    }
    
}
