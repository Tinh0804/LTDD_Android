package com.example.learninglanguageapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private List<Lesson> lessons;
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonItemClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.tvLessonName.setText(lesson.getLessonName());
        holder.tvLessonReward.setText("🏆 Thưởng: " + lesson.getExperienceReward() + " XP");
        holder.itemView.setOnClickListener(v -> listener.onLessonItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessons == null ? 0 : lessons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonName, tvLessonReward;

        ViewHolder(View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvLessonReward = itemView.findViewById(R.id.tvLessonReward);
        }
    }
}