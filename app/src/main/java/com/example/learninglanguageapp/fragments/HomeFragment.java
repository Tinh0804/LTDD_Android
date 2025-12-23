package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.LessonActivity;
import com.example.learninglanguageapp.models.*;
import com.example.learninglanguageapp.viewmodels.UnitViewModel;
import java.util.List;

public class HomeFragment extends Fragment {
    private UnitViewModel unitViewModel;
    private LinearLayout lessonContainer;
    private int lessonGlobalIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lessonContainer = view.findViewById(R.id.lessonContainer);

        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        unitViewModel.getUnitsWithLessonsLiveData().observe(getViewLifecycleOwner(), this::renderCoursePath);

        return view;
    }

    private void renderCoursePath(List<UnitWithLessons> unitWithLessonsList) {
        lessonContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        lessonGlobalIndex = 0;

        for (UnitWithLessons uwl : unitWithLessonsList) {
            Unit unit = uwl.getUnit();

            // Header Unit
            View headerView = inflater.inflate(R.layout.item_unit_header_line, lessonContainer, false);
            ((TextView) headerView.findViewById(R.id.tvUnitName)).setText(unit.getUnitName());
            lessonContainer.addView(headerView);

            List<Lesson> lessons = uwl.getLessons();
            if (lessons == null) continue;

            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);
                View lessonView;

                // Hiển thị mascot ở bài học thứ 3 của mỗi Unit
                if (i == 2)
                    lessonView = createLessonWithMascotRow(inflater, unit, lesson, lessonGlobalIndex);
                else
                    lessonView = createLessonView(inflater, unit, lesson, lessonGlobalIndex);
                lessonContainer.addView(lessonView);
                lessonGlobalIndex++;
            }
        }
    }

    private void setupLessonUI(View view, Unit unit, Lesson lesson) {
        FrameLayout lessonCard = view.findViewById(R.id.lessonCard);
        ImageView iconView = view.findViewById(R.id.iconView);

        // Logic: Khóa nếu Unit bị khóa HOẶC Lesson yêu cầu unlockRequired = true
        boolean isLocked = unit.isLocked() || lesson.isUnlockRequired();

        if (isLocked) {
            lessonCard.setBackgroundResource(R.drawable.bg_circle_locked);
            iconView.setImageResource(R.drawable.ic_lock);
            iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_locked));
            lessonCard.setAlpha(0.7f);
        } else {
            int bg = (lesson.getOrderIndex() % 2 == 0) ? R.drawable.bg_gradient_purple : R.drawable.bg_gradient_yellow;
            lessonCard.setBackgroundResource(bg);
            iconView.setImageResource(R.drawable.ic_star);
            iconView.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white));
            lessonCard.setAlpha(1.0f);
        }

        lessonCard.setOnClickListener(v -> {
            if (isLocked)
                Toast.makeText(getContext(), "Vui lòng hoàn thành bài học trước!", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(getActivity(), LessonActivity.class);
                intent.putExtra("LESSON_ID", lesson.getLessonId());
                intent.putExtra("CATEGORY_NAME", lesson.getLessonName());
                startActivity(intent);
            }
        });
    }

    private View createLessonView(LayoutInflater inflater, Unit unit, Lesson lesson, int index) {
        View view = inflater.inflate(R.layout.item_lesson_circle, null, false);
        setupLessonUI(view, unit, lesson);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 20;
        params.bottomMargin = 20;

        int baseMargin = (int) (getResources().getDisplayMetrics().density * 20);
        int offsetMargin = (int) (getResources().getDisplayMetrics().density * 15);
        int positionCycle = index % 5;

        switch (positionCycle) {
            case 0:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = baseMargin + offsetMargin;
                params.rightMargin = baseMargin - offsetMargin;
                break;
            case 1:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = baseMargin - (offsetMargin / 2);
                params.rightMargin = baseMargin + (offsetMargin / 2);
                break;
            case 2:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = baseMargin - offsetMargin;
                params.rightMargin = baseMargin + offsetMargin;
                break;
            case 3:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = baseMargin - (offsetMargin / 2);
                params.rightMargin = baseMargin + (offsetMargin / 2);
                break;
            case 4:
                params.gravity = Gravity.CENTER_HORIZONTAL;
                params.leftMargin = baseMargin + offsetMargin;
                params.rightMargin = baseMargin - offsetMargin;
                break;
        }

        view.setLayoutParams(params);
        return view;
    }

    private View createLessonWithMascotRow(LayoutInflater inflater, Unit unit, Lesson lesson, int index) {
        // Tạo LinearLayout ngang để chứa lesson và mascot
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rowParams.topMargin = 20;
        rowParams.bottomMargin = 20;
        row.setLayoutParams(rowParams);

        // Tạo lesson view
        View lessonView = inflater.inflate(R.layout.item_lesson_circle, null, false);
        setupLessonUI(lessonView, unit, lesson);

        LinearLayout.LayoutParams lessonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lessonParams.rightMargin = (int) (getResources().getDisplayMetrics().density * 20);
        lessonView.setLayoutParams(lessonParams);
        row.addView(lessonView);

        // Thêm mascot
        ImageView mascot = new ImageView(getContext());
        mascot.setImageResource(R.drawable.mascot);
        LinearLayout.LayoutParams mascotParams = new LinearLayout.LayoutParams(
                (int) (getResources().getDisplayMetrics().density * 80),
                (int) (getResources().getDisplayMetrics().density * 80)
        );
        mascot.setLayoutParams(mascotParams);
        mascot.setScaleType(ImageView.ScaleType.FIT_CENTER);
        row.addView(mascot);

        return row;
    }
}
