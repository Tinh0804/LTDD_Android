package com.example.learninglanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.UnitAdapter;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.viewmodels.UnitViewModel;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {

    private ListView listViewUnits;
    private UnitAdapter unitAdapter;
    private List<Unit> unitList;
    private UnitViewModel unitViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_practice, container, false);

        listViewUnits = view.findViewById(R.id.listUnits);

        unitList = new ArrayList<>();
        unitAdapter = new UnitAdapter(requireContext(), unitList);
        listViewUnits.setAdapter(unitAdapter);

        unitViewModel = new ViewModelProvider(requireActivity()).get(UnitViewModel.class);

        unitViewModel.getUnitsWithLessonsLiveData().observe(getViewLifecycleOwner(), unitsWithLessons -> {
            if (unitsWithLessons != null) {
                unitList.clear();
                for (UnitWithLessons uwl : unitsWithLessons)
                    unitList.add(uwl.getUnit());
                unitAdapter.notifyDataSetChanged();
            }
        });

        unitViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });

        listViewUnits.setOnItemClickListener((parent, view1, position, id) -> {
            Unit clickedUnit = unitList.get(position);
            if (clickedUnit.isLocked()) {
                Toast.makeText(getContext(), "Bạn vẫn chưa mở khoá được unit để luyện tập!", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putInt("unitId", clickedUnit.getUnitId());

            GameFragment gameFragment = new GameFragment();
            gameFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, gameFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        unitViewModel.loadUnitsWithLessons();
    }
}
