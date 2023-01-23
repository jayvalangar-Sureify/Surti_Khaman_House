package com.surti.khaman.house.ui.dashboard;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardFragment extends Fragment implements DashboardInterface {

    private FragmentDashboardBinding binding;
    DashboardInterface dashboardInterface;


    Long grand_total = 0l;

    ArrayList<String> item_name_list ;
    ArrayList<String> item_weight_list ;
    ArrayList<String> item_price_list ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


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
                   String currentDateAndTime = sdf.format(new Date());
                   TextView tv_bill_no, tv_date_time;
                   tv_bill_no = (TextView) dialog.findViewById(R.id.tv_bill_no_value);
                   tv_date_time = (TextView) dialog.findViewById(R.id.tv_date_time_value);

                   tv_bill_no.setText(": "+"001");
                   tv_date_time.setText(": "+currentDateAndTime);
                   //-------------------------------------------------------------------------------


                   Button btn_close = dialog.findViewById(R.id.btn_close_popup);

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
