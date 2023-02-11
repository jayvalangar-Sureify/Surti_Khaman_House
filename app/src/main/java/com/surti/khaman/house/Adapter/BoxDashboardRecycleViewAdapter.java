package com.surti.khaman.house.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;

public class BoxDashboardRecycleViewAdapter extends RecyclerView.Adapter < BoxDashboardRecycleViewAdapter.ViewHolder > {


    Long result_total_only_weight = 0L;
    String calculation_history_weight = "";
    String calculation_history_price = "";
    Long result_total_only_price = 0L;

    ArrayList<DashboaedModelData> dashboaedModelDataArrayList;

    Context context;

    // RecyclerView recyclerView;
    public BoxDashboardRecycleViewAdapter(ArrayList<DashboaedModelData> dashboaedModelDataArrayList, Context context) {
        this.dashboaedModelDataArrayList = dashboaedModelDataArrayList;
        this.context = context;
    }
    @Override
    public BoxDashboardRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_box_dashboard, parent, false);
        BoxDashboardRecycleViewAdapter.ViewHolder viewHolder = new BoxDashboardRecycleViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(BoxDashboardRecycleViewAdapter.ViewHolder holder, int position) {



        holder.tv_item_name.setText(dashboaedModelDataArrayList.get(position).getItem_name());


//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
        holder.btn_show_popup_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                        //==================================================================================
                        dialog.setContentView(R.layout.show_weight_popup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                EditText et_history_calculation, et_final_result_weight;
                TextView tv_show_item_name;
                tv_show_item_name = (TextView) dialog.findViewById(R.id.tv_show_item_name);
                et_history_calculation = (EditText) dialog.findViewById(R.id.et_history_calculation);
                et_final_result_weight = (EditText) dialog.findViewById(R.id.et_final_result_weight);


                tv_show_item_name.setText(""+dashboaedModelDataArrayList.get(position).getItem_name());

                result_total_only_weight = 0L;
                calculation_history_weight = "";
                //======================================================================================
                 Button btn_50, btn_100, btn_150,
                        btn_200, btn_250, btn_300,
                        btn_350, btn_400, btn_450,
                        btn_500, btn_550, btn_600,
                        btn_650, btn_700, btn_750,
                        btn_800, btn_850, btn_900,
                        btn_950, btn_1000, btn_clear,
                         btn_done_selection_weight;

                btn_done_selection_weight = (Button) dialog.findViewById(R.id.btn_done_selection_weight);
                btn_50 = (Button) dialog.findViewById(R.id.btn_50);
                btn_100 = (Button) dialog.findViewById(R.id.btn_100);
                btn_150 = (Button) dialog.findViewById(R.id.btn_150);
                btn_200 = (Button) dialog.findViewById(R.id.btn_200);
                btn_250 = (Button) dialog.findViewById(R.id.btn_250);
                btn_300 = (Button) dialog.findViewById(R.id.btn_300);
                btn_350 = (Button) dialog.findViewById(R.id.btn_350);
                btn_400 = (Button) dialog.findViewById(R.id.btn_400);
                btn_450 = (Button) dialog.findViewById(R.id.btn_450);
                btn_500 = (Button) dialog.findViewById(R.id.btn_500);
                btn_550 = (Button) dialog.findViewById(R.id.btn_550);
                btn_600 = (Button) dialog.findViewById(R.id.btn_600);
                btn_650 = (Button) dialog.findViewById(R.id.btn_650);
                btn_700 = (Button) dialog.findViewById(R.id.btn_700);
                btn_750 = (Button) dialog.findViewById(R.id.btn_750);
                btn_800 = (Button) dialog.findViewById(R.id.btn_800);
                btn_850 = (Button) dialog.findViewById(R.id.btn_850);
                btn_900 = (Button) dialog.findViewById(R.id.btn_900);
                btn_950 = (Button) dialog.findViewById(R.id.btn_950);
                btn_1000 = (Button) dialog.findViewById(R.id.btn_1000);
                btn_clear = (Button) dialog.findViewById(R.id.btn_clear);

                //======================================================================================

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_final_result_weight.setText("= 0");
                        et_history_calculation.setText("");
                        calculation_history_weight = "";
                        result_total_only_weight = 0L;
                    }
                });

                btn_done_selection_weight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        holder.et_final_result_weight.setText(""+result_total_only_weight);
                    }
                });

                //----------------------------------------------------------------------------------
                btn_50.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_50, et_history_calculation, et_final_result_weight);
                    }
                });

                btn_100.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_100, et_history_calculation, et_final_result_weight);
                    }
                });

                btn_150.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_150, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_200.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_200, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_250.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_250, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_300.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_300, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_350.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_350, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_400.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_400, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_450.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_450, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_500.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_500, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_550.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_550, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_600.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_600, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_650.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_650, et_history_calculation, et_final_result_weight);
                    }
                });


                btn_700.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_700, et_history_calculation, et_final_result_weight);
                    }
                });

                btn_750.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_750, et_history_calculation, et_final_result_weight);
                    }
                });
                btn_800.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_800, et_history_calculation, et_final_result_weight);
                    }
                });
                btn_850.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_850, et_history_calculation, et_final_result_weight);
                    }
                });
                btn_900.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_900, et_history_calculation, et_final_result_weight);
                    }
                });
                btn_950.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_950, et_history_calculation, et_final_result_weight);
                    }
                });
                btn_1000.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_weight(btn_1000, et_history_calculation, et_final_result_weight);
                    }
                });


                //----------------------------------------------------------------------------------


                        dialog.show();
                    }
        });
//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#


//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
        holder.btn_show_popup_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                //==================================================================================
                dialog.setContentView(R.layout.show_price_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                EditText et_history_calculation, et_final_result_price;
                TextView tv_show_item_name;
                tv_show_item_name = (TextView) dialog.findViewById(R.id.tv_show_item_name);
                et_history_calculation = (EditText) dialog.findViewById(R.id.et_history_calculation);
                et_final_result_price = (EditText) dialog.findViewById(R.id.et_final_result_price);


                tv_show_item_name.setText(""+dashboaedModelDataArrayList.get(position).getItem_name());

                result_total_only_price = 0L;
                calculation_history_price = "";
                //======================================================================================
                Button btn_5, btn_10, btn_15,
                        btn_20, btn_25, btn_30,
                        btn_35, btn_40, btn_45,
                        btn_50, btn_55, btn_60,
                        btn_65, btn_70, btn_75,
                        btn_100, btn_200, btn_300,
                        btn_500, btn_1000, btn_clear,
                        btn_done_selection_price;

                btn_done_selection_price = (Button) dialog.findViewById(R.id.btn_done_selection_price);
                btn_5 = (Button) dialog.findViewById(R.id.btn_5);
                btn_10 = (Button) dialog.findViewById(R.id.btn_10);
                btn_15 = (Button) dialog.findViewById(R.id.btn_15);
                btn_20 = (Button) dialog.findViewById(R.id.btn_20);
                btn_25 = (Button) dialog.findViewById(R.id.btn_25);
                btn_30 = (Button) dialog.findViewById(R.id.btn_30);
                btn_35 = (Button) dialog.findViewById(R.id.btn_35);
                btn_40 = (Button) dialog.findViewById(R.id.btn_40);
                btn_45 = (Button) dialog.findViewById(R.id.btn_45);
                btn_50 = (Button) dialog.findViewById(R.id.btn_50);
                btn_55 = (Button) dialog.findViewById(R.id.btn_55);
                btn_60 = (Button) dialog.findViewById(R.id.btn_60);
                btn_65 = (Button) dialog.findViewById(R.id.btn_65);
                btn_70 = (Button) dialog.findViewById(R.id.btn_70);
                btn_75 = (Button) dialog.findViewById(R.id.btn_75);
                btn_100 = (Button) dialog.findViewById(R.id.btn_100);
                btn_200 = (Button) dialog.findViewById(R.id.btn_200);
                btn_300 = (Button) dialog.findViewById(R.id.btn_300);
                btn_500 = (Button) dialog.findViewById(R.id.btn_500);
                btn_1000 = (Button) dialog.findViewById(R.id.btn_1000);
                btn_clear = (Button) dialog.findViewById(R.id.btn_clear);

                //======================================================================================

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_final_result_price.setText("= 0");
                        et_history_calculation.setText("");
                        calculation_history_price = "";
                        result_total_only_price = 0L;
                    }
                });

                btn_done_selection_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        holder.et_final_result_price.setText(""+result_total_only_price);
                    }
                });

                //----------------------------------------------------------------------------------
                btn_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_5, et_history_calculation, et_final_result_price);
                    }
                });

                btn_10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_10, et_history_calculation, et_final_result_price);
                    }
                });

                btn_15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_15, et_history_calculation, et_final_result_price);
                    }
                });


                btn_20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_20, et_history_calculation, et_final_result_price);
                    }
                });


                btn_25.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_25, et_history_calculation, et_final_result_price);
                    }
                });


                btn_30.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_30, et_history_calculation, et_final_result_price);
                    }
                });


                btn_35.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_35, et_history_calculation, et_final_result_price);
                    }
                });


                btn_40.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_40, et_history_calculation, et_final_result_price);
                    }
                });


                btn_45.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_45, et_history_calculation, et_final_result_price);
                    }
                });


                btn_50.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_50, et_history_calculation, et_final_result_price);
                    }
                });


                btn_55.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_55, et_history_calculation, et_final_result_price);
                    }
                });


                btn_60.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_60, et_history_calculation, et_final_result_price);
                    }
                });


                btn_65.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_65, et_history_calculation, et_final_result_price);
                    }
                });


                btn_70.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_70, et_history_calculation, et_final_result_price);
                    }
                });

                btn_75.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_75, et_history_calculation, et_final_result_price);
                    }
                });
                btn_100.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_100, et_history_calculation, et_final_result_price);
                    }
                });
                btn_200.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_200, et_history_calculation, et_final_result_price);
                    }
                });
                btn_300.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_300, et_history_calculation, et_final_result_price);
                    }
                });
                btn_500.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_500, et_history_calculation, et_final_result_price);
                    }
                });
                btn_1000.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_click_add_price(btn_1000, et_history_calculation, et_final_result_price);
                    }
                });


                //----------------------------------------------------------------------------------


                dialog.show();
            }
        });
//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#



        holder.box_card_view_main_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: "+position, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void button_click_add_weight(Button btn_weight, EditText et_history_calculation, EditText et_final_result_weight) {
        String input_button = btn_weight.getText().toString();

        if(!calculation_history_weight.isEmpty()) {
            calculation_history_weight = calculation_history_weight + "+" + input_button;
        }else{
            calculation_history_weight = calculation_history_weight + input_button;
        }

        result_total_only_weight = result_total_only_weight + Integer.parseInt(input_button);

        et_history_calculation.setText(calculation_history_weight);
        et_final_result_weight.setText("= "+result_total_only_weight);

    }

    private void button_click_add_price(Button btn_weight, EditText et_history_calculation, EditText et_final_result_price) {
        String input_button = btn_weight.getText().toString();

        if(!calculation_history_price.isEmpty()) {
            calculation_history_price = calculation_history_price + "+" + input_button;
        }else{
            calculation_history_price = calculation_history_price + input_button;
        }

        result_total_only_price = result_total_only_price + Integer.parseInt(input_button);

        et_history_calculation.setText(calculation_history_price);
        et_final_result_price.setText("= "+result_total_only_price);

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
        public TextView tv_item_name, tv_increment_btn, tv_decrement_btn;
        public EditText et_final_result_weight, et_final_result_price;
        public CardView box_card_view_main_row;
        public Button btn_show_popup_weight, btn_show_popup_price;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            this.et_final_result_weight = (EditText) itemView.findViewById(R.id.et_final_result_weight);
            this.et_final_result_price = (EditText) itemView.findViewById(R.id.et_final_result_price);
            this.box_card_view_main_row = (CardView) itemView.findViewById(R.id.box_card_view_main_row);
            this.btn_show_popup_weight = (Button) itemView.findViewById(R.id.btn_show_popup_weight);
            this.btn_show_popup_price = (Button) itemView.findViewById(R.id.btn_show_popup_price);


        }
    }


}
