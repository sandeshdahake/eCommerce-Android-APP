
package com.android.ecommerce.entities.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUrlFromApi {

    private Integer id;

    private Integer productId;
    
    @SerializedName("Url")
    @Expose
    private String url;

    private String zoomImageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZoomImageUrl() {
        return zoomImageUrl;
    }

    public void setZoomImageUrl(String zoomImageUrl) {
        this.zoomImageUrl = zoomImageUrl;
    }

}
