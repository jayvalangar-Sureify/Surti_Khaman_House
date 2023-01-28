package com.surti.khaman.house.ui.shop_menu;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Adapter.ShopMenuRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.Model.ShopMenuModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentShopMenuBinding;

import java.util.ArrayList;

public class ShopMenuFragment extends Fragment {

    DatabaseMain databaseMain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    ShopMenuRecycleViewAdapter myAdapter;

    private FragmentShopMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseMain=new DatabaseMain(getActivity());
        //create method
        recyclerView= (RecyclerView) root.findViewById(R.id.rv_shop_menu);

        Dialog dialog = new Dialog(getActivity());

        Button btnAdd = root.findViewById(R.id.btn_add_item);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //==================================================================================
                dialog.setContentView(R.layout.add_menu_item_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                EditText et_item_name, et_item_weight, et_item_price;
                Button btn_save, btn_display, btn_update;
                et_item_name = (EditText) dialog.findViewById(R.id.et_item_name);
                et_item_weight = (EditText) dialog.findViewById(R.id.et_item_weight);
                et_item_price = (EditText) dialog.findViewById(R.id.et_item_price);
                btn_save = (Button) dialog.findViewById(R.id.btn_save);

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertDate(et_item_name.getText().toString(), et_item_weight.getText().toString(), et_item_price.getText().toString());
                        dialog.dismiss();
                        displayData();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });

                //-------------------------------------------------------------------------------


                dialog.show();
            }
        });
        //==================================================================================

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void insertDate(String itemName, String itemWeight, String itemPrice){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseMain.SHOP_MENU_ITEM_NAME_COLUMN, itemName);
        cv.put(DatabaseMain.SHOP_MENU_ITEM_WEIGHT_COLUMN, itemWeight);
        cv.put(DatabaseMain.SHOP_MENU_ITEM_PRICE_COLUMN, itemPrice);

        sqLiteDatabase = databaseMain.getWritableDatabase();
        Long recinsert = sqLiteDatabase.insert(DatabaseMain.SHOP_MENU_TABLE_NAME, null, cv);
        if (recinsert != null) {
            Toast.makeText(getActivity(), "successfully inserted data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "something wrong try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayData() {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_MENU_TABLE_NAME+"",null);
        ArrayList<ShopMenuModelData> modelArrayList=new ArrayList<>();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_weight = cursor.getString(2);
            String item_price = cursor.getString(3);
            modelArrayList.add(new ShopMenuModelData(id, item_name, item_weight, item_price));
        }
        cursor.close();
        myAdapter=new ShopMenuRecycleViewAdapter(getActivity(),R.layout.singledata,modelArrayList,sqLiteDatabase);
        recyclerView.setAdapter(myAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}