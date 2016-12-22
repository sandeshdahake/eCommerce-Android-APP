package com.android.ecommerce.entities.drawerMenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrawerItemCategory {
    @SerializedName("Id")
    @Expose
    private long id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("subcategories")
    @Expose
    private List<DrawerItemSubCategory> subcategories = null;

    public DrawerItemCategory() {
    }

    public DrawerItemCategory(long id, long originalId, String name) {
        this.id = id;
       // this.id = originalId;
        this.name = name;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DrawerItemSubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<DrawerItemSubCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public boolean hasChildren() {
        return subcategories != null && !subcategories.isEmpty();
    }


}
