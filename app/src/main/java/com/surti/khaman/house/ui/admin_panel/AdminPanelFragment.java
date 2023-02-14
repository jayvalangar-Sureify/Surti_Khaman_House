package com.surti.khaman.house.ui.admin_panel;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.surti.khaman.house.R;
import com.surti.khaman.house.WorkerDirectory.UploadPDF;
import com.surti.khaman.house.databinding.FragmentAdminPanelBinding;
import com.surti.khaman.house.ui.dashboard.DashboardFragment;


public class AdminPanelFragment  extends Fragment {

    static FragmentAdminPanelBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminPanelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_password_dialog(getActivity());
            }
        });
        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        checkLoggedInValue(getActivity());
    }

    private void checkLoggedInValue(FragmentActivity activity) {

        if (DashboardFragment.get_SharedPreference_Is_Successfully_Logged_In(getActivity()) == 1) {
            binding.tvIsAdminLoginValue.setText(getActivity().getResources().getString(R.string.admin_login_success));
            binding.tvIsAdminLoginValue.setTextColor(getActivity().getResources().getColor(R.color.green_500));
            binding.btnLogin.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                UploadPDF.myWorkManager(getActivity());
            }
        } else {
            binding.tvIsAdminLoginValue.setText(getActivity().getResources().getString(R.string.admin_login_unsuccess));
            binding.tvIsAdminLoginValue.setTextColor(getActivity().getResources().getColor(R.color.red_500));
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }

    public static void show_password_dialog(Context context) {

        Dialog dialog = new Dialog(context);
        //==================================================================================
        dialog.setContentView(R.layout.password_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        //-------------------------------------------------------------------------------
        EditText et_password;
        TextView tv_dynamic_result;
        Button btn_0, btn_1, btn_2,
                btn_3, btn_4, btn_5,
                btn_6, btn_7, btn_8,
                btn_9, btn_clear, btn_info, btn_close_popup;

        tv_dynamic_result = (TextView) dialog.findViewById(R.id.tv_dynamic_result);
        et_password = (EditText) dialog.findViewById(R.id.et_password);;
        btn_0 = (Button) dialog.findViewById(R.id.btn_0);
        btn_1 = (Button) dialog.findViewById(R.id.btn_1);
        btn_2 = (Button) dialog.findViewById(R.id.btn_2);
        btn_3 = (Button) dialog.findViewById(R.id.btn_3);
        btn_4 = (Button) dialog.findViewById(R.id.btn_4);
        btn_5 = (Button) dialog.findViewById(R.id.btn_5);
        btn_6 = (Button) dialog.findViewById(R.id.btn_6);
        btn_7 = (Button) dialog.findViewById(R.id.btn_7);
        btn_8 = (Button) dialog.findViewById(R.id.btn_8);
        btn_9 = (Button) dialog.findViewById(R.id.btn_9);
        btn_close_popup = (Button) dialog.findViewById(R.id.btn_close_popup);
        btn_clear = (Button) dialog.findViewById(R.id.btn_clear);
        btn_info = (Button) dialog.findViewById(R.id.btn_info);


        //------------------------------------------------------------------------------------------
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+context.getResources().getString(R.string.info_password), Toast.LENGTH_LONG).show();
            }
        });

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_0, et_password, context, dialog, tv_dynamic_result);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_1, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_2, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_3, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_4, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_5, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_6, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_7, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_8, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_9, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password_for_calculation(btn_0, et_password, context, dialog, tv_dynamic_result);
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entering_password = "";
                et_password.setText("");
            }
        });

        btn_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //------------------------------------------------------------------------------------------

        dialog.show();
    }


    public static String entering_password = "";
    public static void entering_password_for_calculation(Button btn, EditText et_password, Context context, Dialog dialog, TextView tv_dynamic_result) {
        entering_password = entering_password + btn.getText().toString();

        et_password.setText(entering_password);


        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input_password = charSequence.toString();
                if(i2 == 10) {
                    if (checkPasswordValidation(input_password, context)) {
                        dialog.dismiss();
                        DashboardFragment.set_SharedPreference_Is_Successfully_Logged_In(1, context);
                        binding.tvIsAdminLoginValue.setText(context.getResources().getString(R.string.admin_login_success));
                        binding.tvIsAdminLoginValue.setTextColor(context.getResources().getColor(R.color.green_500));
                        binding.btnLogin.setVisibility(View.GONE);
                    } else {
                        entering_password = "";
                        binding.tvIsAdminLoginValue.setText(context.getResources().getString(R.string.admin_login_unsuccess));
                        binding.tvIsAdminLoginValue.setTextColor(context.getResources().getColor(R.color.red_500));
                        tv_dynamic_result.setText(context.getResources().getString(R.string.wrong_password));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable input) {

            }
        });
    }

    private static boolean checkPasswordValidation(String entering_password, Context context) {
        boolean result =false;
        if (!entering_password.isEmpty() && entering_password != null) {
            if (entering_password.equals(DashboardFragment.get_password_sharedpreference(context))) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
    //------------------------------------------------------------------------------------------------------------------------

}
