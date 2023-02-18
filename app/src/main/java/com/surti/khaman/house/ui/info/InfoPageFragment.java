package com.surti.khaman.house.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.surti.khaman.house.BuildConfig;
import com.surti.khaman.house.databinding.FragmentInfoPageBinding;


public class InfoPageFragment extends Fragment {
    FragmentInfoPageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        binding.tvAppVersion.setText(versionName + " ("+versionCode+")");

        return root;
    }

}
