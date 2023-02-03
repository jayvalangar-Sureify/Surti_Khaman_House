package com.surti.khaman.house.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseMain extends SQLiteOpenHelper {
    public static final String DBNAME="shop_database.db";

    public static final String SHOP_MENU_TABLE_NAME="shop_menu";
    public static final String SHOP_MENU_ITEM_NAME_COLUMN="item_name_column";
    public static final String SHOP_MENU_ITEM_WEIGHT_COLUMN="item_weight_column";
    public static final String SHOP_MENU_ITEM_PRICE_COLUMN="item_price_column";

    public static final String SHOP_REVENUE_TABLE_NAME="shop_revenue";
    public static final String SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN="item_name_weight_price";
    public static final String SHOP_REVENUE_BILL_AMOUNT_COLUMN="bill_amount_column";
    public static final String SHOP_REVENUE_BILL_DATE_TIME_COLUMN="bill_date_time_column";
    public static final String SHOP_REVENUE_BILL_NO_COLUMN = "bill_no_column";

    public static final String SHOP_EXPENSES_TABLE_NAME="shop_expenses";
    public static final String SHOP_EXPENSES_AMOUNT="expenses_amount";
    public static final String SHOP_EXPENSES_NOTE="expenses_note";
    public static final String SHOP_EXPENSES_DATE_TIME_COLUMN="expenses_date_time_column";


    // Change When app update
    public static final int VER=1;

    String shop_menu_query, shop_revenue_query, shop_expenses_query;

    public DatabaseMain(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        shop_menu_query="create table "+SHOP_MENU_TABLE_NAME+"(id integer primary key, "+SHOP_MENU_ITEM_NAME_COLUMN+" text, "+SHOP_MENU_ITEM_WEIGHT_COLUMN+" text, "+SHOP_MENU_ITEM_PRICE_COLUMN+" text)";
        shop_revenue_query="create table "+SHOP_REVENUE_TABLE_NAME+"(id integer primary key, "+SHOP_REVENUE_BILL_NO_COLUMN+" text, "+SHOP_REVENUE_BILL_DATE_TIME_COLUMN+" text, "+SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN+" text, "+SHOP_REVENUE_BILL_AMOUNT_COLUMN+" text)";
        shop_expenses_query="create table "+SHOP_EXPENSES_TABLE_NAME+"(id integer primary key, "+SHOP_EXPENSES_AMOUNT+" text, "+SHOP_EXPENSES_NOTE+" text, "+SHOP_EXPENSES_DATE_TIME_COLUMN+" text)";

        db.execSQL(shop_menu_query);
        db.execSQL(shop_revenue_query);
        db.execSQL(shop_expenses_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        shop_menu_query="drop table if exists "+SHOP_MENU_TABLE_NAME+"";
        shop_revenue_query="drop table if exists "+SHOP_REVENUE_TABLE_NAME+"";
        shop_expenses_query="drop table if exists "+SHOP_EXPENSES_TABLE_NAME+"";

        db.execSQL(shop_menu_query);
        db.execSQL(shop_revenue_query);
        db.execSQL(shop_expenses_query);

        onCreate(db);
    }
}
