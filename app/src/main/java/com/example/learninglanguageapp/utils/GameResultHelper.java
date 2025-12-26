package com.example.learninglanguageapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Response.ExerciseSubmitResponse;

public class GameResultHelper {
    public static void showResultDialog(Context context, ExerciseSubmitResponse data, Runnable onHomeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_game_result, null);

        TextView tvXp = view.findViewById(R.id.tvXpEarned);
        TextView tvMsg = view.findViewById(R.id.tvMessage);
        Button btnHome = view.findViewById(R.id.btnHome);

        tvXp.setText("+" + data.lessonExperienceReward + " XP");
        tvMsg.setText(data.isUnlocked ? "Chúc mừng! Bạn đã mở khóa bài học mới." : "Cố gắng hơn ở bài sau nhé!");

        AlertDialog dialog = builder.setView(view).setCancelable(false).create();

        btnHome.setOnClickListener(v -> {
            dialog.dismiss();
            onHomeClick.run();
        });

        dialog.show();
    }
}