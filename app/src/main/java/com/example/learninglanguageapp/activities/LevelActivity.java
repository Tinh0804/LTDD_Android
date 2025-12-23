
package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.LevelAdapter;
import com.example.learninglanguageapp.models.Course;
import com.example.learninglanguageapp.models.UIModel.Level;
import com.example.learninglanguageapp.viewmodels.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

//public class LevelActivity extends AppCompatActivity {
//
//    private LevelAdapter adapter;
//
//    @Override
//    @SuppressLint("MissingInflatedId")
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.level); // layout riêng cho LevelActivity, có ListView với id lvLevels
//
//        ImageButton btnBack = findViewById(R.id.btnBack);
//        Button btnContinue = findViewById(R.id.btnContinue);
//        ListView lvLevels = findViewById(R.id.lvLevels);
//
//        btnBack.setOnClickListener(v -> finish());
//
//        // Danh sách level
//        List<Level> levels = new ArrayList<>();
//        levels.add(new Level("Beginner", "I know some basics", R.drawable.beginer,1));
//        levels.add(new Level("Elementary", "I know some basics", R.drawable.element,2));
//        levels.add(new Level("Intermediate", "I know some basics", R.drawable.star,3));
//        levels.add(new Level("Advanced", "I know some basics", R.drawable.advance,4));
//
//        adapter = new LevelAdapter(this, levels);
//        lvLevels.setAdapter(adapter);
//
//        btnContinue.setOnClickListener(v -> {
//            Level selectedLevel = adapter.getSelectedLevel();
//            if (selectedLevel != null) {
//
//                Bundle bundle = getIntent().getBundleExtra("data");
//                if (bundle == null) bundle = new Bundle(); // ✅
//
//                bundle.putString("level_id", selectedLevel.getId()+""); // ✅ ID
//
//                Intent intent = new Intent(LevelActivity.this, StudyTimeActivity.class);
//                intent.putExtra("data", bundle);  // ✅ CHÍNH XÁC
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    public static class HomeFragment extends Fragment {
//
//        public HomeFragment() {
//            // Required empty public constructor
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater,
//                                 @Nullable ViewGroup container,
//                                 @Nullable Bundle savedInstanceState) {
//            // Inflate the layout for this fragment
//            return inflater.inflate(R.layout.fragment_home, container, false);
//        }
//    }
//}
public class LevelActivity extends AppCompatActivity {
    private LevelAdapter adapter;
    private List<Course> courseList = new ArrayList<>();
    private CourseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);

        // 1. Ánh xạ View
        ListView lvLevels = findViewById(R.id.lvLevels);
        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        // 2. Setup Adapter (Đảm bảo LevelAdapter đã sửa để nhận List<Course>)
        adapter = new LevelAdapter(this, courseList);
        lvLevels.setAdapter(adapter);
        // 3. Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        viewModel.init(this);

        // 4. Đăng ký quan sát dữ liệu (Observe)
        observeViewModel();

        Bundle bundle = getIntent().getBundleExtra("data");
        if (bundle != null) {
            // Bây giờ getInt sẽ lấy được giá trị thực tế vì bên kia đã putInt
            int fromLangId = bundle.getInt("language_use_id", 2);
            int toLangId = bundle.getInt("language_learn_id", 1);
            viewModel.fetchCourses(fromLangId, toLangId);
        }

        // Sự kiện nút bấm
        btnBack.setOnClickListener(v -> finish());
        btnContinue.setOnClickListener(v -> {
            Course selected = adapter.getSelectedCourse();
            if (selected != null) {
                moveToNextStep(selected);
            } else {
                Toast.makeText(this, "Vui lòng chọn trình độ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getCourses().observe(this, courses -> {
            if (courses != null) {
                courseList.clear();
                courseList.addAll(courses);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            // Hiện/ẩn ProgressBar nếu bạn có trong layout
            // Example: progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
    }

    private void moveToNextStep(Course course) {
        Bundle bundle = getIntent().getBundleExtra("data");
        if (bundle == null) bundle = new Bundle();

        bundle.putInt("course_id", course.getCourseId());
        bundle.putString("difficulty", course.getDifficultyLevel());

        Intent intent = new Intent(this, StudyTimeActivity.class);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }
}