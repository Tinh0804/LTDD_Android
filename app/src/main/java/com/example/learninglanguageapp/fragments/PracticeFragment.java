package com.example.learninglanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.UnitAdapter;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {

    private ListView listViewUnits;
    private UnitAdapter unitAdapter;
    private List<Unit> mockUnitList;
    private ExerciseViewModel exerciseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_practice, container, false);
        listViewUnits = view.findViewById(R.id.listUnits);

        // Đảm bảo ViewModel được chia sẻ (requireActivity())
        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        // Tạo dữ liệu giả
        mockUnitList = new ArrayList<>();
        mockUnitList.add(new Unit(1, 1, "Animals", 1));
        mockUnitList.add(new Unit(2, 1, "Family", 2));
        mockUnitList.add(new Unit(3, 1, "Food", 3));
        mockUnitList.add(new Unit(4, 1, "Weather", 4));
        mockUnitList.add(new Unit(5, 1, "Daily Activities", 5));

        unitAdapter = new UnitAdapter(requireContext(), mockUnitList);
        listViewUnits.setAdapter(unitAdapter);

        final int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
        if (unitId != -1) {
            // Gọi phương thức tải TẤT CẢ bài tập của Unit (không lọc theo type)
            // Điều này kích hoạt kiểm tra cache và gọi API nếu cache trống.
            exerciseViewModel.fetchExercises(unitId);
        }

        // Chỉ quan sát lỗi (nếu có)
        exerciseViewModel.errorLiveData.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                // ❗ Rất quan trọng: Xóa giá trị lỗi sau khi hiển thị
                exerciseViewModel.errorLiveData.setValue(null);
            }
        });

        // 2️⃣ Click vào unit: Chuyển Fragment và truyền unitId
        listViewUnits.setOnItemClickListener((parent, view1, position, id) -> {
            Unit clickedUnit = mockUnitList.get(position);

            Bundle bundle = new Bundle();
            // Truyền ID để GameFragment biết nó cần tải dữ liệu nào
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