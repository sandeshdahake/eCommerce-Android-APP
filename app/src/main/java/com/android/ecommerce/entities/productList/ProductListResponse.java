package com.android.ecommerce.entities.productList;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ProductListResponse {


    @SerializedName("records")
    private List<Product> products;



    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
