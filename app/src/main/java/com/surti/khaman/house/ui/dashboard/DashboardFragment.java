package com.surti.khaman.house.ui.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment{

    public static ArrayList<String> page_wise_data_arraylist = new ArrayList<>();
    public static String KEY_FIXED_MENU_ALREADY_DISPLAY = "KEY_FIXED_MENU_ALREADY_DISPLAY";
    public static String KEY_BILL_NUMBER = "KEY_BILL_NUMBER";
    public static String KEY_LAST_DATE = "KEY_LAST_DATE";
    public static String KEY_OLD_BILL_FILE_DATA = "KEY_OLD_BILL_FILE_DATA";
    public static String KEY_OLD_BILL_FILE_STRING_DATA = "KEY_OLD_BILL_FILE_STRING_DATA";
    public static String KEY_OLD_EXPENSES_FILE_DATA = "KEY_OLD_EXPENSES_FILE_DATA";
    public static String KEY_PASSWORD = "KEY_PASSWORD";
    public static String KEY_LOGGED_IN_VALE = "KEY_LOGGED_IN_VALE";

    public static String internal_file_data = "";
    public static String previous_file_data = "";
    public static String bill_no_and__earning_history = "";
    public static Long todays_earning_data = 0l;

    public static int page_width = 420, page_height = 842;


    public static SQLiteDatabase sqLiteDatabase;
    public static DatabaseMain databaseMain;
    ArrayList<DashboaedModelData> dashboaedModelDataArrayList;

    private FragmentDashboardBinding binding;


    String final_bill_string = "";
    String final_file_string = "";
    String currentDateAndTime = "";
    Long grand_total = 0l;

    ArrayList<String> item_name_list ;
    ArrayList<String> item_weight_list ;
    ArrayList<String> item_price_list ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseMain=new DatabaseMain(getActivity());



        if(get_SharedPreference_FixedMenu(getActivity()) == "0") {
            set_SharedPreference_FixedMenu("1", getActivity());
            //------------------------------------------------------------------------------------------
            insert_fixed_Shop_Menu_Data("Vatidal Khaman", "1000", "200");
            insert_fixed_Shop_Menu_Data("Nylon Khaman", "1000", "200");
            insert_fixed_Shop_Menu_Data("White Dhokla", "1000", "200");
            insert_fixed_Shop_Menu_Data("Khandvi", "1000", "240");
            insert_fixed_Shop_Menu_Data("Jain Surti Khamni", "1000", "240");
            insert_fixed_Shop_Menu_Data("Lasun Surti Khamni", "1000", "240");
            insert_fixed_Shop_Menu_Data("Patra", "1000", "200");
            insert_fixed_Shop_Menu_Data("DalDryfruit Samosa", "1000", "240");
            insert_fixed_Shop_Menu_Data("Kaanda P Samosa", "1000", "240");
            insert_fixed_Shop_Menu_Data("Chinese P Samosa", "1000", "240");
            insert_fixed_Shop_Menu_Data("Jain Jambo Samosa", "1", "20");
            insert_fixed_Shop_Menu_Data("Aloo Jambo samosa", "1", "15");
            insert_fixed_Shop_Menu_Data("Dal Dahi Kachori", "1", "25");
            insert_fixed_Shop_Menu_Data("Tel Locho", "1", "35");
            insert_fixed_Shop_Menu_Data("Butter Locho", "1", "45");
            insert_fixed_Shop_Menu_Data("Cheese Locho", "1", "55");
            insert_fixed_Shop_Menu_Data("Schezwan Locho", "1", "55");
            insert_fixed_Shop_Menu_Data("Jalebi-DesiGhee", "1000", "360");
            insert_fixed_Shop_Menu_Data("Fafda", "1000", "360");
            insert_fixed_Shop_Menu_Data("Mohanthal-DesiGhee", "1000", "400");
            insert_fixed_Shop_Menu_Data("Bardoli Fried Patra", "1000", "200");
            insert_fixed_Shop_Menu_Data("Water Bottle Big", "1", "20");
            insert_fixed_Shop_Menu_Data("Water Bottle Small", "1", "10");
            //------------------------------------------------------------------------------------------
        }

        //------------------------------------------------------------------------------------------
        Dialog dialog = new Dialog(getActivity());

        displayData();
        RecyclerView dashboard_recycleView = (RecyclerView) root.findViewById(R.id.dashboard_recycleView);
        DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(dashboaedModelDataArrayList);
        dashboard_recycleView.setHasFixedSize(true);
        dashboard_recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        dashboard_recycleView.setAdapter(adapter);

        //------------------------------------------------------------------------------------------



        //------------------------------------------------------------------------------------------

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

                   tv_bill_no.setText(": "+get_SharedPreference_Billnumber(getActivity()));
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
                               set_SharedPreference_Billnumber(bill_no_integer + 1, getActivity());
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
                               insert_Shop_Revenue_Data(""+bill_no_integer, currentDateAndTime, final_file_string, ""+grand_total, getActivity());
                               //--------------------------------------------------------------------------------------------------------------------------------------------------


                               // Insert Into File
                               //-------------------------------------------------------------------------
                               display_Shop_Revenue_Data(getActivity());

                               previous_file_data = "";
                               if(get_SharedPreference_Old_data_bill_file(getActivity()) == 0) {
                                   set_SharedPreference_Old_data_bill_file(1, getActivity());
                                   String latest_old_bill_file_data = extract_pdf_text(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + MainActivity.file_name_surtikhamanhouse).getAbsolutePath(), getActivity());
                                   if(!latest_old_bill_file_data.isEmpty()) {
                                       previous_file_data =
                                               "\n======OLD_DATA_START====OLD_DATA_START====OLD_DATA_START======\n"
                                                       + latest_old_bill_file_data
                                                       + "\n=======OLD_DATA_END====OLD_DATA_END====OLD_DATA_END======\n";
                                   }
                                   set_SharedPreference_Old_data_bill_file_String(previous_file_data, getActivity());
                               }

                               previous_file_data = get_SharedPreference_Old_data_bill_file_String(getActivity());

                               if(!previous_file_data.isEmpty()){
                                   previous_file_data =  "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                                           +"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                                           +"\n"+previous_file_data+"\n"
                                           +"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                                           +"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
                               }

                               String final_file_data =
                                       previous_file_data
                                       +"\n\n\n\n =$+$+$+$+$+$+$+$+$= TODAY'S EARNING =$+$+$+$+$+$+$+$+$=\n"
                                       +"\n EARNING HISTORY \n"
                                       +"\n "+bill_no_and__earning_history
                                       +"\n -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
                                       +"\n TODAY's EARNING : "+todays_earning_data
                                       +"\n -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
                                       +"\n =$+$+$+$+$+$+$+$+$= TODAY'S EARNING =$+$+$+$+$+$+$+$+$=\n\n\n\n"
                                       +internal_file_data;


                               check_and_create_file(getActivity(), final_file_data, MainActivity.file_name_surtikhamanhouse);

                               check_and_create_file_insdie_package(getActivity(), final_file_data, MainActivity.file_name_surtikhamanhouse);
                               //-------------------------------------------------------------------------

                               binding.btnReset.performClick();
                           }


                       }
                   });

                   dialog.show();
               }
           });
        //------------------------------------------------------------------------------------------



        //==========================================================================================
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayData();
                RecyclerView dashboard_recycleView = (RecyclerView) root.findViewById(R.id.dashboard_recycleView);
                DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(dashboaedModelDataArrayList);
                dashboard_recycleView.setHasFixedSize(true);
                dashboard_recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                dashboard_recycleView.setAdapter(adapter);
            }
        });
        //==========================================================================================


        return root;
    }


    //----------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------
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

    @Override
    public void onResume() {
        super.onResume();


        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String currentDateandTime = sdf.format(new Date());
            String last_dat = get_last_day_for_bill_no_sharedpreference(getActivity());

            // Today's date is not same with last day, Reset Bill Number
            if(!currentDateandTime.equals(last_dat)){
                set_SharedPreference_Billnumber(1, getActivity());
            }
            set_last_day_for_bill_no_sharedpreference(currentDateandTime, getActivity());


        }catch (Exception e){
            e.getMessage();
        }
    }


    // Shop_Menu_Tabel
    //----------------------------------------------------------------------------------------------

    private void display_Shop_Menu_Data() {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_MENU_TABLE_NAME+"",null);

        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_weight = cursor.getString(2);
            String item_price = cursor.getString(3);

        }
        cursor.close();
    }
    //----------------------------------------------------------------------------------------------


    // shop_revenue_tabel
    //----------------------------------------------------------------------------------------------
    public static void insert_Shop_Revenue_Data(String bill_no, String bill_date_time, String item_name_weight_price, String bill_amount, Context context){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseMain.SHOP_REVENUE_BILL_NO_COLUMN, bill_no);
        cv.put(DatabaseMain.SHOP_REVENUE_BILL_DATE_TIME_COLUMN, bill_date_time);
        cv.put(DatabaseMain.SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN, item_name_weight_price);
        cv.put(DatabaseMain.SHOP_REVENUE_BILL_AMOUNT_COLUMN, bill_amount);

        sqLiteDatabase = databaseMain.getWritableDatabase();
        Long recinsert = sqLiteDatabase.insert(DatabaseMain.SHOP_REVENUE_TABLE_NAME, null, cv);
        if (recinsert != null) {
//            Toast.makeText(getActivity(), "successfully inserted data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "REVENUE_TABLE : something wrong try again", Toast.LENGTH_SHORT).show();
            Log.i("test_response", "REVENUE_TABLE : something wrong try again");
        }
    }

    public static void display_Shop_Revenue_Data(Context context) {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_REVENUE_TABLE_NAME+"",null);
        internal_file_data = "";

        Long todays_total_earning = 0l;
        bill_no_and__earning_history = "";
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String SHOP_REVENUE_BILL_NO_COLUMN = cursor.getString(1);
            String SHOP_REVENUE_BILL_DATE_TIME_COLUMN = cursor.getString(2);
            String SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN = cursor.getString(3);
            String SHOP_REVENUE_BILL_AMOUNT_COLUMN = cursor.getString(4);
            internal_file_data =  internal_file_data
                    +"\n (ID : "+id+") >>>>====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"\n"
                    +"\n Bill No : "+SHOP_REVENUE_BILL_NO_COLUMN
                    +"\n Date Time : "+SHOP_REVENUE_BILL_DATE_TIME_COLUMN
                    +"\n"+SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN
                    +"\n Grand Total : "+SHOP_REVENUE_BILL_AMOUNT_COLUMN
                    +"\n (ID : "+id+") >>>>====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"\n";

            Long grand_total_long = Long.parseLong(SHOP_REVENUE_BILL_AMOUNT_COLUMN);
            bill_no_and__earning_history = bill_no_and__earning_history
                    +"\n"
                    +SHOP_REVENUE_BILL_NO_COLUMN
                    +") "
                    + SHOP_REVENUE_BILL_DATE_TIME_COLUMN
                    +" ----> "
                    +SHOP_REVENUE_BILL_AMOUNT_COLUMN;
            todays_total_earning = todays_total_earning + grand_total_long;
        }

        todays_earning_data = todays_total_earning;
        cursor.close();
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    private void insert_fixed_Shop_Menu_Data(String itemName, String itemWeight, String itemPrice) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseMain.SHOP_MENU_ITEM_NAME_COLUMN, itemName);
        cv.put(DatabaseMain.SHOP_MENU_ITEM_WEIGHT_COLUMN, itemWeight);
        cv.put(DatabaseMain.SHOP_MENU_ITEM_PRICE_COLUMN, itemPrice);

        sqLiteDatabase = databaseMain.getWritableDatabase();
        Long recinsert = sqLiteDatabase.insert(DatabaseMain.SHOP_MENU_TABLE_NAME, null, cv);
        if (recinsert != null) {
//            Toast.makeText(getActivity(), "Fixed Menu Inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "MENU_TABLE : something wrong try again", Toast.LENGTH_SHORT).show();
            Log.i("test_response", "MENU_TABLE : something wrong try again");
        }
    }
        //----------------------------------------------------------------------------------------------


    // SharedPreference
    //--------------------------------------------------------------------------------------------------
      public static void set_SharedPreference_FixedMenu(String isFixedMenuAlreadyDisplay, Context context){
          SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_FIXED_MENU_ALREADY_DISPLAY, MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putString(KEY_FIXED_MENU_ALREADY_DISPLAY, isFixedMenuAlreadyDisplay);
          editor.commit();
    }


    public static String get_SharedPreference_FixedMenu(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_FIXED_MENU_ALREADY_DISPLAY, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIXED_MENU_ALREADY_DISPLAY, "0");
    }

    public static void set_SharedPreference_Old_data_bill_file(Integer value, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_BILL_FILE_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_OLD_BILL_FILE_DATA, value);
        editor.commit();
    }


    public static String get_SharedPreference_Old_data_bill_file_String(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_BILL_FILE_STRING_DATA, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OLD_BILL_FILE_STRING_DATA, "");
    }

    public static void set_SharedPreference_Old_data_bill_file_String(String value, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_BILL_FILE_STRING_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OLD_BILL_FILE_STRING_DATA, value);
        editor.commit();
    }


    public static Integer get_SharedPreference_Old_data_bill_file(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_BILL_FILE_DATA, MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_OLD_BILL_FILE_DATA, 0);
    }

    public static void set_SharedPreference_Old_data_expenses_file(Integer value, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_EXPENSES_FILE_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_OLD_EXPENSES_FILE_DATA, value);
        editor.commit();
    }


    public static Integer get_SharedPreference_Old_data_expenses_file(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_EXPENSES_FILE_DATA, MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_OLD_EXPENSES_FILE_DATA, 0);
    }



    public static void set_SharedPreference_Billnumber(Integer bill_no, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_BILL_NUMBER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_BILL_NUMBER, bill_no);
        editor.commit();
    }


    public static Integer get_SharedPreference_Billnumber(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_BILL_NUMBER, context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_BILL_NUMBER, 1);
    }

    public static void set_last_day_for_bill_no_sharedpreference(String password, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_LAST_DATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_DATE, password);
        editor.commit();
    }


    public static String get_last_day_for_bill_no_sharedpreference(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_LAST_DATE, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_DATE, "0");
    }

    public static void set_SharedPreference_Is_Successfully_Logged_In(Integer bill_no, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_LOGGED_IN_VALE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LOGGED_IN_VALE, bill_no);
        editor.commit();
    }


    public static Integer get_SharedPreference_Is_Successfully_Logged_In(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_LOGGED_IN_VALE, context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_LOGGED_IN_VALE, 0);
    }


    public static void set_password_sharedpreference(String password, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }


    public static String get_password_sharedpreference(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PASSWORD, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PASSWORD, "0");
    }
    //--------------------------------------------------------------------------------------------------


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // Call PDF File to call
    //==============================================================================================
    public static void check_and_create_file(Context context, String file_data, String file_name){
        File download_directory_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            createMyPDF(context, file_data, file_name, download_directory_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    //==============================================================================================

    // Call PDF File to call
    //==============================================================================================
    public static void check_and_create_file_insdie_package(Context context, String file_data, String file_name){
        File inside_package_file = get_inside_Package_File_object(context, file_name);
        try {
            createMyPDF_Inside_Package(context, file_data, file_name, inside_package_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    //==============================================================================================

    // Create PDF
    //==============================================================================================
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createMyPDF(Context context, String file_data, String file_name, File file) throws FileNotFoundException, DocumentException {

        try {
            //Create time stamp
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyy_MMM_dd_(HH:mm)").format(date);

            File download_directory_file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(download_directory_file, file_name);

            OutputStream output = new FileOutputStream(myFile);

            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter writer = PdfWriter.getInstance(document,output);

            writer.setEncryption(get_password_sharedpreference(context).getBytes(), get_password_sharedpreference(context).getBytes(), PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_40);
            writer.createXmpMetadata();

            //Step 3
            document.open();

            //Step 4 Add content
            document.add(new Paragraph(
                    "\n +_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+ \n"
                    +"FILE UPDATE TIME :"+timeStamp+
                    "\n +_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+ \n"
                    +file_data));

            //Step 5: Close the document
            document.close();
        }catch (Exception e){
            e.getMessage();
        }

    }
    //==============================================================================================

    // Create PDF
    //==============================================================================================
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createMyPDF_Inside_Package(Context context, String file_data, String file_name, File file) throws FileNotFoundException, DocumentException{


        try {
            //Create time stamp
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyy_MMM_dd_(HH:mm)").format(date);

            OutputStream output = new FileOutputStream(file);

            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter writer = PdfWriter.getInstance(document,output);

            writer.setEncryption(MainActivity.skh_phone_number.getBytes(), MainActivity.skh_phone_number.getBytes(), PdfWriter.ALLOW_COPY, PdfWriter.STANDARD_ENCRYPTION_40);
            writer.createXmpMetadata();

            //Step 3
            document.open();

            //Step 4 Add content
            document.add(new Paragraph(
                    "\n +_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+ \n"
                            +"FILE UPDATE TIME :"+timeStamp+
                            "\n +_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+ \n"
                            +file_data));


            //Step 5: Close the document
            document.close();
        }catch (Exception e){
            e.getMessage();
        }
    }

    //==============================================================================================


    // App Inside PATH : FILE OBJECT
    //----------------------------------------------------------------------------------------------
      public static File get_inside_Package_File_object(Context context, String file_name){
          ContextWrapper contextWrapper = new ContextWrapper(context);
          File downloadDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
          File file = new File(downloadDirectory, file_name);
          return file;
      }
    //----------------------------------------------------------------------------------------------



    // Send Mail
    //----------------------------------------------------------------------------------------------
    public static void sendEmail(Context context, String file_name) {
        String[] TO = {"surtikhamanhouseofficial@gmail.com"};
        String[] CC = {"mitesh.meet51@gmail.com", "9valangar0@gmail.com, valangar90@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        File myExternalFile;
        myExternalFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!myExternalFile.exists()) {
            myExternalFile.mkdir();
        }

        ContextWrapper contextWrapper = new ContextWrapper(context);

        File downloadDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadDirectory,file_name);

//        Uri file_surtikhaman_uri = Uri.fromFile(file);

        Uri file_surtikhaman_uri = FileProvider.getUriForFile(
                context,
                MainActivity.provider_name, //(use your app signature + ".provider" )
                file);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("vnd.android.cursor.dir/email");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent .putExtra(Intent.EXTRA_STREAM, file_surtikhaman_uri);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bill & Expenses");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Surti Khaman House Team, Please find below attachment");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("test_response", "MAIL SEND SEUUCESSFULLY !!!!");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            Log.i("test_response", "There is no email client installed : Exception, "+ ex.getMessage());
        }
    }

    //----------------------------------------------------------------------------------------------


    // File Page Wise Data
    //----------------------------------------------------------------------------------------------
          public static ArrayList<String> split_line_wise(String data){
              String[] array_total_lines = data.split("\r\n|\r|\n");
              int total_number_of_lines = array_total_lines.length;
              int fix_lines_of_one_page = 50;
              int requried_pages = total_number_of_lines / fix_lines_of_one_page;
              int last_remaining_lines = total_number_of_lines % fix_lines_of_one_page;
              int add_exclusive_lines = total_number_of_lines - last_remaining_lines;

              if (requried_pages == 0){
                  requried_pages = 1;
              }else if(last_remaining_lines != 0){
                  requried_pages = requried_pages + 1;
              }

              ArrayList<String> page_wise_data_list = new ArrayList<>();

//              Log.i("test_lines", "============ START =====================");
//
//              Log.i("test_lines", "total_number_of_lines : "+total_number_of_lines);
//              Log.i("test_lines", "fix_lines_of_one_page : "+fix_lines_of_one_page);
//              Log.i("test_lines", "requried_pages : "+requried_pages);
//              Log.i("test_lines", "last_remaining_lines : "+last_remaining_lines);
//              Log.i("test_lines", "add_exclusive_lines : "+add_exclusive_lines);

              String one_page_data = "";
              for (int j = 0; j < total_number_of_lines; j++){

                  if(total_number_of_lines < fix_lines_of_one_page){
//                      Log.i("test_lines", "Less than 50 Lines ADD LINE NUMBER : "+j);

                      one_page_data = one_page_data + "\n" + array_total_lines[j];

                      if(j == (total_number_of_lines - 1)){

//                          Log.i("test_lines", "$$$$$$$ DATA : "+one_page_data);
//                          Log.i("test_lines", "=-=-=-=-=-=-=-=-=-= TIME TO ADD INTO ARRAY LIST,Less than 50 Lines, ADD TO ARRAY LIST : "+j);

                          page_wise_data_list.add(one_page_data);
                          one_page_data = "";
                      }
                  }else if( (j == add_exclusive_lines) && (j > add_exclusive_lines)){
                      one_page_data = one_page_data + "\n" + array_total_lines[j];
                      if(j == (total_number_of_lines - 1)){
//                          Log.i("test_lines", "$$$$$$$ DATA : "+one_page_data);
//                          Log.i("test_lines", "=-=-=-=-=-=-=-=-=-= TIME TO ADD INTO ARRAY LIST, PAGE REMAINING LINES, ADD TO ARRAY LIST : "+j);

                          page_wise_data_list.add(one_page_data);
                          one_page_data = "";
                      }
                  }else {
                      if (j % 50 == 0) {
//                          Log.i("test_lines", "$$$$$$$ DATA : "+one_page_data);
//                          Log.i("test_lines", "=-=-=-=-=-=-=-=-=-= TIME TO ADD INTO ARRAY LIST, DIVISIBLE BY 50, ADD TO ARRAY LIST : "+j);

                          one_page_data = one_page_data + "\n" + array_total_lines[j];
                          page_wise_data_list.add(one_page_data);
                          one_page_data = "";
                      } else {
                          one_page_data = one_page_data + "\n" + array_total_lines[j];
                      }
                  }

                  if(total_number_of_lines > fix_lines_of_one_page && (j > (total_number_of_lines - last_remaining_lines))){
                      if(j == total_number_of_lines - 1){
                          one_page_data = one_page_data + "\n" + array_total_lines[j];
//
//                          Log.i("test_lines", "$$$$$$$ REMAINING DATA : "+one_page_data);
//                          Log.i("test_lines", "=-=-=-=-=-=-=-=-=-= TIME TO ADD INTO ARRAY LIST, DIVISIBLE BY 50, ADD TO ARRAY LIST : "+j);

                          page_wise_data_list.add(one_page_data);
                      }
                  }
              }


//              Log.i("test_lines", "============ FINISHED ===================== PAGE SIZE : "+page_wise_data_list.size());

              return page_wise_data_list;
          }
    //----------------------------------------------------------------------------------------------


    // Read File
    //---------------------------------------------------------------------------------------------------------------------
    public static InputStream inputStream;
    public static String extract_pdf_text(String fileName, Context context) {

        String old_file_content = "";
        PdfReader reader = null;
        try {
            reader = new PdfReader(fileName, get_password_sharedpreference(context).getBytes());

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                // pageNumber = 1
                String textFromPage = PdfTextExtractor.getTextFromPage(reader, i);
                old_file_content = old_file_content + "\n" + textFromPage;
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("Test_lines", "" + old_file_content);
        return old_file_content;
    }
    //---------------------------------------------------------------------------------------------------------------------


    // Delete File
    //-----------------------------------------------------------------------------------------------------------------------
    public static void delete_file_from_download(Context context, String filename){
        try {
            File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadDirectory,filename);
            if(file.exists()) {
                file.delete();
            }
            Toast.makeText(context, ""+context.getResources().getString(R.string.delete_file_success), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(context, ""+context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------


    // Delete File
    //-----------------------------------------------------------------------------------------------------------------------
    public static void show_delete_passord_popup(Context context, String file_name){
//--------------------------------------------------------------------------------------------------

        Dialog dialog = new Dialog(context);
                //==================================================================================
                dialog.setContentView(R.layout.password_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                EditText et_password;
                TextView tv_dynamic_result;
                Button btn_0, btn_1, btn_2,
                        btn_3, btn_4, btn_5,
                        btn_6, btn_7, btn_8,
                        btn_9, btn_clear, btn_info, btn_close_popup;

        tv_dynamic_result = (TextView) dialog.findViewById(R.id.tv_dynamic_result);
        et_password = (EditText) dialog.findViewById(R.id.et_password);;
        btn_0 = (Button) dialog.findViewById(R.id.btn_0);
        btn_1 = (Button) dialog.findViewById(R.id.btn_1);
        btn_2 = (Button) dialog.findViewById(R.id.btn_2);
        btn_3 = (Button) dialog.findViewById(R.id.btn_3);
        btn_4 = (Button) dialog.findViewById(R.id.btn_4);
        btn_5 = (Button) dialog.findViewById(R.id.btn_5);
        btn_6 = (Button) dialog.findViewById(R.id.btn_6);
        btn_7 = (Button) dialog.findViewById(R.id.btn_7);
        btn_8 = (Button) dialog.findViewById(R.id.btn_8);
        btn_9 = (Button) dialog.findViewById(R.id.btn_9);
        btn_close_popup = (Button) dialog.findViewById(R.id.btn_close_popup);
        btn_clear = (Button) dialog.findViewById(R.id.btn_clear);
        btn_info = (Button) dialog.findViewById(R.id.btn_info);


        //------------------------------------------------------------------------------------------
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+context.getResources().getString(R.string.info_password), Toast.LENGTH_LONG).show();
            }
        });

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_0, et_password, context, dialog, tv_dynamic_result);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_1, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_2, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_3, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_4, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_5, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_6, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_7, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_8, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_9, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_0, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password = "";
                et_password.setText("");
            }
        });

        btn_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.dismiss();
            }
        });
        //------------------------------------------------------------------------------------------

                dialog.show();
    }


     public static String entering_password = "";
    public static void entering_password_for_calculation(Button btn, EditText et_password, Context context, Dialog dialog, TextView tv_dynamic_result) {
        entering_password = entering_password + btn.getText().toString();

        et_password.setText(entering_password);


        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input_password = charSequence.toString();
                if(i2 == 10) {
                    if (checkPasswordValidation(input_password, context)) {
                        delete_file_from_download(context, MainActivity.file_name_surtikhamanhouse);
                        dialog.dismiss();
                    } else {
                        entering_password = "";
                        tv_dynamic_result.setText(context.getResources().getString(R.string.wrong_password));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable input) {

            }
        });
    }

    private static boolean checkPasswordValidation(String entering_password, Context context) {
        boolean result =false;
        if (!entering_password.isEmpty() && entering_password != null) {
            if (entering_password.equals(get_password_sharedpreference(context))) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
    //------------------------------------------------------------------------------------------------------------------------

}
