package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class LanguageUse extends AppCompatActivity {

    MaterialCardView cardVietnamese, cardEnglish, cardCanada, cardGermany, cardSpanish, cardFrench;
    String selectedCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_use);

        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialButton btnContinue = findViewById(R.id.btnContinue);

        // Ánh xạ
        cardVietnamese = findViewById(R.id.cardVietnamese);
        cardEnglish     = findViewById(R.id.cardEnglish);
        cardCanada      = findViewById(R.id.cardCanada);
        cardGermany     = findViewById(R.id.cardGermany);
        cardSpanish     = findViewById(R.id.cardSpanish);
        cardFrench      = findViewById(R.id.cardFrench);

        // Back → quay lại Welcome2
        btnBack.setOnClickListener(v -> finish());

        // Click events
        cardVietnamese.setOnClickListener(v -> select("Viet Nam", cardVietnamese));
        cardEnglish.setOnClickListener(v -> select("English", cardEnglish));
        cardCanada.setOnClickListener(v -> select("Canada", cardCanada));
        cardGermany.setOnClickListener(v -> select("Germany", cardGermany));
        cardSpanish.setOnClickListener(v -> select("Spanish", cardSpanish));
        cardFrench.setOnClickListener(v -> select("French", cardFrench));

        // Continue →
        btnContinue.setOnClickListener(v -> {
            if (selectedCountry.isEmpty()) {
                selectedCountry = "English";
            }

            getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    .edit()
                    .putString("country", selectedCountry)
                    .putBoolean("finished_onboarding", true)
                    .apply();

            // Xóa onboarding khỏi back stack
            Intent intent = new Intent(LanguageUse.this, AccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Hàm xử lý chọn language
    private void select(String country, MaterialCardView selectedCard) {
        selectedCountry = country;
        highlight(selectedCard);
    }

    // Highlight card đã chọn
    private void highlight(MaterialCardView selected) {
        MaterialCardView[] all = {
                cardVietnamese, cardEnglish, cardCanada,
                cardGermany, cardSpanish, cardFrench
        };

        // Reset
        for (MaterialCardView c : all) {
            c.setStrokeColor(getColor(android.R.color.transparent));
            c.setStrokeWidth(0);
            c.setCardElevation(4);
        }

        // Highlight card được chọn
        selected.setStrokeColor(getColor(R.color.purple_200)); // viền tím
        selected.setStrokeWidth(6);
        selected.setCardElevation(12); // bóng nổi hơn
    }
}
