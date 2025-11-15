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
import com.example.learninglanguageapp.models.UIModel.Option;

import java.util.List;

public class OptionAdapter extends ArrayAdapter<Option> {

    private int selectedPosition = -1;

    public OptionAdapter(@NonNull Context context, @NonNull List<Option> options) {
        super(context, 0, options);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_option, parent, false);
        }

        Option option = getItem(position);

        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvTitle = view.findViewById(R.id.tvTitle);

        if (option != null) {
            ivIcon.setImageResource(option.getIconResId());
            tvTitle.setText(option.getTitle());
        }

        // Highlight khi chọn
        view.setBackgroundColor(position == selectedPosition ? Color.parseColor("#D0CFFF") : Color.WHITE);

        view.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return view;
    }

    public Option getSelectedOption() {
        if (selectedPosition != -1) {
            return getItem(selectedPosition);
        }
        return null;
    }
}
