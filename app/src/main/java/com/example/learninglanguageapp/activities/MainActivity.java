package com.example.learninglanguageapp.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.fragments.AccountFragment;
import com.example.learninglanguageapp.fragments.FriendFragment;
import com.example.learninglanguageapp.fragments.HomeFragment;
import com.example.learninglanguageapp.fragments.LeaderBoardFragment;
import com.example.learninglanguageapp.fragments.PracticeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView tvGems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvGems = findViewById(R.id.tvGems);
        tvGems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home)
                loadFragment(new HomeFragment());
            else if (id == R.id.nav_practice)
                loadFragment(new PracticeFragment());
            else if (id == R.id.nav_friend)
                loadFragment(new FriendFragment());
          else if (id == R.id.nav_reckon)
                loadFragment(new LeaderBoardFragment());

          else if (id == R.id.nav_account)
                loadFragment(new AccountFragment());

            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
