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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.WorkerDirectory.UploadPDF;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment{

    public static ArrayList<String> page_wise_data_arraylist = new ArrayList<>();
    public static String KEY_FIXED_MENU_ALREADY_DISPLAY = "KEY_FIXED_MENU_ALREADY_DISPLAY";
    public static String KEY_BILL_NUMBER = "KEY_BILL_NUMBER";
    public static String KEY_OLD_FILE_DATA = "KEY_OLD_FILE_DATA";

    public static String internal_file_data = "";
    public static String previous_file_data = "";

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
                               insert_Shop_Revenue_Data(""+bill_no_integer, currentDateAndTime, final_file_string, ""+grand_total, getActivity());
                               //--------------------------------------------------------------------------------------------------------------------------------------------------


                               // Insert Into File
                               //-------------------------------------------------------------------------
                               display_Shop_Revenue_Data(getActivity());

                               check_and_create_file(getActivity(), internal_file_data, MainActivity.file_name_surtikhamanhouse);

                               check_and_create_file_insdie_package(getActivity(), internal_file_data, MainActivity.file_name_surtikhamanhouse);
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


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UploadPDF.myWorkManager(getActivity());
                }
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
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String SHOP_REVENUE_BILL_NO_COLUMN = cursor.getString(1);
            String SHOP_REVENUE_BILL_DATE_TIME_COLUMN = cursor.getString(2);
            String SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN = cursor.getString(3);
            String SHOP_REVENUE_BILL_AMOUNT_COLUMN = cursor.getString(4);
            internal_file_data =  internal_file_data
                    +"\n====Bill====Bill====Bill====Bill====Bill====Bill==\n"
                    +"\n Bill No : "+SHOP_REVENUE_BILL_NO_COLUMN
                    +"\n Date Time : "+SHOP_REVENUE_BILL_DATE_TIME_COLUMN
                    +"\n"+SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN
                    +"\n Grand Total : "+SHOP_REVENUE_BILL_AMOUNT_COLUMN
                    +"\n ===========================================\n";
        }

        if(get_SharedPreference_Old_data(context) == 0) {
            previous_file_data =
                    "\n====OLD====OLD====OLD====OLD====OLD====OLD==\n"
                            + extract_pdf_text(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + MainActivity.file_name_surtikhamanhouse).getAbsolutePath())
                            + "\n====OLD====OLD====OLD====OLD====OLD====OLD==\n";
        }

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

    public static void set_SharedPreference_Old_data(Integer bill_no, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_FILE_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_OLD_FILE_DATA, bill_no);
        editor.commit();
    }


    public static Integer get_SharedPreference_Old_data(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_OLD_FILE_DATA, MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_OLD_FILE_DATA, 0);
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
        createMyPDF(context, file_data, file_name, download_directory_file);
    }
    //==============================================================================================

    // Call PDF File to call
    //==============================================================================================
    public static void check_and_create_file_insdie_package(Context context, String file_data, String file_name){
        File inside_package_file = get_inside_Package_File_object(context, file_name);
        createMyPDF_Inside_Package(context, file_data, file_name, inside_package_file);
    }
    //==============================================================================================

    // Create PDF
    //==============================================================================================
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createMyPDF(Context context, String file_data, String file_name, File file) {

        page_wise_data_arraylist = split_line_wise(previous_file_data + "\n" + file_data);

        //Create the pdf page
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        //=============================================================================================================================

        for (int i = 0; i < page_wise_data_arraylist.size(); i++){

            //Initialize top and left margin for text
            int page1_x = 10, page1_y = 25;

            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(page_width, page_height, 1).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

            Log.i("test_page", i+") "+page_wise_data_arraylist.get(i));

            for (String line : page_wise_data_arraylist.get(i).split("\n")) {
                myPage1.getCanvas().drawText(line, page1_x, page1_y, myPaint);
                page1_y += myPaint.descent() - myPaint.ascent();
            }

            myPdfDocument.finishPage(myPage1);

        }

//=============================================================================================================================



        //Initialize the file with the name and path
        File myExternalFile = new File(file, file_name);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myExternalFile));
///            Toast.makeText(context, file_name+" : FILE SAVED", Toast.LENGTH_SHORT).show();
            Log.i("test_response", file_name+" : FILE SAVED");
        } catch (Exception e) {
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            Toast.makeText(context, file_name+" : Exception , "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("test_response", file_name+" : Exception , "+e.getMessage());
        }
        myPdfDocument.close();
    }
    //==============================================================================================

    // Create PDF
    //==============================================================================================
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createMyPDF_Inside_Package(Context context, String file_data, String file_name, File file) {
        //Create the pdf page
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        //=============================================================================================================================

        for (int i = 0; i < page_wise_data_arraylist.size(); i++){

            //Initialize top and left margin for text
            int page1_x = 10, page1_y = 25;
            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(page_width, page_height, 1).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

            for (String line : page_wise_data_arraylist.get(i).split("\n")) {
                myPage1.getCanvas().drawText(line, page1_x, page1_y, myPaint);
                page1_y += myPaint.descent() - myPaint.ascent();
            }

            myPdfDocument.finishPage(myPage1);

        }

//=============================================================================================================================

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
            //            Toast.makeText(context, file_name+" : FILE SAVED", Toast.LENGTH_SHORT).show();
            Log.i("test_response", file_name+" : FILE SAVED");
        } catch (Exception e) {
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            Toast.makeText(context, file_name+" : Exception, "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("test_response", file_name+" : Exception, "+e.getMessage());
        }
        myPdfDocument.close();
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
        String[] CC = {"9valangar0@gmail.com, valangar90@gmail.com"};
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


    public static InputStream inputStream;
    public static String extract_pdf_text(String fileName){
        String content = "";
        PdfReader reader = null;
        try {
            //String fileName is the string with the path to your .pdf file, for example resources/pdfs/preface.pdf
            reader = new PdfReader(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int numberOfPages = 0;
        try{
             numberOfPages = reader.getNumberOfPages();
        }catch (Exception e){
            e.getMessage();
        }

        numberOfPages = numberOfPages + 1;
        for (int page = 1; page < numberOfPages; page++){
            try {
                String content1Page = PdfTextExtractor.getTextFromPage(reader, page);
                content = content + content1Page;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Log.i("Test_lines",""+content);
        return content;
    }
}
