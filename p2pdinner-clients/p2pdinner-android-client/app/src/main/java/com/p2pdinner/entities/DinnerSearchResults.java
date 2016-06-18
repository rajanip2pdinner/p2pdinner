package com.p2pdinner.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rajaniy on 11/17/15.
 */
public class DinnerSearchResults implements Serializable {
    private String status;
    @SerializedName("results")
    private List<DinnerSearchResult> searchResults;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DinnerSearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<DinnerSearchResult> searchResults) {
        this.searchResults = searchResults;
    }
}
