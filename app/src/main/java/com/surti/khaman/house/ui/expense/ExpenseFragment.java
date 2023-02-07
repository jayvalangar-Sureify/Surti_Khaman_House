package com.surti.khaman.house.ui.expense;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Adapter.ExpensesRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.ExpensesModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentExpenseBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseFragment extends Fragment {

    String internal_file_data = "";

    String currentDateAndTime;
    DatabaseMain databaseMain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    ExpensesRecycleViewAdapter myAdapter;

    private FragmentExpenseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExpenseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        databaseMain=new DatabaseMain(getActivity());
        //create method
        recyclerView= (RecyclerView) root.findViewById(R.id.rv_expenses);

        Dialog dialog = new Dialog(getActivity());

        Button btnAdd = root.findViewById(R.id.btn_add_expenses);
        Button btnDashboard = root.findViewById(R.id.btn_dashboard_expenses);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //==================================================================================
                dialog.setContentView(R.layout.add_expenses_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                EditText et_expense_amount, et_expense_note;
                TextView tv_expenses_date_time;
                Button btn_save;
                et_expense_amount = (EditText) dialog.findViewById(R.id.et_expense_amount);
                et_expense_note = (EditText) dialog.findViewById(R.id.et_expense_note);
                tv_expenses_date_time = (TextView) dialog.findViewById(R.id.tv_expenses_date_time);
                btn_save = (Button) dialog.findViewById(R.id.btn_save);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                currentDateAndTime = sdf.format(new Date());
                tv_expenses_date_time.setText(currentDateAndTime);

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String et_expense_amount_string, et_expense_note_string, tv_expenses_date_time_string;
                        et_expense_amount_string = et_expense_amount.getText().toString();
                        et_expense_note_string = et_expense_note.getText().toString();


                        if(!et_expense_amount_string.isEmpty() && !et_expense_note_string.isEmpty() ) {

                            insert_shop_expenses(et_expense_amount_string, et_expense_note_string, currentDateAndTime);
                            dialog.dismiss();
                            display_Shop_Expenses_Data();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }else{
                            Toast.makeText(getActivity(), "Enter ALL Field", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //-------------------------------------------------------------------------------


                dialog.show();
            }
        });
        //==================================================================================



        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_dashboard);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onResume() {
        super.onResume();

        display_Shop_Expenses_Data();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    //----------------------------------------------------------------------------------------------
    private void insert_shop_expenses(String expenses_amount, String expenses_note, String expenses_date_time){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseMain.SHOP_EXPENSES_AMOUNT, expenses_amount);
        cv.put(DatabaseMain.SHOP_EXPENSES_NOTE, expenses_note);
        cv.put(DatabaseMain.SHOP_EXPENSES_DATE_TIME_COLUMN, expenses_date_time);

        sqLiteDatabase = databaseMain.getWritableDatabase();
        Long recinsert = sqLiteDatabase.insert(DatabaseMain.SHOP_EXPENSES_TABLE_NAME, null, cv);
        if (recinsert != null) {
//            Toast.makeText(getActivity(), "successfully inserted data", Toast.LENGTH_SHORT).show();
            Log.i("test_response", "SHOP_EXPENSES_TABLE_NAME : successfully inserted data");
        } else {
            Toast.makeText(getActivity(), "Expenses_Table : something wrong try again", Toast.LENGTH_SHORT).show();
            Log.i("test_response", "Expenses_Table : something wrong try again");
        }
        sqLiteDatabase.close();

    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    private void display_Shop_Expenses_Data() {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_EXPENSES_TABLE_NAME+"",null);
        ArrayList<ExpensesModelData> modelArrayList=new ArrayList<>();
        internal_file_data = "";
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String expense_amount = cursor.getString(1);
            String expense_note = cursor.getString(2);
            String expense_date_time = cursor.getString(3);
            modelArrayList.add(new ExpensesModelData(id, expense_amount, expense_note, expense_date_time));
            internal_file_data = internal_file_data
                    +"\n"+"====EXPENSES====EXPENSES====EXPENSES====EXPENSES====\n"
                    +"\n"+"Id : "+id
                    +"\n"+"Date and Time : "+expense_date_time
                    +"\n"+"Expenses Amount : "+expense_amount
                    +"\n"+"Expenses Note : "+expense_note
                    +"\n ===========================================\n";
        }
        cursor.close();
        sqLiteDatabase.close();
        DashboardFragment.check_and_create_file(getActivity(), internal_file_data, MainActivity.file_name_skh_expenses);
        DashboardFragment.check_and_create_file_insdie_package(getActivity(), internal_file_data, MainActivity.file_name_skh_expenses);
        myAdapter=new ExpensesRecycleViewAdapter(getActivity(),R.layout.row_expenses_crud,modelArrayList,sqLiteDatabase);
        recyclerView.setAdapter(myAdapter);
    }

    //----------------------------------------------------------------------------------------------


}