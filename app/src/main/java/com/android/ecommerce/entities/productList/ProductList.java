
package com.android.ecommerce.entities.productList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {

    @SerializedName("Summary")
    @Expose
    private Summary summary;
    @SerializedName("hotProperties")
    @Expose
    private List<HotProperty> hotProperties = null;
    @SerializedName("Products")
    @Expose
    private List<Product> products = null;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<HotProperty> getHotProperties() {
        return hotProperties;
    }

    public void setHotProperties(List<HotProperty> hotProperties) {
        this.hotProperties = hotProperties;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
