
package com.android.ecommerce.entities.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("PageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("PageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("TotalProducts")
    @Expose
    private Integer totalProducts;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

}
