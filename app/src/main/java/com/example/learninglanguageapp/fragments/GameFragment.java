package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.GameWordropActivity;
import com.example.learninglanguageapp.activities.MatchGameActivity;
import com.example.learninglanguageapp.activities.MatchingGameActivity;
import com.example.learninglanguageapp.activities.SelectImageGameActivity;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;

public class GameFragment extends Fragment {

    private ExerciseViewModel viewModel;
    // Biến tạm thời để lưu Activity sẽ mở sau khi tải Exercises thành công
    private Class<?> nextActivityClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Cần tạo View ở đây
        return inflater.inflate(R.layout.activity_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy ViewModel từ Activity để chia sẻ dữ liệu
        viewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
        CardView cardImageWordGame = view.findViewById(R.id.cardImageWordGame);
        CardView cardViewArrangeWord = view.findViewById(R.id.cardArrangeWordGame);
        CardView cardViewMatchingGame = view.findViewById(R.id.cardMatchingGame);
        CardView cardViewPictureVocabulary = view.findViewById(R.id.cardVocabularyGame);

        // 1. QUAN SÁT LIVE DATA MỘT LẦN KHI VIEW ĐƯỢC TẠO
        viewModel.exercisesLiveData.observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null && !exercises.isEmpty() && nextActivityClass != null) {
                // Chỉ mở Activity nếu có dữ liệu VÀ đã xác định Activity cần mở
                Intent intent = new Intent(requireContext(), nextActivityClass);
                intent.putExtra("unitId", unitId);
                // Bạn có thể bundle danh sách exercises vào đây nếu cần thiết cho Activity
                startActivity(intent);

                // ❗ Rất quan trọng: Reset giá trị và biến để tránh kích hoạt lại
                viewModel.exercisesLiveData.setValue(null);
                nextActivityClass = null;
            }
        });

        // Chỉ quan sát lỗi
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.errorLiveData.setValue(null);
            }
        });

        // 2. GÁN CLICK LISTENER: Gọi fetch và set biến Activity mục tiêu
        cardImageWordGame.setOnClickListener(v -> triggerFetchAndOpenGame(unitId, "listen", SelectImageGameActivity.class));
        cardViewPictureVocabulary.setOnClickListener(v -> triggerFetchAndOpenGame(unitId, "match_pairs", MatchGameActivity.class));
        cardViewArrangeWord.setOnClickListener(v -> triggerFetchAndOpenGame(unitId, "word_order", GameWordropActivity.class));
        cardViewMatchingGame.setOnClickListener(v -> triggerFetchAndOpenGame(unitId, "match_pairs", MatchingGameActivity.class));
    }

    private void triggerFetchAndOpenGame(int unitId, String type, Class<?> activityClass) {
        if (unitId == -1) {
            Toast.makeText(requireContext(), "Lỗi: Unit ID không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Lưu Activity cần mở vào biến tạm
        this.nextActivityClass = activityClass;
        // Bắt đầu tải dữ liệu. Khi dữ liệu được post, Observer ở trên sẽ tự động mở Activity.
        viewModel.fetchExercisesByType(unitId, type);
    }
}
