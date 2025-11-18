package com.example.learninglanguageapp.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {

    private GridLayout calendarGrid;
    private TextView tvMonthYear, tvDateRange;
    private Calendar calendar;
    private Button btnInsights, btnAchievements;
    private List<Integer> learningDays = Arrays.asList(1, 2, 3, 4, 5, 7, 10, 11, 13, 14, 16, 18, 20, 21, 22, 23);

    // Chart data
    private int[] weeklyTimeData = {45, 32, 95, 28, 75, 60, 70};
    private String[] weekDays = {"16", "17", "18", "19", "20", "21", "22"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        calendar = Calendar.getInstance();

        // Initialize views
        calendarGrid = findViewById(R.id.calendarGrid);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvDateRange = findViewById(R.id.tvDateRange);
        btnInsights = findViewById(R.id.btnInsights);
        btnAchievements = findViewById(R.id.btnAchievements);

        ImageButton btnPrevMonth = findViewById(R.id.btnPrevMonth);
        ImageButton btnNextMonth = findViewById(R.id.btnNextMonth);

        // Set up tabs
        setupMainTabs();

        // Set up calendar
        updateCalendar();

        // Set up chart
        setupBarChart();

        // Month navigation
        btnPrevMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        btnNextMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // Tab buttons
        setupTabButtons();
    }

    private void setupMainTabs() {
        btnInsights.setOnClickListener(v -> {
            btnInsights.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4F46E5")));
            btnInsights.setTextColor(Color.WHITE);
            btnAchievements.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            btnAchievements.setTextColor(Color.parseColor("#1F2937"));
        });

        btnAchievements.setOnClickListener(v -> {
            btnAchievements.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4F46E5")));
            btnAchievements.setTextColor(Color.WHITE);
            btnInsights.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            btnInsights.setTextColor(Color.parseColor("#1F2937"));
        });
    }

    private void setupBarChart() {
        LinearLayout chartContainer = findViewById(R.id.chartContainer);
        chartContainer.removeAllViews();

        int maxValue = 100; // Max chart value
        int chartHeight = 240; // Height in dp

        for (int i = 0; i < weeklyTimeData.length; i++) {
            LinearLayout barWrapper = new LinearLayout(this);
            barWrapper.setOrientation(LinearLayout.VERTICAL);
            barWrapper.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            );
            wrapperParams.setMargins(4, 0, 4, 0);
            barWrapper.setLayoutParams(wrapperParams);

            // Create highlight badge for day 18 (index 2)
            if (i == 2) {
                TextView badge = new TextView(this);
                badge.setText(String.valueOf(weeklyTimeData[i]));
                badge.setTextSize(14);
                badge.setTextColor(Color.parseColor("#4F46E5"));
                badge.setGravity(Gravity.CENTER);

                GradientDrawable badgeBg = new GradientDrawable();
                badgeBg.setShape(GradientDrawable.OVAL);
                badgeBg.setColor(Color.WHITE);
                badgeBg.setStroke(4, Color.parseColor("#4F46E5"));
                badge.setBackground(badgeBg);

                LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                        dpToPx(48),
                        dpToPx(48)
                );
                badgeParams.setMargins(0, 0, 0, 8);
                badge.setLayoutParams(badgeParams);
                barWrapper.addView(badge);
            }

            // Create bar
            LinearLayout bar = new LinearLayout(this);
            int barHeight = (int) ((weeklyTimeData[i] / (float) maxValue) * dpToPx(chartHeight));

            LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    barHeight
            );
            bar.setLayoutParams(barParams);

            GradientDrawable barBg = new GradientDrawable();
            barBg.setCornerRadii(new float[]{16, 16, 16, 16, 0, 0, 0, 0});
            barBg.setColor(i == 2 ? Color.parseColor("#4F46E5") : Color.parseColor("#C7D2FE"));
            bar.setBackground(barBg);

            barWrapper.addView(bar);

            // Add day label
            TextView dayLabel = new TextView(this);
            dayLabel.setText(weekDays[i]);
            dayLabel.setTextSize(10);
            dayLabel.setTextColor(Color.parseColor("#9CA3AF"));
            dayLabel.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams dayParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dayParams.setMargins(0, 8, 0, 0);
            dayLabel.setLayoutParams(dayParams);
            barWrapper.addView(dayLabel);

            chartContainer.addView(barWrapper);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void updateCalendar() {
        calendarGrid.removeAllViews();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonthYear.setText(sdf.format(calendar.getTime()));

        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDayOfWeek < 0) firstDayOfWeek = 6;

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            addDayCell(0, false);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            boolean isLearningDay = learningDays.contains(day);
            addDayCell(day, isLearningDay);
        }
    }

    private void addDayCell(int day, boolean isLearningDay) {
        TextView dayView = new TextView(this);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        dayView.setLayoutParams(params);

        dayView.setText(day > 0 ? String.valueOf(day) : "");
        dayView.setGravity(Gravity.CENTER);
        dayView.setPadding(0, dpToPx(12), 0, dpToPx(12));
        dayView.setTextSize(14);

        if (isLearningDay) {
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            bg.setColor(Color.parseColor("#4F46E5"));
            dayView.setBackground(bg);
            dayView.setTextColor(Color.WHITE);
        } else if (day > 0) {
            dayView.setTextColor(Color.parseColor("#374151"));
        } else {
            dayView.setTextColor(Color.parseColor("#D1D5DB"));
        }

        calendarGrid.addView(dayView);
    }

    private void setupTabButtons() {
        // History tabs
        Button btnWeekly = findViewById(R.id.btnWeeklyHistory);
        Button btnMonthly = findViewById(R.id.btnMonthlyHistory);
        Button btnYearly = findViewById(R.id.btnYearlyHistory);

        // Similar setup for time tabs...
    }
}