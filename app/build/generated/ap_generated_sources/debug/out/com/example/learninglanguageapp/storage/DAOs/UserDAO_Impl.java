package com.example.learninglanguageapp.storage.DAOs;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.learninglanguageapp.models.Entities.UserEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDAO_Impl implements UserDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UserEntity> __insertAdapterOfUserEntity;

  public UserDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUserEntity = new EntityInsertAdapter<UserEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`userId`,`fullName`,`phoneNumber`,`dateOfBirth`,`nativeLanguageId`,`totalExperience`,`currentStreak`,`longestStreak`,`hearts`,`subscriptionType`,`diamond`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final UserEntity entity) {
        if (entity.getUserId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getUserId());
        }
        if (entity.getFullName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getFullName());
        }
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getPhoneNumber());
        }
        if (entity.getDateOfBirth() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDateOfBirth());
        }
        statement.bindLong(5, entity.getNativeLanguageId());
        statement.bindLong(6, entity.getTotalExperience());
        statement.bindLong(7, entity.getCurrentStreak());
        statement.bindLong(8, entity.getLongestStreak());
        statement.bindLong(9, entity.getHearts());
        if (entity.getSubscriptionType() == null) {
          statement.bindNull(10);
        } else {
          statement.bindText(10, entity.getSubscriptionType());
        }
        statement.bindLong(11, entity.getDiamond());
      }
    };
  }

  @Override
  public void insertUser(final UserEntity user) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUserEntity.insert(_connection, user);
      return null;
    });
  }

  @Override
  public UserEntity getCurrentUser() {
    final String _sql = "SELECT * FROM users LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfFullName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fullName");
        final int _columnIndexOfPhoneNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "phoneNumber");
        final int _columnIndexOfDateOfBirth = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dateOfBirth");
        final int _columnIndexOfNativeLanguageId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nativeLanguageId");
        final int _columnIndexOfTotalExperience = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "totalExperience");
        final int _columnIndexOfCurrentStreak = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currentStreak");
        final int _columnIndexOfLongestStreak = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "longestStreak");
        final int _columnIndexOfHearts = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hearts");
        final int _columnIndexOfSubscriptionType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "subscriptionType");
        final int _columnIndexOfDiamond = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "diamond");
        final UserEntity _result;
        if (_stmt.step()) {
          final String _tmpUserId;
          if (_stmt.isNull(_columnIndexOfUserId)) {
            _tmpUserId = null;
          } else {
            _tmpUserId = _stmt.getText(_columnIndexOfUserId);
          }
          final String _tmpFullName;
          if (_stmt.isNull(_columnIndexOfFullName)) {
            _tmpFullName = null;
          } else {
            _tmpFullName = _stmt.getText(_columnIndexOfFullName);
          }
          final String _tmpPhoneNumber;
          if (_stmt.isNull(_columnIndexOfPhoneNumber)) {
            _tmpPhoneNumber = null;
          } else {
            _tmpPhoneNumber = _stmt.getText(_columnIndexOfPhoneNumber);
          }
          final String _tmpDateOfBirth;
          if (_stmt.isNull(_columnIndexOfDateOfBirth)) {
            _tmpDateOfBirth = null;
          } else {
            _tmpDateOfBirth = _stmt.getText(_columnIndexOfDateOfBirth);
          }
          final int _tmpNativeLanguageId;
          _tmpNativeLanguageId = (int) (_stmt.getLong(_columnIndexOfNativeLanguageId));
          final int _tmpTotalExperience;
          _tmpTotalExperience = (int) (_stmt.getLong(_columnIndexOfTotalExperience));
          final int _tmpCurrentStreak;
          _tmpCurrentStreak = (int) (_stmt.getLong(_columnIndexOfCurrentStreak));
          final int _tmpLongestStreak;
          _tmpLongestStreak = (int) (_stmt.getLong(_columnIndexOfLongestStreak));
          final int _tmpHearts;
          _tmpHearts = (int) (_stmt.getLong(_columnIndexOfHearts));
          final String _tmpSubscriptionType;
          if (_stmt.isNull(_columnIndexOfSubscriptionType)) {
            _tmpSubscriptionType = null;
          } else {
            _tmpSubscriptionType = _stmt.getText(_columnIndexOfSubscriptionType);
          }
          final int _tmpDiamond;
          _tmpDiamond = (int) (_stmt.getLong(_columnIndexOfDiamond));
          _result = new UserEntity(_tmpUserId,_tmpFullName,_tmpPhoneNumber,_tmpDateOfBirth,_tmpNativeLanguageId,_tmpTotalExperience,_tmpCurrentStreak,_tmpLongestStreak,_tmpHearts,_tmpSubscriptionType,_tmpDiamond);
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
  public boolean hasUser() {
    final String _sql = "SELECT EXISTS(SELECT * FROM users LIMIT 1)";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final boolean _result;
        if (_stmt.step()) {
          final int _tmp;
          _tmp = (int) (_stmt.getLong(0));
          _result = _tmp != 0;
        } else {
          _result = false;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteUser() {
    final String _sql = "DELETE FROM users";
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
