package com.example.alive.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.alive.data.db.DatabaseConverters;
import com.example.alive.data.entity.Task;
import com.example.alive.data.entity.TaskStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Task> __insertionAdapterOfTask;

  private final DatabaseConverters __databaseConverters = new DatabaseConverters();

  private final EntityDeletionOrUpdateAdapter<Task> __deletionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter<Task> __updateAdapterOfTask;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavorite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTask = new EntityInsertionAdapter<Task>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tasks` (`id`,`aliveImageId`,`markingImagePath`,`circleSelectionsJson`,`resultImagePath`,`status`,`createdTime`,`completedTime`,`isFavorite`,`errorMessage`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Task entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAliveImageId());
        if (entity.getMarkingImagePath() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMarkingImagePath());
        }
        if (entity.getCircleSelectionsJson() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCircleSelectionsJson());
        }
        if (entity.getResultImagePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getResultImagePath());
        }
        final String _tmp = __databaseConverters.fromTaskStatus(entity.getStatus());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        statement.bindLong(7, entity.getCreatedTime());
        if (entity.getCompletedTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCompletedTime());
        }
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getErrorMessage() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getErrorMessage());
        }
      }
    };
    this.__deletionAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Task entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`aliveImageId` = ?,`markingImagePath` = ?,`circleSelectionsJson` = ?,`resultImagePath` = ?,`status` = ?,`createdTime` = ?,`completedTime` = ?,`isFavorite` = ?,`errorMessage` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Task entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAliveImageId());
        if (entity.getMarkingImagePath() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMarkingImagePath());
        }
        if (entity.getCircleSelectionsJson() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCircleSelectionsJson());
        }
        if (entity.getResultImagePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getResultImagePath());
        }
        final String _tmp = __databaseConverters.fromTaskStatus(entity.getStatus());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp);
        }
        statement.bindLong(7, entity.getCreatedTime());
        if (entity.getCompletedTime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getCompletedTime());
        }
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getErrorMessage() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getErrorMessage());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET status = ?, completedTime = ?, resultImagePath = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Task task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTask.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Task task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTask.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Task task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTask.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final TaskStatus status, final Long completedTime,
      final String resultImagePath, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __databaseConverters.fromTaskStatus(status);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        if (completedTime == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, completedTime);
        }
        _argIndex = 3;
        if (resultImagePath == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, resultImagePath);
        }
        _argIndex = 4;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavorite(final long id, final boolean isFavorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super Task> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Task>() {
      @Override
      @Nullable
      public Task call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final Task _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _result = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Task>> getAllFlow() {
    final String _sql = "SELECT * FROM tasks ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<Task>>() {
      @Override
      @NonNull
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Task _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAll(final Continuation<? super List<Task>> $completion) {
    final String _sql = "SELECT * FROM tasks ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Task>>() {
      @Override
      @NonNull
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Task _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Task>> getByStatusFlow(final TaskStatus status) {
    final String _sql = "SELECT * FROM tasks WHERE status = ? ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __databaseConverters.fromTaskStatus(status);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<Task>>() {
      @Override
      @NonNull
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Task _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp_1);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_2 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByStatus(final TaskStatus status,
      final Continuation<? super List<Task>> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE status = ? ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __databaseConverters.fromTaskStatus(status);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Task>>() {
      @Override
      @NonNull
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Task _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp_1);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_2 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Task>> getFavoritesFlow() {
    final String _sql = "SELECT * FROM tasks WHERE isFavorite = 1 ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<Task>>() {
      @Override
      @NonNull
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAliveImageId = CursorUtil.getColumnIndexOrThrow(_cursor, "aliveImageId");
          final int _cursorIndexOfMarkingImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "markingImagePath");
          final int _cursorIndexOfCircleSelectionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "circleSelectionsJson");
          final int _cursorIndexOfResultImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "resultImagePath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfCompletedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "completedTime");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Task _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpAliveImageId;
            _tmpAliveImageId = _cursor.getLong(_cursorIndexOfAliveImageId);
            final String _tmpMarkingImagePath;
            if (_cursor.isNull(_cursorIndexOfMarkingImagePath)) {
              _tmpMarkingImagePath = null;
            } else {
              _tmpMarkingImagePath = _cursor.getString(_cursorIndexOfMarkingImagePath);
            }
            final String _tmpCircleSelectionsJson;
            if (_cursor.isNull(_cursorIndexOfCircleSelectionsJson)) {
              _tmpCircleSelectionsJson = null;
            } else {
              _tmpCircleSelectionsJson = _cursor.getString(_cursorIndexOfCircleSelectionsJson);
            }
            final String _tmpResultImagePath;
            if (_cursor.isNull(_cursorIndexOfResultImagePath)) {
              _tmpResultImagePath = null;
            } else {
              _tmpResultImagePath = _cursor.getString(_cursorIndexOfResultImagePath);
            }
            final TaskStatus _tmpStatus;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStatus);
            }
            _tmpStatus = __databaseConverters.toTaskStatus(_tmp);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final Long _tmpCompletedTime;
            if (_cursor.isNull(_cursorIndexOfCompletedTime)) {
              _tmpCompletedTime = null;
            } else {
              _tmpCompletedTime = _cursor.getLong(_cursorIndexOfCompletedTime);
            }
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            _item = new Task(_tmpId,_tmpAliveImageId,_tmpMarkingImagePath,_tmpCircleSelectionsJson,_tmpResultImagePath,_tmpStatus,_tmpCreatedTime,_tmpCompletedTime,_tmpIsFavorite,_tmpErrorMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
