package com.surti.khaman.house.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.FragmentCommunication;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;

public class BoxDashboardRecycleViewAdapter extends RecyclerView.Adapter < BoxDashboardRecycleViewAdapter.ViewHolder > {

    // Select item color change, pass selected item position to fragment
    public static int row_index = -1;
    private FragmentCommunication mCommunicator;

    public static ArrayList<DashboaedModelData> dashboaedModelDataArrayList;
    Context context;

    // RecyclerView recyclerView;
    public BoxDashboardRecycleViewAdapter(ArrayList<DashboaedModelData> dashboaedModelDataArrayList ,Context context, FragmentCommunication communication) {
        this.dashboaedModelDataArrayList = dashboaedModelDataArrayList;
        this.context = context;
        mCommunicator=communication;
    }

    @Override
    public BoxDashboardRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_box_dashboard, parent, false);
        BoxDashboardRecycleViewAdapter.ViewHolder viewHolder = new BoxDashboardRecycleViewAdapter.ViewHolder(listItem, mCommunicator);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BoxDashboardRecycleViewAdapter.ViewHolder holder, int position) {

        int position_latest = holder.getAbsoluteAdapterPosition();

        holder.tv_item_name.setText(dashboaedModelDataArrayList.get(position_latest).getItem_name());

        String getItemName = "";
        getItemName = dashboaedModelDataArrayList.get(position_latest).getItem_name();
        holder.tv_item_name.setText(getItemName);


        // CardView or Textview Selected
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        holder.tv_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_selected_layout_change(holder, position_latest, holder.box_card_view_main_row, holder.tv_item_name);
            }
        });
        holder.box_card_view_main_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_selected_layout_change(holder, position_latest, holder.box_card_view_main_row, holder.tv_item_name);
            }
        });
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


        // Selected Item color change and Pass position value to fragment
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        if(row_index == position){
            mCommunicator.respond(position);
            holder.box_card_view_main_row.setCardBackgroundColor(context.getResources().getColor(R.color.green_500));
            holder.tv_item_name.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.box_card_view_main_row.setCardBackgroundColor(context.getResources().getColor(R.color.yellow_100));
            holder.tv_item_name.setTextColor(context.getResources().getColor(R.color.red_500));
        }
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

    }


    // Item Selected
    //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void item_selected_layout_change(BoxDashboardRecycleViewAdapter.ViewHolder holder, int position_latest, CardView box_card_view_main_row, TextView tv_item_name){
           row_index = position_latest;
           notifyDataSetChanged();
    }
    //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=



    //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView tv_item_name;
        public CardView box_card_view_main_row;
        FragmentCommunication mComminication;


        public ViewHolder(View itemView, FragmentCommunication Communicator) {
            super(itemView);
            this.tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            this.box_card_view_main_row = (CardView) itemView.findViewById(R.id.box_card_view_main_row);
            mComminication=Communicator;
            tv_item_name.setOnClickListener(this);

            // Initialise Temp Row Index to -1
            row_index = -1;

        }


        // Passing Value To Fragment
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
        @Override
        public void onClick(View view) {
            mComminication.respond(getAbsoluteAdapterPosition());
        }
        //-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    }
//-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

    @Override
    public int getItemCount() {
        return dashboaedModelDataArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
