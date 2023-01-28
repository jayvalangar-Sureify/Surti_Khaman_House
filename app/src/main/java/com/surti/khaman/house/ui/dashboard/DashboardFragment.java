package com.surti.khaman.house.ui.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment implements DashboardInterface {

    SQLiteDatabase sqLiteDatabase;
    DatabaseMain databaseMain;
    ArrayList<DashboaedModelData> dashboaedModelDataArrayList;

    private FragmentDashboardBinding binding;
    DashboardInterface dashboardInterface;


    String final_bill_string = "";
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


        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------
        Dialog dialog = new Dialog(getActivity());


        //------------------------------------------------------------------------------------------

//        DashboaedModelData[] myListData = new DashboaedModelData[]{
//                new DashboaedModelData("Vatidal Khaman", "", "", "", "200", "1000"),
//                new DashboaedModelData("Nylon Khaman", "", "", "", "200", "1000"),
//                new DashboaedModelData("White Dhokla", "", "", "", "200", "1000"),
//                new DashboaedModelData("Jain Khamni", "", "", "", "240", "1000"),
//                new DashboaedModelData("Lasan Khamni", "", "", "", "240", "1000"),
//                new DashboaedModelData("Patra", "", "", "", "200", "1000"),
//                new DashboaedModelData("Khandvi", "", "", "", "240", "1000"),
//                new DashboaedModelData("Dal Patti Samosa", "", "", "", "240", "1000"),
//                new DashboaedModelData("Kanda Patti Samosa", "", "", "", "240", "1000"),
//                new DashboaedModelData("Jain Jumbo Samosa", "", "", "", "20", "1"),
//                new DashboaedModelData("Jambo Samosa", "", "", "", "15", "1"),
//                new DashboaedModelData("Dal Dahi Kachori", "", "", "", "25", "1"),
//                new DashboaedModelData("Oil Locho", "", "", "", "35", "1"),
//                new DashboaedModelData("Butter Locho", "", "", "", "45", "1"),
//                new DashboaedModelData("Cheese Locho", "", "", "", "55", "1"),
//                new DashboaedModelData("Schezwan Locho", "", "", "", "55", "1"),
//                new DashboaedModelData("Jalebi", "", "", "", "360", "1000"),
//                new DashboaedModelData("Fafda", "", "", "", "360", "1000"),
//                new DashboaedModelData("Sev", "", "", "", "240", "1000"),
//                new DashboaedModelData("Papdi", "", "", "", "300", "1000"),
//                new DashboaedModelData("Gathiya", "", "", "", "300", "1000"),
//                new DashboaedModelData("Mohanthal", "", "", "", "400", "1000"),
//                new DashboaedModelData("Small Water Bottle", "", "", "", "10", "1"),
//                new DashboaedModelData("Big Water Bottle", "", "", "", "20", "1")
//
//            };


        displayData();
        RecyclerView dashboard_recycleView = (RecyclerView) root.findViewById(R.id.dashboard_recycleView);
        DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(dashboaedModelDataArrayList, dashboardInterface);
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

                   tv_bill_no.setText(": "+"001");
                   tv_date_time.setText(": "+currentDateAndTime);
                   //-------------------------------------------------------------------------------


                   Button btn_close = dialog.findViewById(R.id.btn_close_popup);
                   Button btn_print = dialog.findViewById(R.id.btn_print);
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
                               }else if(i == (item_name_list.size() - 1)){
                                   final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price;
                               }else{
                                   final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price+"\n";
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
                               Log.i("test_print", "Run bussiness logic");
                               Log.i("test_print", "final_bill_string :- "+final_bill_string);
                               Log.i("test_print", "grand_total :- "+grand_total);


                               // Your code HERE
                               try {
                                   EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);


                                   printer
                                           .printFormattedText(
                                                           "[C]<u><font size='normal'><b>SURTI KHAMAN HOUSE</b></font></u>\n" +
                                                           "[C]<u><font size='normal'>BORIVALI (EAST)</font></u>\n" +
                                                           "[C]<u><font size='normal'>Mobile. 9137272150</font></u>\n" +
                                                           "[L] Bill No : <font size='big'><b>"+"001"+"</b></font>\n" +
                                                           "[L] Date-Time : "+currentDateAndTime+"\n" +
                                                           "[L] Fssai : "+"21522012000953"+"\n" +
                                                           "[C]================================\n" +
                                                           "[L] Items" +
                                                           "[R] Weight" + "    Price\n" +
                                                           "[L]"+final_bill_string +"\n"+
                                                           "[C]================================\n" +
                                                           "[C]GRAND TOTAL : <font size='big'><b>"+grand_total+"</b></font>\n" +
                                                           "[C]================================"
                                           );
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
                       }
                   });

                   dialog.show();
               }
           });
        //------------------------------------------------------------------------------------------





        return root;
    }


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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTotalAmountChange(String totalAmount) {

    }
}
