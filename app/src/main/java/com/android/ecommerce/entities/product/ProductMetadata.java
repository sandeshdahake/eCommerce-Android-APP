
package com.android.ecommerce.entities.product;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMetadata {

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
