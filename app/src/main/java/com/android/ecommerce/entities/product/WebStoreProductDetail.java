
package com.android.ecommerce.entities.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebStoreProductDetail {


    @SerializedName("Price")
    @Expose
    private Price_ price;
    @SerializedName("Sheeping")
    @Expose
    private String sheeping;
    @SerializedName("productUrl")
    @Expose
    private String productUrl;
    @SerializedName("CodAvailable")
    @Expose
    private String codAvailable;
    @SerializedName("DeliveryInfo")
    @Expose
    private String deliveryInfo;
    @SerializedName("ReviewPageUrl")
    @Expose
    private String reviewPageUrl;
    @SerializedName("storeLogo")
    @Expose
    private String storeLogo;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("storeName")
    @Expose
    private String storeName;

    public Price_ getPrice() {
        return price;
    }

    public void setPrice(Price_ price) {
        this.price = price;
    }

    public String getSheeping() {
        return sheeping;
    }

    public void setSheeping(String sheeping) {
        this.sheeping = sheeping;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getCodAvailable() {
        return codAvailable;
    }

    public void setCodAvailable(String codAvailable) {
        this.codAvailable = codAvailable;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getReviewPageUrl() {
        return reviewPageUrl;
    }

    public void setReviewPageUrl(String reviewPageUrl) {
        this.reviewPageUrl = reviewPageUrl;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
