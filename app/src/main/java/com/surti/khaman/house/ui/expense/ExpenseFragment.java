package com.surti.khaman.house.ui.expense;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentExpenseBinding;

public class ExpenseFragment extends Fragment {
    int numberOfLines = 0;
    private FragmentExpenseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExpenseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnAddExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Line();
            }
        });
        return root;
    }

    public void Add_Line() {
        // add edittext
        TextView tv_expense_label = new TextView(getActivity());
        EditText et_expenses_note = new EditText(getActivity());
        EditText et_expenses_amount = new EditText(getActivity());


        LinearLayout.LayoutParams lp_expenses_label = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_expenses_label.setMargins(10, 10, 10, 10);
        tv_expense_label.setLayoutParams(lp_expenses_label);
        tv_expense_label.setText(numberOfLines+" EXPENSES : Enter Amount and Note");
        tv_expense_label.setTextSize(18f);
        tv_expense_label.setTextColor(getResources().getColor(R.color.red_500));
        tv_expense_label.setTypeface(Typeface.DEFAULT_BOLD);
        tv_expense_label.setId(numberOfLines + 1);

        LinearLayout.LayoutParams lp_expenses_amount = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_expenses_label.setMargins(10, 10, 10, 10);
        et_expenses_amount.setLayoutParams(lp_expenses_amount);
        et_expenses_amount.setHint("Enter Expenses Amount");
        et_expenses_amount.setText("");
        et_expenses_amount.setTextColor(getResources().getColor(R.color.blue_500));
        et_expenses_amount.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_expenses_amount.setId(numberOfLines + 1);

        LinearLayout.LayoutParams lp_expenses_note = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_expenses_note.setMargins(10, 10, 10, 10);
        et_expenses_note.setLayoutParams(lp_expenses_note);
        et_expenses_note.setHint("Enter Expenses Note");
        et_expenses_note.setTextColor(getResources().getColor(R.color.blue_500));
        et_expenses_note.setText("");
        et_expenses_note.setId(numberOfLines + 1);



        binding.linearLayoutDecisions.addView(tv_expense_label);
        binding.linearLayoutDecisions.addView(et_expenses_amount);
        binding.linearLayoutDecisions.addView(et_expenses_note);
        numberOfLines++;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}