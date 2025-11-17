package com.example.learninglanguageapp.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.mapper.WordMapper;
import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.AppDatabase;
import com.example.learninglanguageapp.storage.DAOs.WordDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonRepository {

    private ApiService api;
    private WordDao wordDao;


    public LessonRepository(Context context) {
        api = ApiClient.getApiService();
        wordDao = AppDatabase.getInstance(context).wordDao();
    }

    public void getWords(int lessonId,
                         int userId,
                         MutableLiveData<List<Word>> wordsLiveData,
                         MutableLiveData<Boolean> loadingLiveData,
                         MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        // 1️⃣ Lấy offline trước
        new Thread(() -> {
            List<WordEntity> cached = wordDao.getWordsByLesson(lessonId);

            if (!cached.isEmpty()) {
                // Có dữ liệu local → dùng luôn
                List<Word> list = new ArrayList<>();
                for (WordEntity wordEntity:cached) {
                   list.add(WordMapper.toDomain(wordEntity));
                }
                wordsLiveData.postValue(list);
                loadingLiveData.postValue(false);
                return;
            }

            // 2️⃣ Không có → gọi API
            api.getWordsByLessonOfUser(lessonId, userId)
                    .enqueue(new Callback<ApiResponse<List<Word>>>() {

                        @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                        @Override
                        public void onResponse(Call<ApiResponse<List<Word>>> call,
                                               Response<ApiResponse<List<Word>>> response) {

                            loadingLiveData.setValue(false);

                            if (response.isSuccessful() && response.body() != null) {
                                List<Word> list = response.body().getData();

                                wordsLiveData.setValue(list);

                                // 3️⃣ Lưu xuống DB
                                new Thread(() -> {
                                    wordDao.insertWords(
                                            list.stream()
                                                    .map(WordMapper::toEntity)
                                                    .toList()
                                    );

                                }).start();

                            } else {
                                errorLiveData.setValue("API Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Word>>> call, Throwable t) {
                            loadingLiveData.setValue(false);
                            errorLiveData.setValue(t.getMessage());
                        }
                    });
        }).start();
    }
//    public void getWords(int lessonId,int userId,
//                         MutableLiveData<List<Word>> wordsLiveData,
//                         MutableLiveData<Boolean> loadingLiveData,
//                         MutableLiveData<String> errorLiveData) {
//
//        loadingLiveData.setValue(true);
//
//        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
//            loadingLiveData.setValue(false);
//
//            try {
//                List<Word> fakeWords = new ArrayList<>();
//
//                String[] words = {
//                        "apple","banana","cat","dog","elephant","fish","grape","house","ice","juice",
//                        "kite","lion","monkey","nest","orange","penguin","queen","rabbit","sun","tree",
//                        "umbrella","violet","wolf","xylophone","yacht","zebra","car","book","chair","table"
//                };
//
//                String[] translations = {
//                        "táo","chuối","mèo","chó","voi","cá","nho","nhà","đá","nước ép",
//                        "diều","sư tử","khỉ","tổ","cam","chim cánh cụt","nữ hoàng","thỏ","mặt trời","cây",
//                        "ô","tím","sói","đàn xylophone","du thuyền","ngựa vằn","xe hơi","sách","ghế","bàn"
//                };
//
//                String[] pronunciations = {
//                        "ˈæp.əl","bəˈnæn.ə","kæt","dɑːg","ˈel.ɪ.fənt","fɪʃ","ɡreɪp","haʊs","aɪs","dʒuːs",
//                        "kaɪt","laɪ.ən","ˈmʌŋ.ki","nest","ˈɒr.ɪndʒ","ˈpɛŋ.ɡwɪn","kwiːn","ˈræb.ɪt","sʌn","triː",
//                        "ʌmˈbrɛl.ə","ˈvaɪ.ə.lət","wʊlf","zaɪˈlə.foʊn","jɑːt","ˈziː.brə","kɑːr","bʊk","tʃer","teɪbəl"
//                };
//
//                String[] wordTypes = new String[30];
//                for (int i = 0; i < 30; i++) wordTypes[i] = "noun";
//
//                String[] exampleSentences = {
//                        "I eat an apple every day.","Bananas are yellow.","The cat is sleeping.","The dog barks loudly.",
//                        "The elephant is big.","I like fish.","Grapes are sweet.","My house is blue.","Ice melts fast.","I drink juice.",
//                        "The kite flies high.","The lion roars.","The monkey is climbing.","The nest has eggs.","I like orange.","The penguin swims.","The queen is kind.","The rabbit is fast.","The sun is hot.","The tree is tall.",
//                        "I need an umbrella.","Violet is a color.","The wolf howls.","He plays the xylophone.","The yacht is sailing.","The zebra has stripes.","My car is red.","I read a book.","Sit on the chair.","The table is round."
//                };
//
//                for (int i = 0; i < 30; i++) {
//                    Word w = new Word();
//                    w.setWordId(i + 1);
//                    w.setLanguageId(1);
//                    w.setLessonId(1);
//                    w.setWord(words[i]);
//                    w.setTranslation(translations[i]);
//                    w.setPronunciation(pronunciations[i]);
//                    w.setWordType(wordTypes[i]);
//                    w.setAudioFile("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-" + ((i % 10) + 1) + ".mp3");
//                    w.setExampleSentence(exampleSentences[i]);
//                    w.setImageUrl("https://via.placeholder.com/150?text=" + words[i]);
//
//                    fakeWords.add(w);
//                }
//
//                wordsLiveData.setValue(fakeWords);
//
//            } catch (Exception e) {
//                errorLiveData.setValue("Failed to generate fake data: " + e.getMessage());
//            }
//
//        }, 1000); // delay 1 giây
//    }
}
