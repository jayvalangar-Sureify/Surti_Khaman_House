package com.surti.khaman.house.Model;

public class WholesaleModelData {
    private int id;
    private String item_name;
    private String item_weight;
    private String item_price;

    //generate constructor

    public WholesaleModelData(int id, String item_name, String item_weight, String item_price) {
        this.id = id;
        this.item_name = item_name;
        this.item_weight = item_weight;
        this.item_price = item_price;
    }
    //generate getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public String getFixedWeight() {
        return item_weight;
    }

    public void setFixedWeight(String item_weight) {
        this.item_weight = item_weight;
    }

    public String getFixedPrice() {
        return item_price;
    }

    public void setFixedPrice(String item_price) {
        this.item_price = item_price;
    }
}


