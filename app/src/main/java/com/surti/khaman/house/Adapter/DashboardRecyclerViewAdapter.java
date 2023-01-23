package com.surti.khaman.house.Adapter;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter < DashboardRecyclerViewAdapter.ViewHolder > {

    boolean is_Et_Weight_Focus, is_Et_Price_Focus;
     DashboaedModelData[] mydata;
    DashboardInterface dashboardInterface;

    // RecyclerView recyclerView;
    public DashboardRecyclerViewAdapter(DashboaedModelData[] mydata, DashboardInterface dashboardInterface) {
        this.mydata = mydata;
        this.dashboardInterface = dashboardInterface;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_dashboard, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DashboaedModelData MyData = mydata[position];

        String getItemName = "", getWeight = "", getPrice = "", getAmount = "", getFixedPrice = "", getFixedWeight = "";;
        getItemName = mydata[position].getItem_name();
        getWeight = mydata[position].getWeight();
        getPrice = mydata[position].getPrice();
        getAmount = mydata[position].getAmount();
        getFixedPrice = mydata[position].getFixedPrice();
        getFixedWeight = mydata[position].getFixedWeight();

        holder.tv_item_name.setText(getItemName);
        holder.et_weight.setText(getWeight);
        holder.et_price.setText(getPrice);

        if(getAmount != "") {
            holder.tv_amount.setText(getAmount);
        }


        // Find Price
        //------------------------------------------------------------------------------------------
        String finalGetFixedPrice = getFixedPrice;
        String finalGetFixedWeight = getFixedWeight;
        holder.et_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(is_Et_Weight_Focus) {
                    String temp_str = s.toString();
                    if (temp_str.isEmpty() || temp_str == "0") {
                        temp_str = "0";
                    }

                    Long i = Long.parseLong(temp_str);

                    Long find_dp = findDynamicPrice(Long.parseLong(finalGetFixedPrice), i, Long.parseLong(finalGetFixedWeight));
                    holder.et_price.setText(Long.toString(find_dp));

                    if(Long.toString(find_dp) != "") {
                        holder.tv_amount.setText(Long.toString(find_dp));
                    }

                    mydata[position].setDynamicPrice(Long.toString(find_dp));
                    mydata[position].setDynamicWeight(s.toString());
                    mydata[position].setCalculatedAmount(Long.toString(find_dp));

//                    Long grandTotal = findGrandTotal();
//                    dashboardInterface.onTotalAmountChange(grandTotal.toString());
                }

            }
        });
        //------------------------------------------------------------------------------------------



        // Find Weight
        //------------------------------------------------------------------------------------------
        holder.et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(is_Et_Price_Focus) {
                    String temp_str = s.toString();
                    if (temp_str.isEmpty() || temp_str == "0") {
                        temp_str = "0";
                    }

                    Long i = Long.parseLong(temp_str);

                    Long find_dp = findDynamicWeight(Long.parseLong(finalGetFixedWeight), Long.parseLong(finalGetFixedPrice), i);
                    holder.et_weight.setText(Long.toString(find_dp));

                    if(s.toString() != "") {
                        holder.tv_amount.setText(s.toString());
                    }

                    mydata[position].setDynamicPrice(s.toString());
                    mydata[position].setDynamicWeight(Long.toString(find_dp));
                    mydata[position].setCalculatedAmount(s.toString());

//                    Long grandTotal = findGrandTotal();
//                    dashboardInterface.onTotalAmountChange(grandTotal.toString());
                }



            }
        });
        //------------------------------------------------------------------------------------------


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + MyData.getItem_name(), Toast.LENGTH_LONG).show();
            }
        });


        holder.et_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                is_Et_Weight_Focus = isFocus;
            }
        });

        holder.et_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                is_Et_Price_Focus = isFocus;
            }
        });
    }
    @Override
    public int getItemCount() {
        return mydata.length;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item_name, tv_amount;
        public EditText et_weight, et_price;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            this.et_weight = (EditText) itemView.findViewById(R.id.et_weight);
            this.et_price = (EditText) itemView.findViewById(R.id.et_price);
            this.tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }



    // f.w / f.p = d.w / d.p
    // dw = (f.w * d.p / f.p )
    // dp = (f.p * d.w / f.w)

    public long findDynamicWeight(long fw, long fp, long dp){
        long dynamic_weight = 0;

               dynamic_weight = (fw * dp) / fp;

        return dynamic_weight;
    }

    public Long findDynamicPrice(Long fp, Long dw, Long fw){
        Long dynamic_price ;

        dynamic_price = (fp * dw) / fw;

        return dynamic_price;
    }

    public Long findGrandTotal(){
        Long grandTotal =0l;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                List<DashboaedModelData> myDataList = Arrays.asList(mydata);
              //  List<String> aa = myDataList.stream().map(o -> o.getAmount()).collect(Collectors.toList());
               // List<Long> r1 = aa.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
                grandTotal = myDataList.stream().map(s -> Long.parseLong(s.getAmount())).collect(Collectors.toList()).stream().reduce(0l, Long::sum);

            }catch (Exception e){
                e.getMessage();
            }
        }
        return grandTotal;
    }
}