package com.example.learninglanguageapp.storage.DAOs;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.learninglanguageapp.models.Entities.WordEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class WordDao_Impl implements WordDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<WordEntity> __insertAdapterOfWordEntity;

  public WordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfWordEntity = new EntityInsertAdapter<WordEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `words` (`wordId`,`languageId`,`lessonId`,`wordName`,`translation`,`pronunciation`,`wordType`,`audioFile`,`exampleSentence`,`imageUrl`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final WordEntity entity) {
        statement.bindLong(1, entity.wordId);
        statement.bindLong(2, entity.languageId);
        statement.bindLong(3, entity.lessonId);
        if (entity.wordName == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.wordName);
        }
        if (entity.translation == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.translation);
        }
        if (entity.pronunciation == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.pronunciation);
        }
        if (entity.wordType == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.wordType);
        }
        if (entity.audioFile == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.audioFile);
        }
        if (entity.exampleSentence == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.exampleSentence);
        }
        if (entity.imageUrl == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.imageUrl);
        }
      }
    };
  }

  @Override
  public void insertWords(final List<WordEntity> words) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfWordEntity.insert(_connection, words);
      return null;
    });
  }

  @Override
  public List<WordEntity> getWordsByLesson(final int lessonId) {
    final String _sql = "SELECT * FROM words WHERE lessonId = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lessonId);
        final int _columnIndexOfWordId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wordId");
        final int _columnIndexOfLanguageId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "languageId");
        final int _columnIndexOfLessonId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lessonId");
        final int _columnIndexOfWordName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wordName");
        final int _columnIndexOfTranslation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translation");
        final int _columnIndexOfPronunciation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "pronunciation");
        final int _columnIndexOfWordType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wordType");
        final int _columnIndexOfAudioFile = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audioFile");
        final int _columnIndexOfExampleSentence = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exampleSentence");
        final int _columnIndexOfImageUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "imageUrl");
        final List<WordEntity> _result = new ArrayList<WordEntity>();
        while (_stmt.step()) {
          final WordEntity _item;
          _item = new WordEntity();
          _item.wordId = (int) (_stmt.getLong(_columnIndexOfWordId));
          _item.languageId = (int) (_stmt.getLong(_columnIndexOfLanguageId));
          _item.lessonId = (int) (_stmt.getLong(_columnIndexOfLessonId));
          if (_stmt.isNull(_columnIndexOfWordName)) {
            _item.wordName = null;
          } else {
            _item.wordName = _stmt.getText(_columnIndexOfWordName);
          }
          if (_stmt.isNull(_columnIndexOfTranslation)) {
            _item.translation = null;
          } else {
            _item.translation = _stmt.getText(_columnIndexOfTranslation);
          }
          if (_stmt.isNull(_columnIndexOfPronunciation)) {
            _item.pronunciation = null;
          } else {
            _item.pronunciation = _stmt.getText(_columnIndexOfPronunciation);
          }
          if (_stmt.isNull(_columnIndexOfWordType)) {
            _item.wordType = null;
          } else {
            _item.wordType = _stmt.getText(_columnIndexOfWordType);
          }
          if (_stmt.isNull(_columnIndexOfAudioFile)) {
            _item.audioFile = null;
          } else {
            _item.audioFile = _stmt.getText(_columnIndexOfAudioFile);
          }
          if (_stmt.isNull(_columnIndexOfExampleSentence)) {
            _item.exampleSentence = null;
          } else {
            _item.exampleSentence = _stmt.getText(_columnIndexOfExampleSentence);
          }
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _item.imageUrl = null;
          } else {
            _item.imageUrl = _stmt.getText(_columnIndexOfImageUrl);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteWordsByLesson(final int lessonId) {
    final String _sql = "DELETE FROM words WHERE lessonId = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lessonId);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
