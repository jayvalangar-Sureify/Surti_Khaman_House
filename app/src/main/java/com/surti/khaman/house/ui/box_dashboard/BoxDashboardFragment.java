package com.surti.khaman.house.ui.box_dashboard;

import static com.surti.khaman.house.ui.dashboard.DashboardFragment.get_SharedPreference_Billnumber;
import static com.surti.khaman.house.ui.dashboard.DashboardFragment.internal_file_data;
import static com.surti.khaman.house.ui.dashboard.DashboardFragment.set_SharedPreference_Billnumber;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.surti.khaman.house.Adapter.BoxDashboardRecycleViewAdapter;
import com.surti.khaman.house.Adapter.BoxDashboardSelectedItemRecycleViewAdapter;
import com.surti.khaman.house.Adapter.ReceiptPopupRecycleViewAdapter;
import com.surti.khaman.house.Database.DatabaseMain;
import com.surti.khaman.house.FragmentCommunication;
import com.surti.khaman.house.MainActivity;
import com.surti.khaman.house.Model.BoxDashboardSelectedItemModelData;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentBoxDashboardBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class BoxDashboardFragment extends Fragment{

    Long result_total_only_weight = 0L;
    String calculation_history_weight = "";
    String calculation_history_price = "";
    Long result_total_only_price = 0L;

    RecyclerView rv_box_dashboard, rv_box_dashboard_item_selected;
    SQLiteDatabase sqLiteDatabase;
    DatabaseMain databaseMain;
    ArrayList<DashboaedModelData> dashboaedModelDataArrayList;
    HashMap<String, Integer> boxDashboardSelectedItemModelDataHashMap;
    ArrayList<BoxDashboardSelectedItemModelData> boxDashboardSelectedItemModelDataArrayList ;

    private FragmentBoxDashboardBinding binding;

    public static String previous_file_data = "";
    public static String bill_no_and__earning_history = "";
    public static Long todays_earning_data = 0l;

    ArrayList<String> item_name_list ;
    ArrayList<String> item_weight_list ;
    ArrayList<String> item_price_list ;

    String final_bill_string = "";
    String final_file_string = "";
    String currentDateAndTime = "";
    Long grand_total = 0l;
    int position_from_rv_adapter = -1;
    String getItemName = "", getWeight = "", getPrice = "", getAmount = "", getFixedPrice = "", getFixedWeight = "";

    FragmentCommunication communication=new FragmentCommunication() {
        @Override
        public void respond(int position_latest) {
            position_from_rv_adapter = position_latest;
            getItemName = dashboaedModelDataArrayList.get(position_latest).getItem_name();
            getWeight = dashboaedModelDataArrayList.get(position_latest).getWeight();
            getPrice = dashboaedModelDataArrayList.get(position_latest).getPrice();
            getAmount = dashboaedModelDataArrayList.get(position_latest).getAmount();
            getFixedPrice = dashboaedModelDataArrayList.get(position_latest).getFixedPrice();
            getFixedWeight = dashboaedModelDataArrayList.get(position_latest).getFixedWeight();
        }

    };

//=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBoxDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseMain=new DatabaseMain(getActivity());
        rv_box_dashboard = (RecyclerView) root.findViewById(R.id.rv_box_dashboard);
        rv_box_dashboard_item_selected = (RecyclerView) root.findViewById(R.id.rv_box_dashboard_item_selected);

        boxDashboardSelectedItemModelDataHashMap = new HashMap<>();
        boxDashboardSelectedItemModelDataArrayList = new ArrayList<>();

        //--------------
        displayData();
        //--------------

        //==========================================================================================
        BoxDashboardRecycleViewAdapter adapter = new BoxDashboardRecycleViewAdapter(dashboaedModelDataArrayList, getActivity(), communication);
        rv_box_dashboard.setHasFixedSize(true);
//        rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rv_box_dashboard.setLayoutManager(gridLayoutManager);
        rv_box_dashboard.setAdapter(adapter);
        //==========================================================================================



        //==========================================================================================
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayData();
                boxDashboardSelectedItemModelDataHashMap = new HashMap<>();
                boxDashboardSelectedItemModelDataArrayList = new ArrayList<>();

                rv_box_dashboard = (RecyclerView) root.findViewById(R.id.rv_box_dashboard);
                BoxDashboardRecycleViewAdapter adapter = new BoxDashboardRecycleViewAdapter(dashboaedModelDataArrayList, getActivity(), communication);
                rv_box_dashboard.setHasFixedSize(true);
//                rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_box_dashboard.setAdapter(adapter);

                //==========================================================================================
                BoxDashboardSelectedItemRecycleViewAdapter boxDashboardSelectedItemRecycleViewAdapter = new BoxDashboardSelectedItemRecycleViewAdapter(boxDashboardSelectedItemModelDataArrayList, getActivity());
                rv_box_dashboard_item_selected = (RecyclerView) root.findViewById(R.id.rv_box_dashboard_item_selected);
                rv_box_dashboard_item_selected.setHasFixedSize(true);
//        rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                rv_box_dashboard_item_selected.setLayoutManager(gridLayoutManager);
                rv_box_dashboard_item_selected.setAdapter(boxDashboardSelectedItemRecycleViewAdapter);
                //==========================================================================================

            }
        });
        //==========================================================================================

        //$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
        binding.btnShowPopupWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If item is selected than open popup
                if(position_from_rv_adapter > -1){
                Dialog dialog = new Dialog(getContext());
                //==================================================================================
                dialog.setContentView(R.layout.show_weight_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

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

                tv_show_item_name.setText("" + dashboaedModelDataArrayList.get(position_from_rv_adapter).getItem_name());

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


                // condition checking calculation will be accordingly "per plate" or "Weight" wise
                boolean isFixedWeightPP = false;
                if (getFixedWeight != null || getFixedWeight.isEmpty()) {
                    isFixedWeightPP = getFixedWeight.equals("1");
                }

                if (isFixedWeightPP) {
                    btn_50.setText(getActivity().getResources().getString(R.string._1));
                    btn_100.setText(getActivity().getResources().getString(R.string._2));
                    btn_150.setText(getActivity().getResources().getString(R.string._3));
                    btn_200.setText(getActivity().getResources().getString(R.string._4));
                    btn_250.setText(getActivity().getResources().getString(R.string._5));
                    btn_300.setText(getActivity().getResources().getString(R.string._6));
                    btn_350.setText(getActivity().getResources().getString(R.string._7));
                    btn_400.setText(getActivity().getResources().getString(R.string._8));
                    btn_450.setText(getActivity().getResources().getString(R.string._9));
                    btn_500.setText(getActivity().getResources().getString(R.string._10));
                    btn_550.setText(getActivity().getResources().getString(R.string._20));
                    btn_600.setText(getActivity().getResources().getString(R.string._25));
                    btn_650.setText(getActivity().getResources().getString(R.string._30));
                    btn_700.setText(getActivity().getResources().getString(R.string._35));
                    btn_750.setText(getActivity().getResources().getString(R.string._40));
                    btn_800.setText(getActivity().getResources().getString(R.string._45));
                    btn_850.setText(getActivity().getResources().getString(R.string._50));
                    btn_900.setText(getActivity().getResources().getString(R.string._100));
                    btn_950.setText(getActivity().getResources().getString(R.string._500));
                    btn_1000.setText(getActivity().getResources().getString(R.string._1000));

                }

                //======================================================================================


                // Find Price
                //------------------------------------------------------------------------------------------
                et_final_result_weight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable input) {

                    }
                });
                //------------------------------------------------------------------------------------------


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
                        et_weight_and_find_price_logic(et_final_result_weight.getText().toString(), getFixedPrice, getFixedWeight, position_from_rv_adapter);
                        dialog.dismiss();
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
        }
        });
//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#


//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#
        binding.btnShowPopupPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If item is selected than open popup
                if (position_from_rv_adapter > -1) {


                    Dialog dialog = new Dialog(getActivity());
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


                    tv_show_item_name.setText("" + dashboaedModelDataArrayList.get(position_from_rv_adapter).getItem_name());

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
                            et_price_and_find_weight_logic(et_final_result_price.getText().toString(), getFixedPrice, getFixedWeight, position_from_rv_adapter);
                            dialog.dismiss();
                        }
                    });


                    // Find Weight
                    //------------------------------------------------------------------------------------------
                    et_final_result_price.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable input) {

                        }
                    });
                    //------------------------------------------------------------------------------------------


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
            }
        });
//$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#




        //------------------------------------------------------------------------------------------
        Dialog dialog = new Dialog(getActivity());
        binding.btnViewReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setContentView(R.layout.receipt_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                //-------------------------------------------------------------------------------
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm");
                currentDateAndTime = sdf.format(new Date());
                TextView tv_bill_no, tv_date_time;
                tv_bill_no = (TextView) dialog.findViewById(R.id.tv_bill_no_value);
                tv_date_time = (TextView) dialog.findViewById(R.id.tv_date_time_value);

                tv_bill_no.setText(": "+ get_SharedPreference_Billnumber(getActivity()));
                tv_date_time.setText(": "+currentDateAndTime);
                //-------------------------------------------------------------------------------


                Button btn_close = dialog.findViewById(R.id.btn_close_popup);
                Button btn_print = dialog.findViewById(R.id.btn_print);

                TextView tv_cash = dialog.findViewById(R.id.tv_switch_cash);
                TextView tv_online = dialog.findViewById(R.id.tv_switch_online);

                final String[] final_payment_method = {" - Cash"};

                tv_cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final_payment_method[0] = " - Cash";
                        tv_cash.setTextColor(getResources().getColor(R.color.white));
                        tv_cash.setBackgroundColor(getResources().getColor(R.color.green_300));
                        tv_cash.setTypeface(Typeface.DEFAULT_BOLD);
                        tv_online.setTypeface(Typeface.DEFAULT);
                        tv_online.setTextColor(getResources().getColor(R.color.red_500));
                        tv_online.setBackgroundColor(getResources().getColor(R.color.grey_100));
                    }
                });

                tv_online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final_payment_method[0] = " - Online";
                        tv_cash.setTextColor(getResources().getColor(R.color.red_500));
                        tv_cash.setBackgroundColor(getResources().getColor(R.color.grey_100));
                        tv_cash.setTypeface(Typeface.DEFAULT);
                        tv_online.setTypeface(Typeface.DEFAULT_BOLD);
                        tv_online.setTextColor(getResources().getColor(R.color.white));
                        tv_online.setBackgroundColor(getResources().getColor(R.color.green_300));
                    }
                });


                item_name_list = new ArrayList<>();
                item_weight_list = new ArrayList<>();
                item_price_list = new ArrayList<>();

                for(int i = 0; i < dashboaedModelDataArrayList.size(); i++){

                  String getAmount_string = take_only_numbers_from_string(dashboaedModelDataArrayList.get(i).getAmount());
                    String item_name_string = dashboaedModelDataArrayList.get(i).getItem_name();
                    String item_weight_string = take_only_numbers_from_string(dashboaedModelDataArrayList.get(i).getWeight());
                    String item_price_string = take_only_numbers_from_string(dashboaedModelDataArrayList.get(i).getPrice());

                    if((getAmount_string != null) && !getAmount_string.isEmpty()) {
                        if((Long.parseLong(getAmount_string) > 0l)) {
                            item_name_list.add(item_name_string);
                            item_weight_list.add(item_weight_string);
                            item_price_list.add(item_price_string);
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

                        for (int i = 0; i <= item_name_list.size(); i++){

                            String item_name = item_name_list.get(i);
                            String item_weight = item_weight_list.get(i);
                            String item_price = item_price_list.get(i);

                            if(item_price.length() == 1) {
                                item_price = "    "+item_price;
                            }else if(item_price.length() == 2){
                                item_price = "   "+item_price;
                            }else if(item_price.length() == 3){
                                item_price = "  "+item_price;
                            }else if(item_price.length() == 4){
                                item_price = " "+item_price;
                            }

                            if(i == 0 ){
                                final_bill_string =  "\n[L]"+item_name+"[R]"+item_weight+"    "+item_price+"\n";
                                final_file_string = "\n["+item_name+", "+item_weight +", "+item_price + " Rs]\n";
                            }else if(i == (item_name_list.size() - 1)){
                                final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price;
                                final_file_string = final_file_string + ", ["+item_name+", "+item_weight +","+item_price + " Rs]\n";
                            }else{
                                final_bill_string = final_bill_string + "[L]"+item_name+"[R]"+item_weight+"    "+item_price+"\n";
                                final_file_string = final_file_string + ", ["+item_name+", "+item_weight +","+item_price + " Rs]\n";
                            }



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



                btn_print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
                        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MainActivity.PERMISSION_BLUETOOTH_ADMIN);
                        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, MainActivity.PERMISSION_BLUETOOTH_CONNECT);
                        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, MainActivity.PERMISSION_BLUETOOTH_SCAN);
                        } else {
                            Log.i("test_print", "Run bussiness logic : " + final_payment_method[0]);
                            Log.i("test_print", "final_bill_string :- " + final_bill_string);
                            Log.i("test_print", "grand_total :- " + grand_total);

                            // Generate Bill Number
                            //------------------------------------------------------------------------
                            int bill_no_integer = get_SharedPreference_Billnumber(getActivity());
                            set_SharedPreference_Billnumber(bill_no_integer + 1, getActivity());
                            //------------------------------------------------------------------------

                            // Only admin can print bills
                            if (DashboardFragment.get_SharedPreference_Is_Successfully_Logged_In(getActivity()) == 1) {
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        // Print Bill
                                        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                                        // Your code HERE
                                        try {

                                            if (item_name_list.size() != 0) {
                                                EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
                                                printer
                                                        .printFormattedText(
                                                                "[C]<font size='normal'><b>SURTI KHAMAN HOUSE</b></font>\n" +
                                                                        "[C]<font size='normal'>BORIVALI (EAST)</font>\n" +
                                                                        "[C]<font size='normal'>Mobile. 9137272150</font>\n" +
                                                                        "[L] Bill No : <font size='big'><b>" + bill_no_integer + "</b></font> " + final_payment_method[0] + "\n" +
                                                                        "[L]<font size='normal'> Date-Time : " + currentDateAndTime + "</font>\n" +
                                                                        "[L]<font size='normal'> Fssai : " + "21522012000953" + "</font>\n" +
                                                                        "[C]================================\n" +
                                                                        "[L] Items" +
                                                                        "[R] Weight" + "    Price\n" +
                                                                        "[L]<font size='small'>" + final_bill_string + "</font>\n" +
                                                                        "[C]================================\n" +
                                                                        "[C]GRAND TOTAL : <font size='big'><b>" + grand_total + "</b></font>\n" +
                                                                        "[C]================================"
                                                        );
                                            } else {

                                            }
                                            try {
                                                dialog.dismiss();
                                            } catch (Exception e) {
                                                e.getMessage();
                                                Log.i("test_print", "Exception dialog dismiss:" + e.getMessage());
                                            }
                                        } catch (EscPosConnectionException e) {
                                            e.printStackTrace();
                                            Log.i("test_print", "Exception 1 :- " + e.getMessage());
                                        } catch (EscPosParserException e) {
                                            e.printStackTrace();
                                            Log.i("test_print", "Exception 2 :- " + e.getMessage());
                                        } catch (EscPosEncodingException e) {
                                            e.printStackTrace();
                                            Log.i("test_print", "Exception 3 :- " + e.getMessage());
                                        } catch (EscPosBarcodeException e) {
                                            e.printStackTrace();
                                            Log.i("test_print", "Exception 4 :- " + e.getMessage());
                                        }


                                    }
                                    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

                                });
                            }


                            //---------------------------------------------------------------------------------------------------------
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Insert Into Database
                                    //-------------------------------------------------------------------------------------------------------------------------------------------------
                                    DashboardFragment.insert_Shop_Revenue_Data("" + bill_no_integer, currentDateAndTime, final_file_string, "" + grand_total, getActivity());
                                    //--------------------------------------------------------------------------------------------------------------------------------------------------


                                    // Insert Into File
                                    //-------------------------------------------------------------------------
                                    display_Shop_Revenue_Data(getActivity());

                                    previous_file_data = "";

                                    previous_file_data = DashboardFragment.get_SharedPreference_Old_data_bill_file_String(getActivity());

                                    if (!previous_file_data.isEmpty()) {
                                        previous_file_data = "\n@@ ---->DELETED FILE DATA / Old App data)<---- @@"
                                                + "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                                                + "\n" + previous_file_data + "\n"
                                                + "\n@@ ---->DELETED FILE DATA / Old App data)<---- @@"
                                                + "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n";
                                    }

                                    String final_file_data =
                                            previous_file_data
                                                    + "\n\n\n\n =$+$+$+$+$+$+$+$+$= TODAY'S EARNING =$+$+$+$+$+$+$+$+$=\n"
                                                    + "\n EARNING HISTORY \n"
                                                    + "\n " + bill_no_and__earning_history
                                                    + "\n -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
                                                    + "\n TODAY's EARNING : " + todays_earning_data
                                                    + "\n -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
                                                    + "\n =$+$+$+$+$+$+$+$+$= TODAY'S EARNING =$+$+$+$+$+$+$+$+$=\n\n\n\n"
                                                    + internal_file_data;

                                    DashboardFragment.check_and_create_file(getActivity(), final_file_data, MainActivity.file_name_surtikhamanhouse);

                                    DashboardFragment.check_and_create_file_insdie_package(getActivity(), final_file_data, MainActivity.file_name_surtikhamanhouse);
                                    //-------------------------------------------------------------------------

                                }

                            });
                            //---------------------------------------------------------------------------------------------------------

                            dialog.dismiss();
                            binding.btnReset.performClick();
                        }

                    }
                });

                dialog.show();
            }
        });
        //------------------------------------------------------------------------------------------




        return root;
    }
//=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    // Display Database Data
    //----------------------------------------------------------------------------------------------
    private void displayData() {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_MENU_TABLE_NAME+"",null);
        dashboaedModelDataArrayList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String item_name = cursor.getString(1);
            String item_weight = cursor.getString(2);
            String item_price = cursor.getString(3);
            dashboaedModelDataArrayList.add(new DashboaedModelData(item_name, "", "", "",  item_price, item_weight));
        }
        cursor.close();
    }
    //----------------------------------------------------------------------------------------------


    @Override
    public void onResume() {
        super.onResume();


        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String currentDateandTime = sdf.format(new Date());
            String last_dat = DashboardFragment.get_last_day_for_bill_no_sharedpreference(getActivity());

            // Today's date is not same with last day, Reset Bill Number
            if(!currentDateandTime.equals(last_dat)){
                set_SharedPreference_Billnumber(1, getActivity());
            }
            DashboardFragment.set_last_day_for_bill_no_sharedpreference(currentDateandTime, getActivity());


        }catch (Exception e){
            e.getMessage();
        }
    }

    //----------------------------------------------------------------------------------------------
    public void display_Shop_Revenue_Data(Context context) {
        sqLiteDatabase=databaseMain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+ DatabaseMain.SHOP_REVENUE_TABLE_NAME+"",null);
        internal_file_data = "";

        Long todays_total_earning = 0l;
        bill_no_and__earning_history = "";
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String SHOP_REVENUE_BILL_NO_COLUMN = cursor.getString(1);
            String SHOP_REVENUE_BILL_DATE_TIME_COLUMN = cursor.getString(2);
            String SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN = cursor.getString(3);
            String SHOP_REVENUE_BILL_AMOUNT_COLUMN = cursor.getString(4);
            internal_file_data =  internal_file_data
                    +"\n (ID : "+id+") >>>>====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"\n"
                    +"\n Bill No : "+SHOP_REVENUE_BILL_NO_COLUMN
                    +"\n Date Time : "+SHOP_REVENUE_BILL_DATE_TIME_COLUMN
                    +"\n"+SHOP_REVENUE_ITEM_NAME_WEIGHT_PRICE_COLUMN
                    +"\n Grand Total : "+SHOP_REVENUE_BILL_AMOUNT_COLUMN
                    +"\n (ID : "+id+") >>>>====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"====Bill_"+SHOP_REVENUE_BILL_NO_COLUMN+"\n";

            Long grand_total_long = Long.parseLong(SHOP_REVENUE_BILL_AMOUNT_COLUMN);
            bill_no_and__earning_history = bill_no_and__earning_history
                    +"\n"
                    +SHOP_REVENUE_BILL_NO_COLUMN
                    +") "
                    + SHOP_REVENUE_BILL_DATE_TIME_COLUMN
                    +" ----> "
                    +SHOP_REVENUE_BILL_AMOUNT_COLUMN;
            todays_total_earning = todays_total_earning + grand_total_long;
        }

        todays_earning_data = todays_total_earning;
        cursor.close();
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
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
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
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
    //----------------------------------------------------------------------------------------------




    // Enter dynamic price and finding weight
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-
    public void et_price_and_find_weight_logic(String input_dp_string, String fp_string, String fw_string, int position){

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
                dp_string = take_only_numbers_from_string(dp_string);
                dp_long = Long.parseLong(dp_string);
            }

            if(!fw_string.isEmpty()){
                fw_long = Long.parseLong(fw_string);
            }

            Long find_dw_long = findDynamicWeight(fw_long, fp_long, dp_long);

//        Toast.makeText(getContext(), "Dynamic Weight = "+find_dw_long, Toast.LENGTH_LONG).show();

            if(find_dw_long != null) {
                String find_dw_string = Long.toString(find_dw_long);
//                holder.et_final_result_weight.setText(find_dw_string);
                dashboaedModelDataArrayList.get(position).setDynamicWeight(find_dw_string);

                if(!boxDashboardSelectedItemModelDataHashMap.containsKey(dashboaedModelDataArrayList.get(position).getItem_name())) {
                    boxDashboardSelectedItemModelDataHashMap.put(dashboaedModelDataArrayList.get(position).getItem_name(), position);
                    boxDashboardSelectedItemModelDataArrayList.add(new BoxDashboardSelectedItemModelData(dashboaedModelDataArrayList.get(position).getItem_name(), dashboaedModelDataArrayList.get(position).getWeight(), dashboaedModelDataArrayList.get(position).getPrice()));
                }else{

                        Iterator<BoxDashboardSelectedItemModelData> iter = boxDashboardSelectedItemModelDataArrayList.iterator();
                        while (iter.hasNext()) {
                            BoxDashboardSelectedItemModelData boxDashboardSelectedItemModelData = iter.next();
                            if (boxDashboardSelectedItemModelData.get_selected_item_name().contains(dashboaedModelDataArrayList.get(position).getItem_name())){
                                boxDashboardSelectedItemModelDataArrayList.remove(iter);
                                iter.remove();
                            }
                        }

                    boxDashboardSelectedItemModelDataArrayList.add(new BoxDashboardSelectedItemModelData(dashboaedModelDataArrayList.get(position).getItem_name(), dashboaedModelDataArrayList.get(position).getWeight(), dashboaedModelDataArrayList.get(position).getPrice()));
                }
                //==========================================================================================
                BoxDashboardSelectedItemRecycleViewAdapter adapter = new BoxDashboardSelectedItemRecycleViewAdapter(boxDashboardSelectedItemModelDataArrayList, getActivity());
                rv_box_dashboard_item_selected.setHasFixedSize(true);
//        rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                rv_box_dashboard_item_selected.setLayoutManager(gridLayoutManager);
                rv_box_dashboard_item_selected.setAdapter(adapter);
                //==========================================================================================
            }else{
//                holder.et_final_result_weight.setText("");
            }

            if(input_dp_string.isEmpty()){
                dashboaedModelDataArrayList.get(position).setDynamicWeight("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-

    public String take_only_numbers_from_string(String value){
        if(!value.isEmpty()){
            value = value.replaceAll("[^0-9]", "");
        }
        return value;
    }

    // Enter dynamic price and finding weight
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-
    public void et_weight_and_find_price_logic(String input_dw_string, String fp_string, String fw_string, int position) {
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
                dw_string = take_only_numbers_from_string(dw_string);
                dw_long = Long.parseLong(dw_string);;
            }

            if(!fw_string.isEmpty()){
                fw_long = Long.parseLong(fw_string);
            }


            Long find_dp_long = findDynamicPrice(fp_long, dw_long, fw_long);
            String find_dp_string = "";
            if(find_dp_long != null && find_dp_long != 0l) {
                find_dp_string = Long.toString(find_dp_long);

//                Toast.makeText(getContext(), "Dynamic Price = "+find_dp_string, Toast.LENGTH_LONG).show();
//
//                holder.et_final_result_price.setText(find_dp_string);
                dashboaedModelDataArrayList.get(position).setDynamicPrice(find_dp_string);
                dashboaedModelDataArrayList.get(position).setCalculatedAmount(find_dp_string);

                if(!boxDashboardSelectedItemModelDataHashMap.containsKey(dashboaedModelDataArrayList.get(position).getItem_name())) {
                    boxDashboardSelectedItemModelDataHashMap.put(dashboaedModelDataArrayList.get(position).getItem_name(), position);
                    boxDashboardSelectedItemModelDataArrayList.add(new BoxDashboardSelectedItemModelData(dashboaedModelDataArrayList.get(position).getItem_name(), dashboaedModelDataArrayList.get(position).getWeight(), dashboaedModelDataArrayList.get(position).getPrice()));
                }else{
                    Iterator<BoxDashboardSelectedItemModelData> iter = boxDashboardSelectedItemModelDataArrayList.iterator();
                    while (iter.hasNext()) {
                        BoxDashboardSelectedItemModelData boxDashboardSelectedItemModelData = iter.next();
                        if (boxDashboardSelectedItemModelData.get_selected_item_name().contains(dashboaedModelDataArrayList.get(position).getItem_name())){
                            boxDashboardSelectedItemModelDataArrayList.remove(iter);
                            iter.remove();
                        }
                    }

                    boxDashboardSelectedItemModelDataArrayList.add(new BoxDashboardSelectedItemModelData(dashboaedModelDataArrayList.get(position).getItem_name(), dashboaedModelDataArrayList.get(position).getWeight(), dashboaedModelDataArrayList.get(position).getPrice()));
                }
                
                //==========================================================================================
                BoxDashboardSelectedItemRecycleViewAdapter adapter = new BoxDashboardSelectedItemRecycleViewAdapter(boxDashboardSelectedItemModelDataArrayList, getActivity());
                rv_box_dashboard_item_selected.setHasFixedSize(true);
//        rv_box_dashboard.setLayoutManager(new LinearLayoutManager(getContext()));
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                rv_box_dashboard_item_selected.setLayoutManager(gridLayoutManager);
                rv_box_dashboard_item_selected.setAdapter(adapter);
                //==========================================================================================

            }else{
//                holder.et_final_result_price.setText("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }




            if(input_dw_string.isEmpty()){
                dashboaedModelDataArrayList.get(position).setDynamicWeight("");
                dashboaedModelDataArrayList.get(position).setDynamicPrice("");
                dashboaedModelDataArrayList.get(position).setCalculatedAmount("");
            }
    }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=--==--=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-




    // Find dynamic price or Dynamic weight
    //----------------------------------------------------------------------------------------------

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
    //----------------------------------------------------------------------------------------------
}
