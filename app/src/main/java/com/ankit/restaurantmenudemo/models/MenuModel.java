package com.ankit.restaurantmenudemo.models;

public class MenuModel {
    public String name;
    public String description;
    public boolean isHeader;
    public boolean isImage;

    public MenuModel() {
    }

    public MenuModel(String name, String description, boolean isHeader, boolean isImage) {
        this.name = name;
        this.description = description;
        this.isHeader = isHeader;
        this.isImage = isImage;
    }
}
