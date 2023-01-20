package com.surti.khaman.house.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surti.khaman.house.Adapter.DashboardRecyclerViewAdapter;
import com.surti.khaman.house.Interface.DashboardInterface;
import com.surti.khaman.house.Model.DashboaedModelData;
import com.surti.khaman.house.R;
import com.surti.khaman.house.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment implements DashboardInterface {

    private FragmentDashboardBinding binding;
    DashboardInterface dashboardInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardInterface = new DashboardInterface() {
            @Override
            public void onTotalAmountChange(String totalAmount) {
                binding.tvTotalAmount.setText(totalAmount + " RS");

            }
        };

        //------------------------------------------------------------------------------------------

        DashboaedModelData[] myListData = new DashboaedModelData[]{
                new DashboaedModelData("Khaman", "0", "0", "0", "200"),
                new DashboaedModelData("N.Khaman ", "0", "0", "0", "200"),
                new DashboaedModelData("Dhokla", "0", "0", "0", "200"),
                new DashboaedModelData("Patra", "0", "0", "0", "200"),
                new DashboaedModelData("Khandvi", "0", "0", "0", "240"),
                new DashboaedModelData("Khamni", "0", "0", "0", "240"),
                new DashboaedModelData("Fafda", "0", "0", "0", "360"),
                new DashboaedModelData("Jalebi", "0", "0", "0", "300"),
                new DashboaedModelData("Samosa", "0", "0", "0", "20"),
                new DashboaedModelData("P.samosa", "0", "0", "0", "240")};


        RecyclerView dashboard_recycleView = (RecyclerView) root.findViewById(R.id.dashboard_recycleView);
        DashboardRecyclerViewAdapter adapter = new DashboardRecyclerViewAdapter(myListData, dashboardInterface);
        dashboard_recycleView.setHasFixedSize(true);
        dashboard_recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        dashboard_recycleView.setAdapter(adapter);

        //------------------------------------------------------------------------------------------


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTotalAmountChange(String totalAmount) {

    }
}
