package com.example.learninglanguageapp.storage;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.example.learninglanguageapp.storage.DAOs.ExerciseDAO;
import com.example.learninglanguageapp.storage.DAOs.ExerciseDAO_Impl;
import com.example.learninglanguageapp.storage.DAOs.WordDao;
import com.example.learninglanguageapp.storage.DAOs.WordDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile WordDao _wordDao;

  private volatile ExerciseDAO _exerciseDAO;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(2, "555ddb9d29f593a7d07485047b3a29a5", "b619791ec60ce513a1faf0e6dfd3d729") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `words` (`wordId` INTEGER NOT NULL, `languageId` INTEGER NOT NULL, `lessonId` INTEGER NOT NULL, `wordName` TEXT, `translation` TEXT, `pronunciation` TEXT, `wordType` TEXT, `audioFile` TEXT, `exampleSentence` TEXT, `imageUrl` TEXT, PRIMARY KEY(`wordId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `exercises` (`exerciseId` INTEGER NOT NULL, `lessonId` INTEGER NOT NULL, `unitId` INTEGER NOT NULL, `orderIndex` INTEGER NOT NULL, `exerciseType` TEXT, `question` TEXT, `audioFile` TEXT, `correctAnswer` TEXT, `experienceReward` INTEGER NOT NULL, `options` TEXT, PRIMARY KEY(`exerciseId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '555ddb9d29f593a7d07485047b3a29a5')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `words`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `exercises`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsWords = new HashMap<String, TableInfo.Column>(10);
        _columnsWords.put("wordId", new TableInfo.Column("wordId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("languageId", new TableInfo.Column("languageId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("lessonId", new TableInfo.Column("lessonId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("wordName", new TableInfo.Column("wordName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("translation", new TableInfo.Column("translation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("pronunciation", new TableInfo.Column("pronunciation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("wordType", new TableInfo.Column("wordType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("audioFile", new TableInfo.Column("audioFile", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("exampleSentence", new TableInfo.Column("exampleSentence", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWords.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysWords = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesWords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWords = new TableInfo("words", _columnsWords, _foreignKeysWords, _indicesWords);
        final TableInfo _existingWords = TableInfo.read(connection, "words");
        if (!_infoWords.equals(_existingWords)) {
          return new RoomOpenDelegate.ValidationResult(false, "words(com.example.learninglanguageapp.models.Entities.WordEntity).\n"
                  + " Expected:\n" + _infoWords + "\n"
                  + " Found:\n" + _existingWords);
        }
        final Map<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(10);
        _columnsExercises.put("exerciseId", new TableInfo.Column("exerciseId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("lessonId", new TableInfo.Column("lessonId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("unitId", new TableInfo.Column("unitId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("orderIndex", new TableInfo.Column("orderIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("exerciseType", new TableInfo.Column("exerciseType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("question", new TableInfo.Column("question", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("audioFile", new TableInfo.Column("audioFile", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("correctAnswer", new TableInfo.Column("correctAnswer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("experienceReward", new TableInfo.Column("experienceReward", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("options", new TableInfo.Column("options", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(connection, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenDelegate.ValidationResult(false, "exercises(com.example.learninglanguageapp.models.Entities.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "words", "exercises");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "words", "exercises");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(WordDao.class, WordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseDAO.class, ExerciseDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public WordDao wordDao() {
    if (_wordDao != null) {
      return _wordDao;
    } else {
      synchronized(this) {
        if(_wordDao == null) {
          _wordDao = new WordDao_Impl(this);
        }
        return _wordDao;
      }
    }
  }

  @Override
  public ExerciseDAO exerciseDao() {
    if (_exerciseDAO != null) {
      return _exerciseDAO;
    } else {
      synchronized(this) {
        if(_exerciseDAO == null) {
          _exerciseDAO = new ExerciseDAO_Impl(this);
        }
        return _exerciseDAO;
      }
    }
  }
}
