package com.example.learninglanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.UnitAdapter;
import com.example.learninglanguageapp.models.Unit;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {

    private ListView listViewUnits;
    private UnitAdapter unitAdapter;
    private List<Unit> mockUnitList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_practice, container, false);

        listViewUnits = view.findViewById(R.id.listUnits);

        // Tạo dữ liệu giả
        mockUnitList = new ArrayList<>();
        mockUnitList.add(new Unit(1, 1, "Animals", 1));
        mockUnitList.add(new Unit(2, 1, "Family", 2));
        mockUnitList.add(new Unit(3, 1, "Food", 3));
        mockUnitList.add(new Unit(4, 1, "Weather", 4));
        mockUnitList.add(new Unit(5, 1, "Daily Activities", 5));

        unitAdapter = new UnitAdapter(requireContext(), mockUnitList);
        listViewUnits.setAdapter(unitAdapter);

        listViewUnits.setOnItemClickListener((parent, view1, position, id) -> {

            Unit clickedUnit = mockUnitList.get(position);

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
}
