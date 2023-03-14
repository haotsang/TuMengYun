package com.example.myapplication.entity;

import java.util.List;

public class LocationInfo {

    private int id;

    private String name;

    private List<BannerSimpleItem> bannerList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BannerSimpleItem> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerSimpleItem> bannerList) {
        this.bannerList = bannerList;
    }
}
