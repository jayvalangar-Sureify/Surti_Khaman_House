package com.surti.khaman.house.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter < DashboardRecyclerViewAdapter.ViewHolder > {

    boolean is_Et_Weight_Focus, is_Et_Price_Focus;
     ArrayList<DashboaedModelData> dashboaedModelDataArrayList;

    // RecyclerView recyclerView;
    public DashboardRecyclerViewAdapter(ArrayList<DashboaedModelData> dashboaedModelDataArrayList) {
        this.dashboaedModelDataArrayList = dashboaedModelDataArrayList;
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
        int position_latest = holder.getAbsoluteAdapterPosition();
        final DashboaedModelData MyData = dashboaedModelDataArrayList.get(position_latest);

        String getItemName = "", getWeight = "", getPrice = "", getAmount = "", getFixedPrice = "", getFixedWeight = "";;
        getItemName = dashboaedModelDataArrayList.get(position_latest).getItem_name();
        getWeight = dashboaedModelDataArrayList.get(position_latest).getWeight();
        getPrice = dashboaedModelDataArrayList.get(position_latest).getPrice();
        getAmount = dashboaedModelDataArrayList.get(position_latest).getAmount();
        getFixedPrice = dashboaedModelDataArrayList.get(position_latest).getFixedPrice();
        getFixedWeight = dashboaedModelDataArrayList.get(position_latest).getFixedWeight();

        holder.tv_item_name.setText(getItemName);
        holder.et_weight.setText(getWeight);
        holder.et_price.setText(getPrice);

        if(getAmount != "") {
            holder.tv_amount.setText(getAmount);
        }else{
            holder.tv_amount.setText("");
        }


        // Find Price
        //------------------------------------------------------------------------------------------
        String fp_string = getFixedPrice;
        String fw_string = getFixedWeight;
        holder.et_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(is_Et_Weight_Focus && i2 == 0) {
                    holder.et_price.setText("");
                    holder.tv_amount.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable input) {

                if(is_Et_Weight_Focus) {
                    String dw_string = input.toString();
                    if(!dw_string.isEmpty() && dw_string != "0"){
                        dashboaedModelDataArrayList.get(position_latest).setDynamicWeight(input.toString());
                    }{

                    }

                    Long fp_long = null, dw_long = null, fw_long = null;

                    if(!fp_string.isEmpty()){
                        fp_long = Long.parseLong(fp_string);
                    }

                    if(!dw_string.isEmpty()){
                        dw_long = Long.parseLong(dw_string);;
                    }

                    if(!fw_string.isEmpty()){
                        fw_long = Long.parseLong(fw_string);
                    }


                    Long find_dp_long = findDynamicPrice(fp_long, dw_long, fw_long);
                    String find_dp_string = "";
                    if(find_dp_long != null && find_dp_long != 0l) {
                        find_dp_string = Long.toString(find_dp_long);
                        holder.tv_amount.setText(find_dp_string);
                        holder.et_price.setText(find_dp_string);
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice(find_dp_string);
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount(find_dp_string);
                    }else{
                        holder.et_price.setText("");
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice("");
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount("");
                    }




                    if(input.toString().isEmpty()){
                        dashboaedModelDataArrayList.get(position_latest).setDynamicWeight("");
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice("");
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount("");
                    }


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
            public void afterTextChanged(Editable input) {

                if(is_Et_Price_Focus) {
                    String dp_string = input.toString();
                    if(!dp_string.isEmpty() && dp_string != "0"){
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice(input.toString());
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount(input.toString());
                        holder.tv_amount.setText(dp_string);
                    }else{
                        holder.tv_amount.setText("");
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice("");
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount("");
                    }

                    Long fp_long = null, dp_long = null, fw_long = null;

                    if(!fp_string.isEmpty()){
                        fp_long = Long.parseLong(fp_string);
                    }

                    if(!dp_string.isEmpty()){
                        dp_long = Long.parseLong(dp_string);;
                    }

                    if(!fw_string.isEmpty()){
                        fw_long = Long.parseLong(fw_string);
                    }



                    Long find_dw_long = findDynamicWeight(fw_long, fp_long, dp_long);
                    if(find_dw_long != null) {
                        String find_dw_string = Long.toString(find_dw_long);
                        holder.et_weight.setText(find_dw_string);
                        dashboaedModelDataArrayList.get(position_latest).setDynamicWeight(find_dw_string);
                    }else{
                        holder.et_weight.setText("");
                    }



                    if(input.toString().isEmpty()){
                        dashboaedModelDataArrayList.get(position_latest).setDynamicWeight("");
                        dashboaedModelDataArrayList.get(position_latest).setDynamicPrice("");
                        dashboaedModelDataArrayList.get(position_latest).setCalculatedAmount("");
                    }

                }



            }
        });
        //------------------------------------------------------------------------------------------


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "click on item: " + MyData.getItem_name(), Toast.LENGTH_LONG).show();
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
        return dashboaedModelDataArrayList.size();
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

    public Long findDynamicWeight(Long fw, Long fp, Long dp){
        if((fp != null) && (dp != null) && (fw != null)) {
            Long dynamic_weight;

            dynamic_weight = (fw * dp) / fp;

            return dynamic_weight;
        }else {
            return null;
        }
    }

    public Long findDynamicPrice(Long fp, Long dw, Long fw){
        if((fp != null) && (dw != null) && (fw != null)) {
            Long dynamic_price;

            dynamic_price = (fp * dw) / fw;

            return dynamic_price;
        }else {
            return null;
        }
    }

}