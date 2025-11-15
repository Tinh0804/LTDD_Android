package com.example.learninglanguageapp.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.LanguageAdapter;
import com.example.learninglanguageapp.models.UIModel.Language;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingInflatedId")
public class LanguageLearn extends AppCompatActivity {
    private LanguageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_learn);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        ListView listView = findViewById(R.id.listViewLearn);

        // 🔙 Quay lại NameRegisterActivity
        btnBack.setOnClickListener(v -> finish());
        // Danh sách ngôn ngữ
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("Vietnamese", R.drawable.flagvietnam,2));
        languages.add(new Language("English", R.drawable.flagenglish,1));
        languages.add(new Language("Canada", R.drawable.flagcanada,3));
        languages.add(new Language("Germany", R.drawable.flaggermany,4));
        languages.add(new Language("Spanish", R.drawable.flagspainsh,5));
        languages.add(new Language("French", R.drawable.flagfrench,6));

        adapter = new LanguageAdapter(this, languages);
        listView.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            Language selected = adapter.getSelectedLanguage();
            if (selected != null) {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("data");
                if (bundle != null)
                    bundle.putString("language_learn_id", selected.getLanguageId()+"");

                intent.putExtra("data", bundle);
                startActivity(intent);

            } else {
                Toast.makeText(LanguageLearn.this, "Please select a language", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
