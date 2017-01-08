
package com.android.ecommerce.entities.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Property {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("name")
    @Expose
    private String name;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
