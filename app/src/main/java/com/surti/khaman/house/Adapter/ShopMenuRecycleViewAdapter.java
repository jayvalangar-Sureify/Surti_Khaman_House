package com.surti.khaman.house.Adapter;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.Model.ShopMenuModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;


public class ShopMenuRecycleViewAdapter extends RecyclerView.Adapter<ShopMenuRecycleViewAdapter.ModelViewHolder> {
    Context context;
    ArrayList<ShopMenuModelData>modelArrayList=new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    //generate constructor

    public ShopMenuRecycleViewAdapter(Context context, int singledata, ArrayList<ShopMenuModelData> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public ShopMenuRecycleViewAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_display_shop_menu_data,null);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopMenuRecycleViewAdapter.ModelViewHolder holder, int position) {
        final ShopMenuModelData model=modelArrayList.get(position);
        holder.tv_item_name.setText(model.getItem_name());
        holder.tv_item_weight.setText(model.getFixedWeight() + " gm");
        holder.tv_item_price.setText(model.getFixedPrice() + " Rs");

        //click on button go to main activity
//        holder.btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle=new Bundle();
//                bundle.putInt("id",model.getId());
//                bundle.putString("item_name",model.getItem_name());
//                bundle.putString("item_weight",model.getFixedWeight());
//                bundle.putString("item_price",model.getFixedPrice());
//                Intent intent=new Intent(context, MainActivity.class);
//                intent.putExtra("userdata",bundle);
//                context.startActivity(intent);
//            }
//        });

//--------------------------------------------------------------------------------------------------

        Dialog dialog = new Dialog(context);
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //==================================================================================
                dialog.setContentView(R.layout.update_menu_item_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                EditText et_item_name, et_item_weight, et_item_price;
                Button btn_update;
                et_item_name = (EditText) dialog.findViewById(R.id.et_item_name);
                et_item_weight = (EditText) dialog.findViewById(R.id.et_item_weight);
                et_item_price = (EditText) dialog.findViewById(R.id.et_item_price);
                btn_update = (Button) dialog.findViewById(R.id.btn_update);

                et_item_name.setText(model.getItem_name());
                et_item_weight.setText(model.getFixedWeight());
                et_item_price.setText(model.getFixedPrice());

                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues cv=new ContentValues();
                        String et_item_name_string, et_item_weight_string, et_item_price_string;
                        et_item_name_string = et_item_name.getText().toString();
                        et_item_weight_string = et_item_weight.getText().toString();
                        et_item_price_string = et_item_price.getText().toString();

                        if(!et_item_name_string.isEmpty() && !et_item_weight_string.isEmpty() && !et_item_price_string.isEmpty() ) {
                            cv.put(DatabaseMain.SHOP_MENU_ITEM_NAME_COLUMN, et_item_name_string);
                            cv.put(DatabaseMain.SHOP_MENU_ITEM_WEIGHT_COLUMN, et_item_weight_string);
                            cv.put(DatabaseMain.SHOP_MENU_ITEM_PRICE_COLUMN, et_item_price_string);
                            DatabaseMain databaseMain;
                            databaseMain = new DatabaseMain(context);
                            sqLiteDatabase = databaseMain.getReadableDatabase();
                            long recedit = sqLiteDatabase.update(DatabaseMain.SHOP_MENU_TABLE_NAME, cv, "id=" + model.getId(), null);
                            if (recedit != -1) {
                                ShopMenuModelData shopMenuModelData = new ShopMenuModelData(model.getId(), et_item_name.getText().toString(), et_item_weight.getText().toString(), et_item_price.getText().toString());
                                modelArrayList.set(position, shopMenuModelData);
                                notifyItemChanged(position);
//                                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            dialog.dismiss();
                        }else {
                            // Initialising Toast
                            Toast.makeText(context, "Enter ALL Field", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //-------------------------------------------------------------------------------


                dialog.show();
            }
        });
//--------------------------------------------------------------------------------------------------



        //delete row
        holder.btn_delte.setOnClickListener(new View.OnClickListener() {
            DatabaseMain dBmain=new DatabaseMain(context);
            @Override
            public void onClick(View v) {
                sqLiteDatabase=dBmain.getReadableDatabase();
                long delele=sqLiteDatabase.delete(DatabaseMain.SHOP_MENU_TABLE_NAME,"id="+model.getId(),null);
                if (delele!=-1){
                    Toast.makeText(context, "deleted data successfully", Toast.LENGTH_SHORT).show();
                    modelArrayList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_item_weight, tv_item_price;
        Button btn_update,btn_delte;
        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name=(TextView)itemView.findViewById(R.id.tv_item_name);
            tv_item_weight=(TextView)itemView.findViewById(R.id.tv_item_weight);
            tv_item_price=(TextView)itemView.findViewById(R.id.tv_item_price);
            btn_update=(Button)itemView.findViewById(R.id.btn_update);
            btn_delte=(Button)itemView.findViewById(R.id.btn_delte);
        }
    }
}