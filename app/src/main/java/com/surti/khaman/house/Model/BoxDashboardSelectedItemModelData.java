package com.surti.khaman.house.Model;

public class BoxDashboardSelectedItemModelData {
    private String selected_item_name, selected_weight, selected_price, selected_amount, selected_fixed_price, selected_fixed_weight;

    public BoxDashboardSelectedItemModelData(String item_name, String weight, String price) {
        this.selected_item_name = item_name;
        this.selected_weight = weight;
        this.selected_price = price;
    }

    public String get_selected_item_name() {
        return selected_item_name;
    }

    public String get_selected_weight() {
        return selected_weight;
    }

    public String get_selected_price() {
        return selected_price;
    }


    public void set_selected_item_name(String item_name) {
        this.selected_item_name = item_name;
    }

    public void set_selected_weight(String weight) {
        if(weight == "0"){
            this.selected_weight = "";
        }else{
            this.selected_weight = weight;
        };
    }

    public void set_selected_price(String price) {
        if(price == "0"){
            this.selected_price = "";
        }else{
            this.selected_price = price;
        }
    }





}


