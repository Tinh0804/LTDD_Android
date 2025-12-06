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
import com.example.learninglanguageapp.models.UIModel.Level;

import java.util.List;

public class LevelAdapter extends ArrayAdapter<Level> {

    private int selectedPosition = -1;

    public LevelAdapter(@NonNull Context context, @NonNull List<Level> levels) {
        super(context, 0, levels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.level_item, parent, false);
        }

        Level level = getItem(position);

        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvName = view.findViewById(R.id.tvLevelName);
        TextView tvDesc = view.findViewById(R.id.tvLevelDesc);

        if (level != null) {
            ivIcon.setImageResource(level.getIcon());
            tvName.setText(level.getName());
            tvDesc.setText(level.getDesc());
        }

        // highlight item được chọn
        view.setBackgroundColor(position == selectedPosition ? Color.parseColor("#D0CFFF") : Color.WHITE);

        view.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return view;
    }

    public Level getSelectedLevel() {
        if (selectedPosition != -1) {
            return getItem(selectedPosition);
        }
        return null;
    }
}
