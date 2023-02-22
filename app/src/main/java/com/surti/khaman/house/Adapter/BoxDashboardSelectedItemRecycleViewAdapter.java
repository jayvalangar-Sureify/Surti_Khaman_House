package com.surti.khaman.house.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Model.BoxDashboardSelectedItemModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;

public class BoxDashboardSelectedItemRecycleViewAdapter extends RecyclerView.Adapter < com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter.ViewHolder > {

        public static ArrayList<BoxDashboardSelectedItemModelData> boxDashboardSelectedItemModelData;
        Context context;

        // RecyclerView recyclerView;
        public BoxDashboardSelectedItemRecycleViewAdapter(ArrayList<BoxDashboardSelectedItemModelData> boxDashboardSelectedItemModelData ,Context context) {
            this.boxDashboardSelectedItemModelData = boxDashboardSelectedItemModelData;
            this.context = context;
        }

        @Override
        public com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.row_box_dashboard_selected_item, parent, false);
            com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter.ViewHolder viewHolder = new com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter.ViewHolder holder, int position) {

            int position_latest = holder.getAbsoluteAdapterPosition();

            holder.tv_item_selected_name.setText(boxDashboardSelectedItemModelData.get(position_latest).get_selected_item_name());
            holder.tv_item_selected_weight.setText(boxDashboardSelectedItemModelData.get(position_latest).get_selected_weight());
            holder.tv_item_selected_price.setText(boxDashboardSelectedItemModelData.get(position_latest).get_selected_price());

        }

        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_item_selected_name, tv_item_selected_weight, tv_item_selected_price;
            public CardView cardview_boxdashboard_selected;


            public ViewHolder(View itemView) {
                super(itemView);
                this.tv_item_selected_name = (TextView) itemView.findViewById(R.id.tv_item_selected_name);
                this.tv_item_selected_weight = (TextView) itemView.findViewById(R.id.tv_item_selected_weight);
                this.tv_item_selected_price = (TextView) itemView.findViewById(R.id.tv_item_selected_price);
                this.cardview_boxdashboard_selected = (CardView) itemView.findViewById(R.id.cardview_boxdashboard_selected);

            }

        }
//-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

        @Override
        public int getItemCount() {
            return boxDashboardSelectedItemModelData.size();
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }

