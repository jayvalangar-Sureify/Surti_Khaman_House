package com.surti.khaman.house.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;

import java.util.ArrayList;

public class BoxDashboardRecycleViewAdapter extends RecyclerView.Adapter < BoxDashboardRecycleViewAdapter.ViewHolder > {

    boolean is_Et_Weight_Focus, is_Et_Price_Focus;
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

        int position_latest = holder.getAbsoluteAdapterPosition();

        holder.tv_item_name.setText(dashboaedModelDataArrayList.get(position_latest).getItem_name());

        String getItemName = "", getWeight = "", getPrice = "", getAmount = "", getFixedPrice = "", getFixedWeight = "";;
        getItemName = dashboaedModelDataArrayList.get(position_latest).getItem_name();
        getWeight = dashboaedModelDataArrayList.get(position_latest).getWeight();
        getPrice = dashboaedModelDataArrayList.get(position_latest).getPrice();
        getAmount = dashboaedModelDataArrayList.get(position_latest).getAmount();
        getFixedPrice = dashboaedModelDataArrayList.get(position_latest).getFixedPrice();
        getFixedWeight = dashboaedModelDataArrayList.get(position_latest).getFixedWeight();


        String fp_string = getFixedPrice;
        String fw_string = getFixedWeight;

        holder.tv_item_name.setText(getItemName);
        holder.et_final_result_weight.setText(getWeight);
        holder.et_final_result_price.setText(getPrice);
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
                Button btn_close_popup;

                btn_close_popup = (Button) dialog.findViewById(R.id.btn_close_popup);
                tv_show_item_name = (TextView) dialog.findViewById(R.id.tv_show_item_name);
                et_history_calculation = (EditText) dialog.findViewById(R.id.et_history_calculation);
                et_final_result_weight = (EditText) dialog.findViewById(R.id.et_final_result_weight);



                btn_close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                tv_show_item_name.setText(""+dashboaedModelDataArrayList.get(position_latest).getItem_name());

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


                // per plate
                boolean isFixedWeightPP = false;
                if(fw_string != null || fw_string.isEmpty()){
                    isFixedWeightPP = fw_string.equals("1");
                }

                if(isFixedWeightPP){
                    btn_50.setText(context.getResources().getString(R.string._1));
                    btn_100.setText(context.getString(R.string._2));
                    btn_150.setText(context.getString(R.string._3));
                    btn_200.setText(context.getString(R.string._4));
                    btn_250.setText(context.getString(R.string._5));
                    btn_300.setText(context.getString(R.string._6));
                    btn_350.setText(context.getString(R.string._7));
                    btn_400.setText(context.getString(R.string._8));
                    btn_450.setText(context.getString(R.string._9));
                    btn_500.setText(context.getString(R.string._10));
                    btn_550.setText(context.getString(R.string._20));
                    btn_600.setText(context.getString(R.string._25));
                    btn_650.setText(context.getString(R.string._30));
                    btn_700.setText(context.getString(R.string._35));
                    btn_750.setText(context.getString(R.string._40));
                    btn_800.setText(context.getString(R.string._45));
                    btn_850.setText(context.getString(R.string._50));
                    btn_900.setText(context.getString(R.string._100));
                    btn_950.setText(context.getString(R.string._500));
                    btn_1000.setText(context.getString(R.string._1000));

                }

                //======================================================================================

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_final_result_weight.setText("= 0 gm");
                        et_history_calculation.setText("");
                        calculation_history_weight = "";
                        result_total_only_weight = 0L;
                    }
                });

                btn_done_selection_weight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        is_Et_Weight_Focus = true;
                        is_Et_Price_Focus = false;
                        holder.et_final_result_weight.setText(""+result_total_only_weight);
                        et_weight_and_find_price_logic(""+result_total_only_weight, fp_string, fw_string, holder, position_latest);
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
                Button btn_close_popup;

                btn_close_popup = (Button) dialog.findViewById(R.id.btn_close_popup);
                tv_show_item_name = (TextView) dialog.findViewById(R.id.tv_show_item_name);
                et_history_calculation = (EditText) dialog.findViewById(R.id.et_history_calculation);
                et_final_result_price = (EditText) dialog.findViewById(R.id.et_final_result_price);


                btn_close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                tv_show_item_name.setText(""+dashboaedModelDataArrayList.get(position_latest).getItem_name());

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
                        et_final_result_price.setText("= 0 Rs");
                        et_history_calculation.setText("");
                        calculation_history_price = "";
                        result_total_only_price = 0L;
                    }
                });

                btn_done_selection_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        is_Et_Weight_Focus = false;
                        is_Et_Price_Focus = true;
                        holder.et_final_result_price.setText(""+result_total_only_price);
                        et_price_and_find_weight_logic(""+result_total_only_price, fp_string, fw_string, holder, position_latest);
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
//                Toast.makeText(view.getContext(), "click on item: "+position_latest, Toast.LENGTH_LONG).show();
            }
        });


        // Find Price
        //------------------------------------------------------------------------------------------
        holder.et_final_result_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(is_Et_Weight_Focus && i2 == 0) {
                    holder.et_final_result_price.setText("");
//                    holder.tv_amount.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable input) {
                et_weight_and_find_price_logic(input.toString(), fp_string, fw_string, holder, position_latest);
            }
        });
        //------------------------------------------------------------------------------------------



        // Find Weight
        //------------------------------------------------------------------------------------------
        holder.et_final_result_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable input) {
                et_price_and_find_weight_logic(input.toString(), fp_string, fw_string, holder, position_latest);
            }
        });
        //------------------------------------------------------------------------------------------


        holder.et_final_result_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                is_Et_Weight_Focus = isFocus;
            }
        });

        holder.et_final_result_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                is_Et_Price_Focus = isFocus;
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
        et_final_result_weight.setText("= "+result_total_only_weight+ " gm");

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
        et_final_result_price.setText("= "+result_total_only_price + " Rs");

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


 // Enter dynamic price and finding weight
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-
    public void et_price_and_find_weight_logic(String input_dp_string, String fp_string, String fw_string, ViewHolder holder, int position){

        if(is_Et_Price_Focus) {
            String dp_string = input_dp_string;
            if(!dp_string.isEmpty() && dp_string != "0"){
                dashboaedModelDataArrayList.get(position).setDynamicPrice(dp_string);
                dashboaedModelDataArrayList.get(position).setCalculatedAmount(dp_string);
//                        holder.tv_amount.setText(dp_string);
            }else{
//                        holder.tv_amount.setText("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
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
                holder.et_final_result_weight.setText(find_dw_string);
                dashboaedModelDataArrayList.get(position).setDynamicWeight(find_dw_string);
            }else{
                holder.et_final_result_weight.setText("");
            }

            if(input_dp_string.isEmpty()){
                dashboaedModelDataArrayList.get(position).setDynamicWeight("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }

        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-


    // Enter dynamic price and finding weight
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-
    public void et_weight_and_find_price_logic(String input_dw_string, String fp_string, String fw_string, ViewHolder holder, int position) {
        if(is_Et_Weight_Focus) {
            String dw_string = input_dw_string;
            if(!dw_string.isEmpty() && dw_string != "0"){
                dashboaedModelDataArrayList.get(position).setDynamicWeight(input_dw_string);
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
//                        holder.tv_amount.setText(find_dp_string);
                holder.et_final_result_price.setText(find_dp_string);
                dashboaedModelDataArrayList.get(position).setDynamicPrice(find_dp_string);
                dashboaedModelDataArrayList.get(position).setCalculatedAmount(find_dp_string);
            }else{
                holder.et_final_result_price.setText("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }




            if(input_dw_string.isEmpty()){
                dashboaedModelDataArrayList.get(position).setDynamicWeight("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }


        }

    }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-



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
