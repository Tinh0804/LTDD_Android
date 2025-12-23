package com.example.learninglanguageapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Course;

import java.util.List;

public class LevelAdapter extends ArrayAdapter<Course> {

    private int selectedPosition = -1;
    // URL base của server để ghép với path icon từ API
    private static final String BASE_URL = "http://192.168.1.73:5050";

    public LevelAdapter(@NonNull Context context, @NonNull List<Course> courses) {
        super(context, 0, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.level_item, parent, false);
        }

        Course course = getItem(position);

        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvName = view.findViewById(R.id.tvLevelName);
        TextView tvDesc = view.findViewById(R.id.tvLevelDesc);

        if (course != null) {
            // Hiển thị tên (ví dụ: Beginner)
            tvName.setText(course.getDifficultyLevel().toUpperCase());
            // Hiển thị mô tả đầy đủ
            tvDesc.setText(course.getCourseName());

            // Load ảnh từ API bằng Glide
            String imageUrl = BASE_URL + course.getCourseIcon();
            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.beginer) // Ảnh chờ khi đang tải
                    .error(R.drawable.beginer)       // Ảnh hiện ra nếu lỗi
                    .into(ivIcon);
        }

        // Highlight item được chọn
        view.setBackgroundColor(position == selectedPosition ? Color.parseColor("#D0CFFF") : Color.TRANSPARENT);

        view.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return view;
    }

    public Course getSelectedCourse() {
        if (selectedPosition != -1) {
            return getItem(selectedPosition);
        }
        return null;
    }
}