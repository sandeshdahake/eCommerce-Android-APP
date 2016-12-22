package com.android.ecommerce.entities.drawerMenu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrawerItemCategory {

    private long Id;

    @SerializedName("original_Id")
    private long originalId;
    private String Name;
    private List<DrawerItemCategory> children;
    private String type;

    public DrawerItemCategory() {
    }

    public DrawerItemCategory(long Id, long originalId, String Name) {
        this.Id = Id;
        this.originalId = originalId;
        this.Name = Name;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(long originalId) {
        this.originalId = originalId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<DrawerItemCategory> getChildren() {
        return children;
    }

    public void setChildren(List<DrawerItemCategory> children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawerItemCategory that = (DrawerItemCategory) o;

        if (Id != that.Id) return false;
        if (originalId != that.originalId) return false;
        if (Name != null ? !Name.equals(that.Name) : that.Name != null) return false;
        if (children != null ? !children.equals(that.children) : that.children != null)
            return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (Id ^ (Id >>> 32));
        result = 31 * result + (int) (originalId ^ (originalId >>> 32));
        result = 31 * result + (Name != null ? Name.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DrawerItemCategory{" +
                "Id=" + Id +
                ", originalId=" + originalId +
                ", Name='" + Name + '\'' +
                ", children=" + children +
                ", type='" + type + '\'' +
                '}';
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}
