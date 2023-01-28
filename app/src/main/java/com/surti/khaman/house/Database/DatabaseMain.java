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

    // Change When app update
    public static final int VER=1;

    String query;

    public DatabaseMain(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        query="create table "+SHOP_MENU_TABLE_NAME+"(id integer primary key, "+SHOP_MENU_ITEM_NAME_COLUMN+" text, "+SHOP_MENU_ITEM_WEIGHT_COLUMN+" text, "+SHOP_MENU_ITEM_PRICE_COLUMN+" text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        query="drop table if exists "+SHOP_MENU_TABLE_NAME+"";
        db.execSQL(query);
        onCreate(db);
    }
}
