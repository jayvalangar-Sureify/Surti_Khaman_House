package com.surti.khaman.house.Model;

public class DashboaedModelData {

    private String item_name, weight, price, amount, fixed_price, fixed_weight;

    public DashboaedModelData(String item_name, String weight, String price, String amount, String fixed_price, String fixed_weight) {
        this.item_name = item_name;
        this.weight = weight;
        this.price = price;
        this.amount = amount;
        this.fixed_price = fixed_price;
        this.fixed_weight = fixed_weight;
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

    public String getFixedPrice() {
        return fixed_price;
    }

    public String getFixedWeight() {
        return fixed_weight;
    }


    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setDynamicWeight(String weight) {
        if(weight == "0"){
            this.weight = "";
        }else{
            this.weight = weight;
        };
    }

    public void setDynamicPrice(String price) {
        if(price == "0"){
            this.price = "";
        }else{
            this.price = price;
        }
    }

    public void setCalculatedAmount(String amount) {

        if(amount == "0"){
            this.amount = "";
        }else{
            this.amount = amount;
        }
    }

    public void setFixedprice(String fixed_price) {
        this.fixed_price = fixed_price;
    }

    public void setFixedweight(String fixed_weight) {
        this.fixed_weight = fixed_weight;
    }




}
