package com.surti.khaman.house.ui.box_dashboard;

import static com.surti.khaman.house.ui.dashboard.DashboardFragment.get_SharedPreference_Billnumber;
import static com.surti.khaman.house.ui.dashboard.DashboardFragment.internal_file_data;
import static com.surti.khaman.house.ui.dashboard.DashboardFragment.set_SharedPreference_Billnumber;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.surti.khaman.house.Adapter.BoxDashboardRecycleViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentBoxDashboardBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BoxDashboardFragment extends Fragment{

    RecyclerView rv_box_dashboard;
    SQLiteDatabase sqLiteDatabase;
    DatabaseMain databaseMain;
    ArrayList<DashboaedModelData> dashboaedModelDataArrayList;

    private FragmentBoxDashboardBinding binding;

    ArrayList<String> item_name_list ;
    ArrayList<String> item_weight_list ;
    ArrayList<String> item_price_list ;

    String final_bill_string = "";
    String final_file_string = "";
    String currentDateAndTime = "";
    Long grand_total = 0l;

//=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBoxDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseMain=new DatabaseMain(getActivity());
        rv_box_dashboard = (RecyclerView) root.findViewById(R.id.rv_box_dashboard);

        //--------------
        displayData();
        //--------------

        //==========================================================================================
        BoxDashboardRecycleViewAdapter adapter = new BoxDashboardRecycleViewAdapter(dashboaedModelDataArrayList, getActivity());
        rv_box_dashboard.setHasFixedSize(true);
        rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_box_dashboard.setAdapter(adapter);
        //==========================================================================================



        //==========================================================================================
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayData();
                rv_box_dashboard = (RecyclerView) root.findViewById(R.id.rv_box_dashboard);
                BoxDashboardRecycleViewAdapter adapter = new BoxDashboardRecycleViewAdapter(dashboaedModelDataArrayList, getActivity());
                rv_box_dashboard.setHasFixedSize(true);
                rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_box_dashboard.setAdapter(adapter);
            }
        });
        //==========================================================================================



        //------------------------------------------------------------------------------------------
        Dialog dialog = new Dialog(getActivity());
        binding.btnViewReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setContentView(R.layout.receipt_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                currentDateAndTime = sdf.format(new Date());
                TextView tv_bill_no, tv_date_time;
                tv_bill_no = (TextView) dialog.findViewById(R.id.tv_bill_no_value);
                tv_date_time = (TextView) dialog.findViewById(R.id.tv_date_time_value);

                tv_bill_no.setText(": "+ get_SharedPreference_Billnumber(getActivity()));
                tv_date_time.setText(": "+currentDateAndTime);
                //-------------------------------------------------------------------------------


                Button btn_close = dialog.findViewById(R.id.btn_close_popup);
                Button btn_print = dialog.findViewById(R.id.btn_print);

                TextView tv_cash = dialog.findViewById(R.id.tv_switch_cash);
                TextView tv_online = dialog.findViewById(R.id.tv_switch_online);

                final String[] final_payment_method = {" - Cash"};

                tv_cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final_payment_method[0] = " - Cash";
                        tv_cash.setTextColor(getResources().getColor(R.color.white));
                        tv_cash.setBackgroundColor(getResources().getColor(R.color.green_300));
                        tv_cash.setTypeface(Typeface.DEFAULT_BOLD);
                        tv_online.setTypeface(Typeface.DEFAULT);
                        tv_online.setTextColor(getResources().getColor(R.color.red_500));
                        tv_online.setBackgroundColor(getResources().getColor(R.color.grey_100));
                    }
                });

                tv_online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final_payment_method[0] = " - Online";
                        tv_cash.setTextColor(getResources().getColor(R.color.red_500));
                        tv_cash.setBackgroundColor(getResources().getColor(R.color.grey_100));
                        tv_cash.setTypeface(Typeface.DEFAULT);
                        tv_online.setTypeface(Typeface.DEFAULT_BOLD);
                        tv_online.setTextColor(getResources().getColor(R.color.white));
                        tv_online.setBackgroundColor(getResources().getColor(R.color.green_300));
                    }
                });


                item_name_list = new ArrayList<>();
                item_weight_list = new ArrayList<>();
                item_price_list = new ArrayList<>();

                for(int i = 0; i < dashboaedModelDataArrayList.size(); i++){

                    if((dashboaedModelDataArrayList.get(i).getAmount() != null) && !dashboaedModelDataArrayList.get(i).getAmount().isEmpty()) {
                        if((Long.parseLong(dashboaedModelDataArrayList.get(i).getAmount()) > 0l)) {
                            String item_name = dashboaedModelDataArrayList.get(i).getItem_name();
                            String item_weight = dashboaedModelDataArrayList.get(i).getWeight();
                            String item_price = dashboaedModelDataArrayList.get(i).getPrice();

                            item_name_list.add(item_name);
                            item_weight_list.add(item_weight);
                            item_price_list.add(item_price);
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        ArrayList<Long> grand_total_list = new ArrayList<>();

                        for(String s : item_price_list) grand_total_list.add(Long.valueOf(s));


                        grand_total = 0l;

                        for (Long number : grand_total_list){
                            grand_total += number;
                        }

                        for (int i = 0; i <= item_name_list.size(); i++){

                            String item_name = item_name_list.get(i);
                            String item_weight = item_weight_list.get(i);
                            String item_price = item_price_list.get(i);

                            if(item_price.length() == 1) {
                                item_price = "    "+item_price;
                            }else if(item_price.length() == 2){
                                item_price = "   "+item_price;
                            }else if(item_price.length() == 3){
                                item_price = "  "+item_price;
                            }else if(item_price.length() == 4){
                                item_price = " "+item_price;
                            }

                            if(i == 0 ){
                                final_bill_string =  "\n[L]"+item_name+"[R]"+item_weight+"    "+item_price+"\n";
                                final_file_string = "\n["+item_name+", "+item_weight +", "+item_price + " Rs]\n";
                            }else if(i == (item_name_list.size() - 1)){
                                final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price;
                                final_file_string = final_file_string + ", ["+item_name+", "+item_weight +","+item_price + " Rs]\n";
                            }else{
                                final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price+"\n";
                                final_file_string = final_file_string + ", ["+item_name+", "+item_weight +","+item_price + " Rs]\n";
                            }



                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

//-------------------------------------------------------------------------------------------------------------
                RecyclerView dashboard_recycleView = (RecyclerView) dialog.findViewById(R.id.rv_receipt);
                ReceiptPopupRecycleViewAdapter adapter = new ReceiptPopupRecycleViewAdapter(item_name_list, item_weight_list, item_price_list);
//                   dashboard_recycleView.setHasFixedSize(true);
                dashboard_recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                dashboard_recycleView.setAdapter(adapter);
//-------------------------------------------------------------------------------------------------------------


                TextView tv_final_receipt_total = (TextView) dialog.findViewById(R.id.tv_final_receipt_total);
                tv_final_receipt_total.setText("Total = "+grand_total);




                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
                        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
                        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
                        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
                        } else {
                            Log.i("test_print", "Run bussiness logic : "+ final_payment_method[0]);
                            Log.i("test_print", "final_bill_string :- "+final_bill_string);
                            Log.i("test_print", "grand_total :- "+grand_total);

                            // Generate Bill Number
                            //------------------------------------------------------------------------
                            int bill_no_integer = get_SharedPreference_Billnumber(getActivity());
                            if(bill_no_integer <= 100){
                                set_SharedPreference_Billnumber(bill_no_integer + 1, getActivity());
                            }else{
                                set_SharedPreference_Billnumber(1, getActivity());
                            }
                            //------------------------------------------------------------------------


                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    // Print Bill
                                    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                                    // Your code HERE
                                    try {

                                        if(item_name_list.size() != 0){
                                            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
                                            printer
                                                    .printFormattedText(
                                                            "[C]<font size='normal'><b>SURTI KHAMAN HOUSE</b></font>\n" +
                                                                    "[C]<font size='normal'>BORIVALI (EAST)</font>\n" +
                                                                    "[C]<font size='normal'>Mobile. 9137272150</font>\n" +
                                                                    "[L] Bill No : <font size='big'><b>" + bill_no_integer + "</b></font> "+final_payment_method[0]+"\n" +
                                                                    "[L]<font size='normal'> Date-Time : " + currentDateAndTime + "</font>\n" +
                                                                    "[L]<font size='normal'> Fssai : " + "21522012000953" + "</font>\n" +
                                                                    "[C]================================\n" +
                                                                    "[L] Items" +
                                                                    "[R] Weight" + "    Price\n" +
                                                                    "[L]<font size='small'>" + final_bill_string + "</font>\n" +
                                                                    "[C]================================\n" +
                                                                    "[C]GRAND TOTAL : <font size='big'><b>" + grand_total + "</b></font>\n" +
                                                                    "[C]================================"
                                                    );
                                        }else{

                                        }
                                        try{
                                            dialog.dismiss();
                                        }catch (Exception e){
                                            e.getMessage();
                                            Log.i("test_print", "Exception dialog dismiss:"+e.getMessage());
                                        }
                                    } catch (EscPosConnectionException e) {
                                        e.printStackTrace();
                                        Log.i("test_print", "Exception 1 :- "+e.getMessage());
                                    } catch (EscPosParserException e) {
                                        e.printStackTrace();
                                        Log.i("test_print", "Exception 2 :- "+e.getMessage());
                                    } catch (EscPosEncodingException e) {
                                        e.printStackTrace();
                                        Log.i("test_print", "Exception 3 :- "+e.getMessage());
                                    } catch (EscPosBarcodeException e) {
                                        e.printStackTrace();
                                        Log.i("test_print", "Exception 4 :- "+e.getMessage());
                                    }


                                }
                                //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

                            });

                            // Insert Into Database
                            //-------------------------------------------------------------------------------------------------------------------------------------------------
                            DashboardFragment.insert_Shop_Revenue_Data(""+bill_no_integer, currentDateAndTime, final_file_string, ""+grand_total, getActivity());
                            //--------------------------------------------------------------------------------------------------------------------------------------------------


                            // Insert Into File
                            //-------------------------------------------------------------------------
                            DashboardFragment.display_Shop_Revenue_Data(getActivity());

                            DashboardFragment.check_and_create_file(getActivity(), internal_file_data, MainActivity.file_name_surtikhamanhouse);

                            DashboardFragment.check_and_create_file_insdie_package(getActivity(), internal_file_data, MainActivity.file_name_surtikhamanhouse);
                            //-------------------------------------------------------------------------

                            binding.btnReset.performClick();
                        }


                    }
                });

                dialog.show();
            }
        });
        //------------------------------------------------------------------------------------------



        return root;
    }
//=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    // Display Database Data
    //----------------------------------------------------------------------------------------------
    private void displayData() {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_MENU_TABLE_NAME+"",null);
        dashboaedModelDataArrayList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_weight = cursor.getString(2);
            String item_price = cursor.getString(3);
            dashboaedModelDataArrayList.add(new DashboaedModelData(item_name, "", "", "",  item_price, item_weight));
        }
        cursor.close();
    }
    //----------------------------------------------------------------------------------------------
}
