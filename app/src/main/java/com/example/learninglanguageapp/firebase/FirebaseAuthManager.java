package com.example.learninglanguageapp.firebase;

import android.content.Context;

import com.example.learninglanguageapp.activities.LoginActivity;
import com.example.learninglanguageapp.models.UserProfile;
import com.example.learninglanguageapp.storage.LocalStorageManager;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

// firebase/FirebaseAuthManager.java
public class FirebaseAuthManager {

    private static FirebaseAuthManager instance;
    private FirebaseAuth firebaseAuth;
    private Context context;

    private FirebaseAuthManager(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseAuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseAuthManager(context);
        }
        return instance;
    }


    public void signInWithEmailAndPassword(String email, String password,
                                           AuthResultListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Save user data locally
                            saveUserLocally(user);
                            listener.onSuccess(user);
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public void createUserWithEmailAndPassword(String email, String password,
                                               AuthResultListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            createUserProfile(user, listener);
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    private void createUserProfile(FirebaseUser user, AuthResultListener listener) {
        UserProfile newUser = new UserProfile();
        newUser.setId(user.getUid());
        newUser.setEmail(user.getEmail());
        newUser.setCreatedAt(System.currentTimeMillis());
        newUser.setLevel(1);
        newUser.setXp(0);

        FirestoreManager.getInstance(context).saveUser(newUser, task -> {
            if (task.isSuccessful()) {
                saveUserLocally(user);
                listener.onSuccess(user);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    private void saveUserLocally(FirebaseUser user) {
        SharedPrefsHelper prefs = new SharedPrefsHelper(context);
        prefs.saveString(Constants.PREF_USER_ID, user.getUid());
        prefs.saveString(Constants.PREF_USER_EMAIL, user.getEmail());
        prefs.saveLong(Constants.PREF_LAST_SYNC, System.currentTimeMillis());
    }

    public void signOut() {
        firebaseAuth.signOut();

        // Clear local data
        SharedPrefsHelper prefs = new SharedPrefsHelper(context);
        prefs.clearAll();

        LocalStorageManager.getInstance(context).clearAllData();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    public interface AuthResultListener {
        void onSuccess(FirebaseUser user);



        void onError(Exception exception);
    }
}

// firebase/FirestoreManager.java


// firebase/AnalyticsManager.java
