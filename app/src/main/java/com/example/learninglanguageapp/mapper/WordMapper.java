package com.example.learninglanguageapp.mapper;

import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.models.Word;

public class WordMapper {

    public static WordEntity toEntity(Word dto) {
        WordEntity e = new WordEntity();
        e.wordId = dto.getWordId();
        e.wordName = dto.getWord();
        e.translation = dto.getTranslation();
        e.imageUrl = dto.getImageUrl();
        e.audioFile = dto.getAudioFile();
        e.exampleSentence = dto.getExampleSentence();
        e.lessonId = dto.getLessonId();
        e.languageId = dto.getLanguageId();
        e.wordType = dto.getWordType();
        e.pronunciation = dto.getPronunciation();
        return e;
    }

    public static Word toDomain(WordEntity entity) {
        Word w = new Word();
        w.setWordId(entity.wordId);
        w.setWord(entity.wordName);
        w.setTranslation(entity.translation);
        w.setImageUrl(entity.imageUrl);
        w.setAudioFile(entity.audioFile);
        w.setExampleSentence(entity.exampleSentence);
        w.setLessonId(entity.lessonId);
        w.setLanguageId(entity.languageId);
        w.setWordType(entity.wordType);
        w.setPronunciation(entity.pronunciation);
        return w;
    }
}
