package com.surti.khaman.house.ui.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
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
import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment implements DashboardInterface {

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


        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------
        Dialog dialog = new Dialog(getActivity());


        //------------------------------------------------------------------------------------------

        DashboaedModelData[] myListData = new DashboaedModelData[]{
                new DashboaedModelData("Khaman", "", "", "", "200", "1000"),
                new DashboaedModelData("N.Khaman ", "", "", "", "200", "1000"),
                new DashboaedModelData("Dhokla", "", "", "", "200", "1000"),
                new DashboaedModelData("Patra", "", "", "", "200", "1000"),
                new DashboaedModelData("Khandvi", "", "", "", "240", "1000"),
                new DashboaedModelData("Khamni", "", "", "", "240", "1000"),
                new DashboaedModelData("Gathiya", "", "", "", "240", "1000"),
                new DashboaedModelData("Sev", "", "", "", "240", "1000"),
                new DashboaedModelData("Fafda", "", "", "", "360", "1000"),
                new DashboaedModelData("Jalebi", "", "", "", "360", "1000"),
                new DashboaedModelData("P.samosa", "", "", "", "240", "1000"),
                new DashboaedModelData("Samosa", "", "", "", "15", "1"),
                new DashboaedModelData("Kachori", "", "", "", "15", "1")};


        RecyclerView dashboard_recycleView = (RecyclerView) root.findViewById(R.id.dashboard_recycleView);
        DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(myListData, dashboardInterface);
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

                   for(int i = 0; i < myListData.length; i++){

                       if((myListData[i].getAmount() != null) && !myListData[i].getAmount().isEmpty()) {
                           if((Long.parseLong(myListData[i].getAmount()) > 0l)) {
                               String item_name = myListData[i].getItem_name();
                               String item_weight = myListData[i].getWeight();
                               String item_price = myListData[i].getPrice();

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
                               if(i == 0 ){
                                   final_bill_string =  "\n[L]"+item_name_list.get(i)+"[C]"+item_weight_list.get(i)+"[R]"+item_price_list.get(i)+"\n";
                               }else{
                                   final_bill_string = final_bill_string + "[L]"+item_name_list.get(i)+"[C]"+item_weight_list.get(i)+"[R]"+item_price_list.get(i)+"\n";
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

                   String final_bill_string1 = "Jay";
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
                                                           "[L]\n" +
                                                           "[C]<u><font size='normal'><b>SURTI KHAMAN HOUSE</b></font></u>\n" +
                                                           "[C]<u><font size='normal'>BORIVALI (EAST)</font></u>\n" +
                                                           "[C]<u><font size='normal'>Mobile. 9137272150</font></u>\n" +
                                                           "[L]\n" +
                                                           "[L] Bill No : "+"001"+"\n" +
                                                           "[L] Date-Time : "+currentDateAndTime+"\n" +
                                                           "[L] Fssai : "+"21522012000953"+"\n" +
                                                           "[C]================================\n" +
                                                           "[L] Items" +
                                                           "[C] Weight" +
                                                           "[R] Price \n" +
                                                           "[L]"+final_bill_string +"\n"+
                                                           "[C]--------------------------------\n" +
                                                           "[L]GRAND TOTAL :[R]<font size='big'><b>"+grand_total+"</b></font>\n" +
                                                           "[L]\n" +
                                                           "[C]================================\n" +
                                                           "[L]\n"
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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTotalAmountChange(String totalAmount) {

    }
}
