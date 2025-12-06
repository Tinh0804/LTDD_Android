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
import com.example.learninglanguageapp.adapters.OptionAdapter;
import com.example.learninglanguageapp.models.UIModel.Option;

import java.util.ArrayList;
import java.util.List;

public class AimActivity extends AppCompatActivity {

    private OptionAdapter adapter;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout4_12); // layout chính

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        ListView listView = findViewById(R.id.lvOptions);

        btnBack.setOnClickListener(v -> finish());

        List<Option> options = new ArrayList<>();
        options.add(new Option("Travel abroad", R.drawable.plane));
        options.add(new Option("Boost my job / career", R.drawable.bag));
        options.add(new Option("Support for education", R.drawable.book));
        options.add(new Option("Discover a new culture", R.drawable.mother));
        options.add(new Option("Just for fun", R.drawable.fun));

        adapter = new OptionAdapter(this, options);
        listView.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            Option selected = adapter.getSelectedOption();
            if (selected != null) {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("data");

                if (bundle != null)
                    bundle.putString("reason", selected.getTitle());
                intent.putExtra("data",bundle);
                startActivity(intent);
            } else {
                Toast.makeText(AimActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
