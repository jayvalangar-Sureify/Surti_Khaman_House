package com.surti.khaman.house.Model;

public class ExpensesModelData {
    private int id;
    private String expenses_amount;
    private String expenses_note;
    private String expenses_date_time;

    //generate constructor

    public ExpensesModelData(int id, String expenses_amount, String expenses_note, String expenses_date_time) {
        this.id = id;
        this.expenses_amount = expenses_amount;
        this.expenses_note = expenses_note;
        this.expenses_date_time = expenses_date_time;
    }
    //generate getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set_Expenses_amount(String expenses_amount) {
        this.expenses_amount = expenses_amount;
    }

    public String get_Expenses_Amount() {
        return expenses_amount;
    }

    public void set_Expenses_Note(String expenses_note) {
        this.expenses_note = expenses_note;
    }

    public String get_Expenses_Note() {
        return expenses_note;
    }

    public void set_Expenses_Date_Time(String expenses_date_time) {
        this.expenses_date_time = expenses_date_time;
    }

    public String get_Expenses_Date_Time() {
        return expenses_date_time;
    }
}

