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
import com.surti.khaman.house.Model.ExpensesModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;



public class ExpensesRecycleViewAdapter extends RecyclerView.Adapter<ExpensesRecycleViewAdapter.ModelViewHolder> {
    Context context;
    ArrayList<ExpensesModelData> modelArrayList=new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    //generate constructor

    public ExpensesRecycleViewAdapter(Context context, int singledata, ArrayList<ExpensesModelData> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public ExpensesRecycleViewAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_expenses_crud,null);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesRecycleViewAdapter.ModelViewHolder holder, int position) {
        final ExpensesModelData model=modelArrayList.get(position);

        holder.et_expenses_amount.setText(model.get_Expenses_Amount() + " Rs");
        holder.et_expenses_note.setText(model.get_Expenses_Note());
        holder.tv_expenses_date_time.setText(model.get_Expenses_Date_Time());


//--------------------------------------------------------------------------------------------------

        Dialog dialog = new Dialog(context);
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //==================================================================================
                dialog.setContentView(R.layout.update_expenses_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                EditText et_expense_amount, et_expense_note;
                TextView tv_date_time_value;
                Button btn_update;
                et_expense_amount = (EditText) dialog.findViewById(R.id.et_expense_amount);
                et_expense_note = (EditText) dialog.findViewById(R.id.et_expense_note);
                tv_date_time_value = (TextView) dialog.findViewById(R.id.tv_expenses_date_time);
                btn_update = (Button) dialog.findViewById(R.id.btn_update);

                et_expense_amount.setText(model.get_Expenses_Amount());
                et_expense_note.setText(model.get_Expenses_Note());
                tv_date_time_value.setText(model.get_Expenses_Date_Time());

                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues cv=new ContentValues();
                        String et_expense_amount_string, et_expense_note_string, tv_date_time_value_string;
                        et_expense_amount_string = et_expense_amount.getText().toString();
                        et_expense_note_string = et_expense_note.getText().toString();
                        tv_date_time_value_string = tv_date_time_value.getText().toString();

                        if(!et_expense_amount_string.isEmpty() && !et_expense_note_string.isEmpty() ) {
                            cv.put(DatabaseMain.SHOP_EXPENSES_AMOUNT, et_expense_amount_string);
                            cv.put(DatabaseMain.SHOP_EXPENSES_NOTE, et_expense_note_string);
                            cv.put(DatabaseMain.SHOP_EXPENSES_DATE_TIME_COLUMN, tv_date_time_value_string);
                            DatabaseMain databaseMain;
                            databaseMain = new DatabaseMain(context);
                            sqLiteDatabase = databaseMain.getReadableDatabase();
                            long recedit = sqLiteDatabase.update(DatabaseMain.SHOP_EXPENSES_TABLE_NAME, cv, "id=" + model.getId(), null);
                            if (recedit != -1) {
                                ExpensesModelData expensesModelData = new ExpensesModelData(model.getId(), et_expense_amount_string, et_expense_note_string, tv_date_time_value_string);
                                modelArrayList.set(position, expensesModelData);
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
                long delele=sqLiteDatabase.delete(DatabaseMain.SHOP_EXPENSES_TABLE_NAME,"id="+model.getId(),null);
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
        TextView tv_expenses_date_time;
        EditText et_expenses_amount, et_expenses_note;
        Button btn_update,btn_delte;
        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_expenses_date_time=(TextView)itemView.findViewById(R.id.tv_expenses_date_time);
            et_expenses_amount=(EditText)itemView.findViewById(R.id.et_expense_amount);
            et_expenses_note=(EditText) itemView.findViewById(R.id.et_expenses_note);
            btn_update=(Button)itemView.findViewById(R.id.btn_update);
            btn_delte=(Button)itemView.findViewById(R.id.btn_delte);
        }
    }
}
