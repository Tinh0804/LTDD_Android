package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.LevelAdapter;
import com.example.learninglanguageapp.models.UIModel.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelActivity extends AppCompatActivity {

    private LevelAdapter adapter;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level); // layout riêng cho LevelActivity, có ListView với id lvLevels

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        ListView lvLevels = findViewById(R.id.lvLevels);

        btnBack.setOnClickListener(v -> finish());

        // Danh sách level
        List<Level> levels = new ArrayList<>();
        levels.add(new Level("Beginner", "I know some basics", R.drawable.beginer,1));
        levels.add(new Level("Elementary", "I know some basics", R.drawable.element,2));
        levels.add(new Level("Intermediate", "I know some basics", R.drawable.star,3));
        levels.add(new Level("Advanced", "I know some basics", R.drawable.advance,4));

        adapter = new LevelAdapter(this, levels);
        lvLevels.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            Level selectedLevel = adapter.getSelectedLevel();
            if (selectedLevel != null) {
                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("data");

                if (bundle != null)
                    bundle.putString("level_id", selectedLevel.getName());
                intent.putExtra("data", selectedLevel.getName());
                startActivity(intent);
            } else {
                Toast.makeText(LevelActivity.this, "Please select a level", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class HomeFragment extends Fragment {

        public HomeFragment() {
            // Required empty public constructor
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_home, container, false);
        }
    }
}
