package com.example.learninglanguageapp.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.example.learninglanguageapp.models.Progress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseDatabase realtimeDatabase;
    private FirebaseAnalytics analytics;

    private FirebaseManager(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        realtimeDatabase = FirebaseDatabase.getInstance();
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public static synchronized FirebaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseManager(context);
        }
        return instance;
    }

    // Authentication methods
    public void signInWithEmail(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void createUserWithEmail(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // Firestore methods
    public void saveUserData(String userId, @SuppressLint("RestrictedApi") User user, OnCompleteListener<Void> listener) {
        firestore.collection("users").document(userId).set(user).addOnCompleteListener(listener);
    }

    public void getUserData(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        firestore.collection("users").document(userId).get().addOnCompleteListener(listener);
    }

    public void saveProgress(String userId, Progress progress, OnCompleteListener<Void> listener) {
        firestore.collection("users").document(userId)
                .collection("progress").document().set(progress)
                .addOnCompleteListener(listener);
    }

    // Storage methods
    public void uploadProfileImage(String userId, Uri imageUri, OnCompleteListener<UploadTask.TaskSnapshot> listener) {
        StorageReference ref = storage.getReference().child("profile_images/" + userId + ".jpg");
        ref.putFile(imageUri).addOnCompleteListener(listener);
    }

    // Analytics methods
    public void logEvent(String eventName, Bundle parameters) {
        analytics.logEvent(eventName, parameters);
    }

    // Getters
    public FirebaseAuth getAuth() { return firebaseAuth; }
    public FirebaseFirestore getFirestore() { return firestore; }
    public FirebaseStorage getStorage() { return storage; }
    public FirebaseDatabase getRealtimeDatabase() { return realtimeDatabase; }
    public FirebaseAnalytics getAnalytics() { return analytics; }
}