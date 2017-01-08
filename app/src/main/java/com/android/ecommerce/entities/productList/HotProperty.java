
package com.android.ecommerce.entities.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotProperty {

    @SerializedName("PropertyName")
    @Expose
    private String propertyName;
    @SerializedName("Priority")
    @Expose
    private String priority;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
