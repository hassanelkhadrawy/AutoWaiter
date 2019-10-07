package com.example.autowaiter.Models;

public class OrderModel {

    public String orderName;
    public int numberTable;

    public OrderModel(String orderName, int numberTable) {
        this.orderName = orderName;
        this.numberTable = numberTable;
    }

    public OrderModel() {
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(int numberTable) {
        this.numberTable = numberTable;
    }
}
