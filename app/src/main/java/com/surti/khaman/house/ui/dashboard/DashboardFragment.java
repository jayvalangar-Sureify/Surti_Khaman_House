package com.surti.khaman.house.ui.dashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;

import java.util.ArrayList;


public class DashboardFragment extends Fragment implements DashboardInterface {

    private FragmentDashboardBinding binding;
    DashboardInterface dashboardInterface;


    String grand_total = "0 Rs";

    ArrayList<String> item_name_list ;
    ArrayList<String> item_weight_list ;
    ArrayList<String> item_price_list ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardInterface = new DashboardInterface() {
            @Override
            public void onTotalAmountChange(String totalAmount) {
                binding.tvTotalAmount.setText(totalAmount + " RS");
                grand_total = totalAmount + "Rs";

            }
        };

        Dialog dialog = new Dialog(getActivity());


        //------------------------------------------------------------------------------------------

        DashboaedModelData[] myListData = new DashboaedModelData[]{
                new DashboaedModelData("Khaman", "0", "0", "0", "200"),
                new DashboaedModelData("N.Khaman ", "0", "0", "0", "200"),
                new DashboaedModelData("Dhokla", "0", "0", "0", "200"),
                new DashboaedModelData("Patra", "0", "0", "0", "200"),
                new DashboaedModelData("Khandvi", "0", "0", "0", "240"),
                new DashboaedModelData("Khamni", "0", "0", "0", "240"),
                new DashboaedModelData("Fafda", "0", "0", "0", "360"),
                new DashboaedModelData("Jalebi", "0", "0", "0", "300"),
                new DashboaedModelData("Samosa", "0", "0", "0", "15"),
                new DashboaedModelData("P.samosa", "0", "0", "0", "240")};


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

                   Button btn_close = dialog.findViewById(R.id.btn_close_popup);


                   item_name_list = new ArrayList<>();
                   item_weight_list = new ArrayList<>();
                   item_price_list = new ArrayList<>();

                   //--------------------------------------------------------------------------------
                   TableLayout ll = (TableLayout) dialog.findViewById(R.id.table_receipt);
                    TextView tv_item_name, tv_item_weight, tv_item_price ;

                   //--------------------------------------------------------------------------------
                   for(int i = 0; i < myListData.length; i++){

                       if((int) Double.parseDouble(myListData[i].getAmount()) > 0){

                           String item_name = myListData[i].getItem_name();
                           String item_weight = myListData[i].getWeight();
                           String item_price = myListData[i].getPrice();

                           item_name_list.add(item_name);
                           item_weight_list.add(item_weight);
                           item_price_list.add(item_price);
                       }

                   }


                   for (int j = 0; j < item_name_list.size(); j++){
                       TableRow row= new TableRow(getContext());

                       TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                       row.setLayoutParams(lp);
                       tv_item_name = new TextView(getContext());
                       tv_item_name.setText(""+item_name_list.get(j)+" ---- "+item_weight_list.get(j)+" ----  "+item_price_list.get(j));
                       row.addView(tv_item_name);
                       ll.addView(row,j);
                   }


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
