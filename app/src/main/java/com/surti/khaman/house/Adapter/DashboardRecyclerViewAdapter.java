package com.surti.khaman.house.Adapter;

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

        String getItemName = "", getWeight = "0", getPrice = "0", getAmount = "0", getFixedPrice = "0";
        getItemName = mydata[position].getItem_name();
        getWeight = mydata[position].getWeight();
        getPrice = mydata[position].getPrice();
        getAmount = mydata[position].getAmount();
        getFixedPrice = mydata[position].getFixed_price();

        holder.tv_item_name.setText(getItemName);
        holder.et_weight.setText(getWeight);
        holder.et_price.setText(getPrice);
        holder.tv_amount.setText(getAmount);


        // Find Price
        //------------------------------------------------------------------------------------------
        String finalGetFixedPrice = getFixedPrice;
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
                    if (temp_str.isEmpty()) {
                        temp_str = "0";
                    }

                    double i = Double.parseDouble(temp_str);

                    double find_dp = findDynamicPrice(parseStringToDouble(finalGetFixedPrice), i, 1000);
                    holder.et_price.setText(Double.toString(find_dp));
                    holder.tv_amount.setText(Double.toString(find_dp));

                    mydata[position].setPrice(Double.toString(find_dp));
                    mydata[position].setWeight(s.toString());
                    mydata[position].setAmount(Double.toString(find_dp));

                    Double grandTotal = findGrandTotal();
                    dashboardInterface.onTotalAmountChange(grandTotal.toString());
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
                    if (temp_str.isEmpty()) {
                        temp_str = "0";
                    }

                    double i = Double.parseDouble(temp_str);

                    double find_dp = findDynamicWeight(1000, parseStringToDouble(finalGetFixedPrice), i);
                    holder.et_weight.setText(Double.toString(find_dp));
                    holder.tv_amount.setText(s.toString());

                    mydata[position].setPrice(s.toString());
                    mydata[position].setWeight(Double.toString(find_dp));
                    mydata[position].setAmount(s.toString());

                    Double grandTotal = findGrandTotal();
                    dashboardInterface.onTotalAmountChange(grandTotal.toString());
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
    public long getItemId(int position) {
        return position;
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







    private static double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }

    // f.w / f.p = d.w / d.p
    // dw = (f.w * d.p / f.p )
    // dp = (f.p * d.w / f.w)

    public Double findDynamicWeight(double fw, double fp, double dp){
        double dynamic_weight = 0;

               dynamic_weight = (fw * dp) / fp;

        return dynamic_weight;
    }

    public Double findDynamicPrice(double fp, double dw, double fw){
        double dynamic_price = 0;

        dynamic_price = (fp * dw) / fw;

        return dynamic_price;
    }

    public double findGrandTotal(){
        double grandTotal = 0;

            for(int i = 0; i < mydata.length; i++){
                if(mydata[i].getAmount() != null) {
                    grandTotal = grandTotal + parseStringToDouble(mydata[i].getAmount());
                }
            }
        Arrays.asList(mydata).stream().collect(myDataa -> mydataa.get)
        return grandTotal;
    }
}