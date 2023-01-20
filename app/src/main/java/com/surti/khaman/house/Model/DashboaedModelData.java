package com.surti.khaman.house.Model;

public class DashboaedModelData {

    private String item_name, weight, price, amount, fixed_price;

    public DashboaedModelData(String item_name, String weight, String price, String amount, String fixed_price) {
        this.item_name = item_name;
        this.weight = weight;
        this.price = price;
        this.amount = amount;
        this.fixed_price = fixed_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getWeight() {
        return weight;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getFixed_price() {
        return fixed_price;
    }


    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFixed_price(String fixed_price) {
        this.fixed_price = fixed_price;
    }




}
