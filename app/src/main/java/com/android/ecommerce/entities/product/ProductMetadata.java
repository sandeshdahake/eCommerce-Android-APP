
package com.android.ecommerce.entities.product;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductMetadata implements Serializable{

    public List<MetadataItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<MetadataItem> itemList) {
        this.itemList = itemList;
    }

    private List<MetadataItem>  itemList;

}
