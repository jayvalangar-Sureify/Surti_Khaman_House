package com.surti.khaman.house.ui.wholesale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.surti.khaman.house.databinding.FragmentWholesaleBinding;

public class Wholesale extends Fragment {

    private FragmentWholesaleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWholesaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
