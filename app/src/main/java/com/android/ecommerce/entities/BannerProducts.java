package com.android.ecommerce.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeshdahake@gmail.com on 30-12-2016.
 */
public class BannerProducts {
    @SerializedName("Id")
    private long id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Key")
    private String key;
    @SerializedName("Value")
    private String value;
    @SerializedName("Image")
    private String image;
    @SerializedName("Price")
    private String price;
    @SerializedName("IsBestSeller")
    private int isBestSeller;
    @SerializedName("Rating")
    private float rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(int isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BannerProducts that = (BannerProducts) o;

        if (id != that.id) return false;
        if (isBestSeller != that.isBestSeller) return false;
        if (Float.compare(that.rating, rating) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return image != null ? image.equals(that.image) : that.image == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + isBestSeller;
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BannerProducts{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", isBestSeller=" + isBestSeller +
                ", rating=" + rating +
                '}';
    }
}
