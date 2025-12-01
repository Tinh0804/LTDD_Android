package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.LessonActivity;
import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.viewmodels.UnitViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private UnitViewModel unitViewModel;
    private LinearLayout lessonContainer;
    private ProgressBar progressBar;

    private int lessonIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lessonContainer = view.findViewById(R.id.lessonContainer);
        progressBar = view.findViewById(R.id.progressBar);

        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);

        unitViewModel.getUnitsWithLessonsLiveData().observe(getViewLifecycleOwner(), this::renderCoursePath);

        unitViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                lessonContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
        });

        unitViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null)
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
        });

        return view;
    }

    private void renderCoursePath(List<UnitWithLessons> unitWithLessonsList) {
        lessonContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        lessonIndex = 0;

        for (UnitWithLessons unitWithLessons : unitWithLessonsList) {
            Unit unit = unitWithLessons.getUnit();

            View headerView = inflater.inflate(R.layout.item_unit_header_line, lessonContainer, false);
            TextView tvUnitName = headerView.findViewById(R.id.tvUnitName);
            tvUnitName.setText(String.format("%d. %s", unit.getOrderIndex(), unit.getUnitName()));
            lessonContainer.addView(headerView);

            List<Lesson> lessons = unitWithLessons.getLessons();
            for (int i = 0; i < lessons.size(); i++) {
                Lesson lesson = lessons.get(i);

                // Nếu là lesson thứ 3 (index 2), thêm mascot và lesson vào cùng 1 hàng
                if (i == 2) {
                    View rowView = createLessonWithMascotRow(inflater, unit, lesson, lessonIndex);
                    lessonContainer.addView(rowView);
                } else {
                    View lessonView = createLessonView(inflater, unit, lesson, lessonIndex);
                    lessonContainer.addView(lessonView);
                }
                lessonIndex++;
            }
        }
    }

    private View createLessonWithMascotRow(LayoutInflater inflater, Unit unit, Lesson lesson, int index) {
        // Tạo LinearLayout ngang để chứa lesson và mascot
        LinearLayout rowLayout = new LinearLayout(getContext());
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rowParams.topMargin = 20;
        rowParams.bottomMargin = 20;
        rowLayout.setLayoutParams(rowParams);

        // Tạo lesson view
        View lessonView = inflater.inflate(R.layout.item_lesson_circle, null, false);
        FrameLayout lessonCard = lessonView.findViewById(R.id.lessonCard);
        ImageView iconView = lessonView.findViewById(R.id.iconView);

        if (lessonCard != null && iconView != null) {
            boolean isLocked = lesson.isUnlockRequired() || unit.isLocked();

            if (isLocked) {
                lessonCard.setBackgroundResource(R.drawable.bg_circle_locked);
                iconView.setImageResource(R.drawable.ic_lock);
                iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_locked));
                lessonCard.setAlpha(0.7f);
            } else {
                if (lesson.getOrderIndex() <= 1) {
                    lessonCard.setBackgroundResource(R.drawable.bg_gradient_yellow);
                    iconView.setImageResource(R.drawable.ic_check);
                } else {
                    lessonCard.setBackgroundResource(R.drawable.bg_gradient_purple);
                    iconView.setImageResource(R.drawable.ic_star);
                }
                iconView.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white));
                lessonCard.setAlpha(1.0f);
            }

            lessonCard.setOnClickListener(v -> {
                if (isLocked)
                    Toast.makeText(getContext(), "Lesson is locked!", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), LessonActivity.class);
                    intent.putExtra("LESSON_ID", lesson.getLessonId());
                    intent.putExtra("CATEGORY_NAME", lesson.getLessonName());
                    startActivity(intent);
                }
            });
        }

        // Thêm lesson vào row với margin nhỏ bên phải
        LinearLayout.LayoutParams lessonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lessonParams.rightMargin = (int) (getResources().getDisplayMetrics().density * 20);
        lessonView.setLayoutParams(lessonParams);
        rowLayout.addView(lessonView);

        ImageView mascotView = new ImageView(getContext());
        mascotView.setImageResource(R.drawable.mascot);

        LinearLayout.LayoutParams mascotParams = new LinearLayout.LayoutParams(
                (int) (getResources().getDisplayMetrics().density * 80),
                (int) (getResources().getDisplayMetrics().density * 80)
        );
        mascotView.setLayoutParams(mascotParams);
        mascotView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        rowLayout.addView(mascotView);

        return rowLayout;
    }

    private View createLessonView(LayoutInflater inflater, Unit unit, Lesson lesson, int index) {

        View lessonView = inflater.inflate(R.layout.item_lesson_circle, null, false);
        FrameLayout lessonCard = lessonView.findViewById(R.id.lessonCard);
        ImageView iconView = lessonView.findViewById(R.id.iconView);

        if (lessonCard == null || iconView == null) {
            return new View(getContext());
        }

        boolean isLocked = lesson.isUnlockRequired() || unit.isLocked();

        // Áp dụng Style và Icon
        if (isLocked) {
            lessonCard.setBackgroundResource(R.drawable.bg_circle_locked);
            iconView.setImageResource(R.drawable.ic_lock);
            iconView.setColorFilter(ContextCompat.getColor(getContext(), R.color.icon_locked));
            lessonCard.setAlpha(0.7f);
        } else {
            if (lesson.getOrderIndex() <= 1) {
                lessonCard.setBackgroundResource(R.drawable.bg_gradient_yellow);
                iconView.setImageResource(R.drawable.ic_check);
            } else {
                lessonCard.setBackgroundResource(R.drawable.bg_gradient_purple);
                iconView.setImageResource(R.drawable.ic_star);
            }
            iconView.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.white));
            lessonCard.setAlpha(1.0f);
        }

        lessonCard.setOnClickListener(v -> {
            if (isLocked)
                Toast.makeText(getContext(), "Lesson is locked!", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(getActivity(), LessonActivity.class);
                intent.putExtra("LESSON_ID", lesson.getLessonId());
                intent.putExtra("CATEGORY_NAME", lesson.getLessonName());
                startActivity(intent);
            }
        });

        // Điều chỉnh layout params - các lesson gần center với độ nghiêng nhẹ
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 20;
        params.bottomMargin = 20;

        // Sử dụng margin nhỏ và đều để lesson gần center
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
        lessonView.setLayoutParams(params);
        return lessonView;
    }
}
