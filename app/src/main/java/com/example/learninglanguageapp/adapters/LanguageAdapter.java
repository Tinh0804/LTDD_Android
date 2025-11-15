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
import com.example.learninglanguageapp.models.UIModel.Language;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<Language> {

    private int selectedPosition = -1;

    public LanguageAdapter(@NonNull Context context, @NonNull List<Language> languages) {
        super(context, 0, languages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_language, parent, false);
        }

        Language language = getItem(position);

        ImageView imgFlag = view.findViewById(R.id.imgFlag);
        TextView tvLanguage = view.findViewById(R.id.tvLanguage);

        if (language != null) {
            imgFlag.setImageResource(language.getFlagResId());
            tvLanguage.setText(language.getName());
        }

        // highlight item được chọn
        view.setBackgroundColor(position == selectedPosition ? Color.parseColor("#D0CFFF") : Color.WHITE);

        view.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return view;
    }

    public Language getSelectedLanguage() {
        if (selectedPosition != -1) {
            return getItem(selectedPosition);
        }
        return null;
    }
}
