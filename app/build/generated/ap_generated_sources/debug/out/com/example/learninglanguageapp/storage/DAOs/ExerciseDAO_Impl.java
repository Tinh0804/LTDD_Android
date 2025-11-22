package com.example.learninglanguageapp.storage.DAOs;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.utils.Converters;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExerciseDAO_Impl implements ExerciseDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExerciseEntity> __insertAdapterOfExerciseEntity;

  private final Converters __converters = new Converters();

  public ExerciseDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExerciseEntity = new EntityInsertAdapter<ExerciseEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`exerciseId`,`lessonId`,`unitId`,`orderIndex`,`exerciseType`,`question`,`audioFile`,`correctAnswer`,`experienceReward`,`options`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExerciseEntity entity) {
        statement.bindLong(1, entity.exerciseId);
        statement.bindLong(2, entity.lessonId);
        statement.bindLong(3, entity.unitId);
        statement.bindLong(4, entity.orderIndex);
        if (entity.exerciseType == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.exerciseType);
        }
        if (entity.question == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.question);
        }
        if (entity.audioFile == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.audioFile);
        }
        if (entity.correctAnswer == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.correctAnswer);
        }
        statement.bindLong(9, entity.experienceReward);
        final String _tmp = __converters.fromList(entity.options);
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, _tmp);
        }
      }
    };
  }

  @Override
  public void insertAll(final List<ExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExerciseEntity.insert(_connection, exercises);
      return null;
    });
  }

  @Override
  public void insertExercises(final List<ExerciseEntity> exercises) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExerciseEntity.insert(_connection, exercises);
      return null;
    });
  }

  @Override
  public List<ExerciseEntity> getExercisesByLessonAndType(final int lessonId, final String type) {
    final String _sql = "SELECT * FROM exercises WHERE lessonId = ? AND exerciseType = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lessonId);
        _argIndex = 2;
        if (type == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, type);
        }
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfLessonId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lessonId");
        final int _columnIndexOfUnitId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unitId");
        final int _columnIndexOfOrderIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orderIndex");
        final int _columnIndexOfExerciseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseType");
        final int _columnIndexOfQuestion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "question");
        final int _columnIndexOfAudioFile = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audioFile");
        final int _columnIndexOfCorrectAnswer = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctAnswer");
        final int _columnIndexOfExperienceReward = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "experienceReward");
        final int _columnIndexOfOptions = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "options");
        final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>();
        while (_stmt.step()) {
          final ExerciseEntity _item;
          _item = new ExerciseEntity();
          _item.exerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item.lessonId = (int) (_stmt.getLong(_columnIndexOfLessonId));
          _item.unitId = (int) (_stmt.getLong(_columnIndexOfUnitId));
          _item.orderIndex = (int) (_stmt.getLong(_columnIndexOfOrderIndex));
          if (_stmt.isNull(_columnIndexOfExerciseType)) {
            _item.exerciseType = null;
          } else {
            _item.exerciseType = _stmt.getText(_columnIndexOfExerciseType);
          }
          if (_stmt.isNull(_columnIndexOfQuestion)) {
            _item.question = null;
          } else {
            _item.question = _stmt.getText(_columnIndexOfQuestion);
          }
          if (_stmt.isNull(_columnIndexOfAudioFile)) {
            _item.audioFile = null;
          } else {
            _item.audioFile = _stmt.getText(_columnIndexOfAudioFile);
          }
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _item.correctAnswer = null;
          } else {
            _item.correctAnswer = _stmt.getText(_columnIndexOfCorrectAnswer);
          }
          _item.experienceReward = (int) (_stmt.getLong(_columnIndexOfExperienceReward));
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfOptions)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfOptions);
          }
          _item.options = __converters.toList(_tmp);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExerciseEntity> getExercisesByUnitId(final int unitId) {
    final String _sql = "SELECT * FROM exercises WHERE unitId = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, unitId);
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfLessonId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lessonId");
        final int _columnIndexOfUnitId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unitId");
        final int _columnIndexOfOrderIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orderIndex");
        final int _columnIndexOfExerciseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseType");
        final int _columnIndexOfQuestion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "question");
        final int _columnIndexOfAudioFile = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audioFile");
        final int _columnIndexOfCorrectAnswer = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctAnswer");
        final int _columnIndexOfExperienceReward = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "experienceReward");
        final int _columnIndexOfOptions = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "options");
        final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>();
        while (_stmt.step()) {
          final ExerciseEntity _item;
          _item = new ExerciseEntity();
          _item.exerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item.lessonId = (int) (_stmt.getLong(_columnIndexOfLessonId));
          _item.unitId = (int) (_stmt.getLong(_columnIndexOfUnitId));
          _item.orderIndex = (int) (_stmt.getLong(_columnIndexOfOrderIndex));
          if (_stmt.isNull(_columnIndexOfExerciseType)) {
            _item.exerciseType = null;
          } else {
            _item.exerciseType = _stmt.getText(_columnIndexOfExerciseType);
          }
          if (_stmt.isNull(_columnIndexOfQuestion)) {
            _item.question = null;
          } else {
            _item.question = _stmt.getText(_columnIndexOfQuestion);
          }
          if (_stmt.isNull(_columnIndexOfAudioFile)) {
            _item.audioFile = null;
          } else {
            _item.audioFile = _stmt.getText(_columnIndexOfAudioFile);
          }
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _item.correctAnswer = null;
          } else {
            _item.correctAnswer = _stmt.getText(_columnIndexOfCorrectAnswer);
          }
          _item.experienceReward = (int) (_stmt.getLong(_columnIndexOfExperienceReward));
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfOptions)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfOptions);
          }
          _item.options = __converters.toList(_tmp);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExerciseEntity> getExercisesByLesson(final List<Integer> lessonId) {
    final StringBuilder _stringBuilder = new StringBuilder();
    _stringBuilder.append("SELECT * FROM exercises WHERE lessonId = ");
    final int _inputSize = lessonId == null ? 1 : lessonId.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    final String _sql = _stringBuilder.toString();
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (lessonId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          for (Integer _item : lessonId) {
            if (_item == null) {
              _stmt.bindNull(_argIndex);
            } else {
              _stmt.bindLong(_argIndex, _item);
            }
            _argIndex++;
          }
        }
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfLessonId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lessonId");
        final int _columnIndexOfUnitId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unitId");
        final int _columnIndexOfOrderIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orderIndex");
        final int _columnIndexOfExerciseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseType");
        final int _columnIndexOfQuestion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "question");
        final int _columnIndexOfAudioFile = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audioFile");
        final int _columnIndexOfCorrectAnswer = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctAnswer");
        final int _columnIndexOfExperienceReward = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "experienceReward");
        final int _columnIndexOfOptions = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "options");
        final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>();
        while (_stmt.step()) {
          final ExerciseEntity _item_1;
          _item_1 = new ExerciseEntity();
          _item_1.exerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _item_1.lessonId = (int) (_stmt.getLong(_columnIndexOfLessonId));
          _item_1.unitId = (int) (_stmt.getLong(_columnIndexOfUnitId));
          _item_1.orderIndex = (int) (_stmt.getLong(_columnIndexOfOrderIndex));
          if (_stmt.isNull(_columnIndexOfExerciseType)) {
            _item_1.exerciseType = null;
          } else {
            _item_1.exerciseType = _stmt.getText(_columnIndexOfExerciseType);
          }
          if (_stmt.isNull(_columnIndexOfQuestion)) {
            _item_1.question = null;
          } else {
            _item_1.question = _stmt.getText(_columnIndexOfQuestion);
          }
          if (_stmt.isNull(_columnIndexOfAudioFile)) {
            _item_1.audioFile = null;
          } else {
            _item_1.audioFile = _stmt.getText(_columnIndexOfAudioFile);
          }
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _item_1.correctAnswer = null;
          } else {
            _item_1.correctAnswer = _stmt.getText(_columnIndexOfCorrectAnswer);
          }
          _item_1.experienceReward = (int) (_stmt.getLong(_columnIndexOfExperienceReward));
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfOptions)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfOptions);
          }
          _item_1.options = __converters.toList(_tmp);
          _result.add(_item_1);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public ExerciseEntity getExercise(final int exerciseId) {
    final String _sql = "SELECT * FROM exercises WHERE exerciseId = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, exerciseId);
        final int _columnIndexOfExerciseId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseId");
        final int _columnIndexOfLessonId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lessonId");
        final int _columnIndexOfUnitId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "unitId");
        final int _columnIndexOfOrderIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "orderIndex");
        final int _columnIndexOfExerciseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "exerciseType");
        final int _columnIndexOfQuestion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "question");
        final int _columnIndexOfAudioFile = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audioFile");
        final int _columnIndexOfCorrectAnswer = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctAnswer");
        final int _columnIndexOfExperienceReward = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "experienceReward");
        final int _columnIndexOfOptions = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "options");
        final ExerciseEntity _result;
        if (_stmt.step()) {
          _result = new ExerciseEntity();
          _result.exerciseId = (int) (_stmt.getLong(_columnIndexOfExerciseId));
          _result.lessonId = (int) (_stmt.getLong(_columnIndexOfLessonId));
          _result.unitId = (int) (_stmt.getLong(_columnIndexOfUnitId));
          _result.orderIndex = (int) (_stmt.getLong(_columnIndexOfOrderIndex));
          if (_stmt.isNull(_columnIndexOfExerciseType)) {
            _result.exerciseType = null;
          } else {
            _result.exerciseType = _stmt.getText(_columnIndexOfExerciseType);
          }
          if (_stmt.isNull(_columnIndexOfQuestion)) {
            _result.question = null;
          } else {
            _result.question = _stmt.getText(_columnIndexOfQuestion);
          }
          if (_stmt.isNull(_columnIndexOfAudioFile)) {
            _result.audioFile = null;
          } else {
            _result.audioFile = _stmt.getText(_columnIndexOfAudioFile);
          }
          if (_stmt.isNull(_columnIndexOfCorrectAnswer)) {
            _result.correctAnswer = null;
          } else {
            _result.correctAnswer = _stmt.getText(_columnIndexOfCorrectAnswer);
          }
          _result.experienceReward = (int) (_stmt.getLong(_columnIndexOfExperienceReward));
          final String _tmp;
          if (_stmt.isNull(_columnIndexOfOptions)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getText(_columnIndexOfOptions);
          }
          _result.options = __converters.toList(_tmp);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void clear() {
    final String _sql = "DELETE FROM exercises";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
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
