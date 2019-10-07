package com.example.autowaiter.Models;

public class MenuModel {
    public String menuName;
    public String menuImage;

    public MenuModel(String menuName, String menuImage) {
        this.menuName = menuName;
        this.menuImage = menuImage;
    }

    public MenuModel() {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }
}
