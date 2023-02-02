package com.surti.khaman.house.ui.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}