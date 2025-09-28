package com.example.learninglanguageapp.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.learninglanguageapp.activities.MainActivity;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.NotificationUtils;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Message received from: " + remoteMessage.getFrom());

        // Handle data payload
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleDataMessage(remoteMessage.getData());
        }

        // Handle notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
            );
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // Send token to server
        sendRegistrationToServer(token);

        // Store token locally
        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        prefsHelper.saveString("fcm_token", token);
    }

    private void handleDataMessage(Map<String, String> data) {
        String type = data.get("type");

        switch (type) {
            case "lesson_reminder":
//                showLessonReminder(data);
                break;
            case "streak_reminder":
                showStreakReminder(data);
                break;
            case "friend_challenge":
                showFriendChallenge(data);
                break;
            case "achievement_unlock":
//                showAchievementUnlock(data);
                break;
        }
    }

    private void showNotification(String title, String body) {
        NotificationUtils.showNotification(
                this,
                Constants.DAILY_REMINDER_ID,
                title,
                body,
                MainActivity.class
        );
    }



    private void showStreakReminder(Map<String, String> data) {
        NotificationUtils.showNotification(
                this,
                Constants.STREAK_REMINDER_ID,
                "Đừng để chuỗi ngày học bị gián đoạn! 🔥",
                "Học 1 bài ngay để duy trì chuỗi " + data.get("streak_count") + " ngày",
                MainActivity.class
        );
    }

    private void showFriendChallenge(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", "leaderboard");
        intent.putExtra("challenge_id", data.get("challenge_id"));

        NotificationUtils.showNotificationWithAction(
                this,
                1004,
                data.get("title"),
                data.get("body"),
                intent
        );
    }



    private void sendRegistrationToServer(String token) {
        // Send token to your server
        ApiService apiService = ApiClient.getInstance().getApiService();
        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        String userToken = prefsHelper.getString(Constants.PREF_USER_TOKEN, "");

        if (!userToken.isEmpty()) {
            // Implementation for sending FCM token to server
            Map<String, String> tokenData = new HashMap<>();
            tokenData.put("fcm_token", token);
            // apiService.updateFCMToken("Bearer " + userToken, tokenData);
        }
    }
}