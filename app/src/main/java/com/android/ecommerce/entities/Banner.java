package com.android.ecommerce.entities;

import com.google.gson.annotations.SerializedName;

public class Banner {

    @SerializedName("Id")
    private long id;

    @SerializedName("Image")
    private String imageUrl;



    @SerializedName("Url")
    private String productUrl;

    public Banner() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banner banner = (Banner) o;

        if (id != banner.id) return false;
        if (imageUrl != null ? !imageUrl.equals(banner.imageUrl) : banner.imageUrl != null)
            return false;
        return productUrl != null ? productUrl.equals(banner.productUrl) : banner.productUrl == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (productUrl != null ? productUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", productUrl='" + productUrl + '\'' +
                '}';
    }
}
