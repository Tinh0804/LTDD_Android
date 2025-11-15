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

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.UIModel.StudyTimeOption;

import java.util.List;

public class StudyTimeAdapter extends ArrayAdapter<StudyTimeOption> {

    private int selectedPosition = -1;

    public StudyTimeAdapter(@NonNull Context context, @NonNull List<StudyTimeOption> options) {
        super(context, 0, options);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_study_time, parent, false);
        }

        StudyTimeOption option = getItem(position);

        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvSubtitle = view.findViewById(R.id.tvSubtitle);
        ImageView ivCheck = view.findViewById(R.id.ivCheck);

        if(option != null){
            ivIcon.setImageResource(option.getIconResId());
            tvTitle.setText(option.getTitle());
            tvSubtitle.setText(option.getSubtitle());
        }

        // Highlight selected
        if(position == selectedPosition){
            ivCheck.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.parseColor("#D0CFFF"));
        } else {
            ivCheck.setVisibility(View.GONE);
            view.setBackgroundColor(Color.WHITE);
        }

        view.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return view;
    }

    public StudyTimeOption getSelectedOption(){
        if(selectedPosition != -1){
            return getItem(selectedPosition);
        }
        return null;
    }
}
