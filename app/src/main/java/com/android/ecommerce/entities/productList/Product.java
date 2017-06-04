
package com.android.ecommerce.entities.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Rating")
    @Expose
    private Float rating;
    @SerializedName("IsBestSeller")
    @Expose
    private String isBestSeller;
    @SerializedName("Primary_Camera")
    @Expose
    private String primaryCamera;
    @SerializedName("OS")
    @Expose
    private String oS;
    @SerializedName("Screen_Size")
    @Expose
    private String screenSize;
    @SerializedName("Ram")
    @Expose
    private String ram;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(String isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

    public String getPrimaryCamera() {
        return primaryCamera;
    }

    public void setPrimaryCamera(String primaryCamera) {
        this.primaryCamera = primaryCamera;
    }

    public String getOS() {
        return oS;
    }

    public void setOS(String oS) {
        this.oS = oS;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

}
