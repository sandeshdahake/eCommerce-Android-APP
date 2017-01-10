package com.android.ecommerce.entities.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeshdahake@gmail.com on 09-01-2017.
 */

public class MetadataItem {
    @SerializedName("grouplabel")
    @Expose
    private String grouplabel;
    @SerializedName("properties")
    @Expose
    private List<Property> properties = null;

    public String getGrouplabel() {
        return grouplabel;
    }

    public void setGrouplabel(String grouplabel) {
        this.grouplabel = grouplabel;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
