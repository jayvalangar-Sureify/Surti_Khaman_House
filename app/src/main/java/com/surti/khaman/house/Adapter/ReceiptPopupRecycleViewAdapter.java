package com.surti.khaman.house.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.R;

import java.util.ArrayList;

public class ReceiptPopupRecycleViewAdapter extends RecyclerView.Adapter < com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter.ViewHolder > {


    ArrayList<String> item_name_list = new ArrayList<>();
    ArrayList<String> item_weight_list = new ArrayList<>();
    ArrayList<String> item_price_list = new ArrayList<>();

        // RecyclerView recyclerView;
        public ReceiptPopupRecycleViewAdapter(ArrayList<String> item_name_list, ArrayList<String> item_weight_list, ArrayList<String> item_price_list) {
            this.item_name_list = item_name_list;
            this.item_weight_list = item_weight_list;
            this.item_price_list = item_price_list;
        }

        @Override
        public com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.row_receipt_recycle_view, parent, false);
            com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter.ViewHolder viewHolder = new com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter.ViewHolder(listItem);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter.ViewHolder holder, int position) {


            String getFinalItemName = "", getFinalWeight = "0", getFinalAmount = "0", getFinalTotal = "0";

            getFinalItemName = item_name_list.get(position);
            getFinalWeight = item_weight_list.get(position);
            getFinalAmount = item_price_list.get(position);

            holder.tv_item_name.setText(getFinalItemName);
            holder.tv_item_weight.setText(getFinalWeight);
            holder.tv_item_price.setText(getFinalAmount);

        }
        @Override
        public int getItemCount() {
            return item_name_list.size();
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_item_name, tv_item_weight, tv_item_price;

            public ViewHolder(View itemView) {
                super(itemView);
                this.tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
                this.tv_item_weight = (TextView) itemView.findViewById(R.id.tv_item_weight);
                this.tv_item_price = (TextView) itemView.findViewById(R.id.tv_item_price);
            }
        }


    }