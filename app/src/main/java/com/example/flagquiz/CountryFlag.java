package com.example.flagquiz;

public class CountryFlag {
    private String name; // 国家的名称
    private int imageResourceId; // 国旗图片的资源 ID
    private String region; // 国旗所属地区

    // 构造函数
    public CountryFlag(String name, int imageResourceId, String region) {
        this.name = name;
        this.imageResourceId = imageResourceId;
        this.region = region;
    }

    // 获取国家名称
    public String getName() {
        return name;
    }

    // 获取国旗图片的资源 ID
    public int getImageResourceId() {
        return imageResourceId;
    }

    // 获取国旗所属地区
    public String getRegion() {
        return region;
    }
}

