package com.android.ecommerce.entities;

import java.util.List;

public class BannersResponse {

    private List<Banner> records;


    private List<BannerProducts> popularRecords;
    private List<BannerProducts> featuredRecords;

    public BannersResponse() {
    }

    public List<Banner> getRecords() {
        return records;
    }

    public List<BannerProducts> getPopularRecords() {
        return popularRecords;
    }

    public void setPopularRecords(List<BannerProducts> popularRecords) {
        this.popularRecords = popularRecords;
    }

    public List<BannerProducts> getFeaturedRecords() {
        return featuredRecords;
    }

    public void setFeaturedRecords(List<BannerProducts> featuredRecords) {
        this.featuredRecords = featuredRecords;
    }

    public void setRecords(List<Banner> records) {
        this.records = records;
    }

}