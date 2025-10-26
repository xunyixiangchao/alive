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
import com.example.alive.data.entity.AliveImage;
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
public final class AliveImageDao_Impl implements AliveImageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AliveImage> __insertionAdapterOfAliveImage;

  private final EntityDeletionOrUpdateAdapter<AliveImage> __deletionAdapterOfAliveImage;

  private final EntityDeletionOrUpdateAdapter<AliveImage> __updateAdapterOfAliveImage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public AliveImageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAliveImage = new EntityInsertionAdapter<AliveImage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `alive_images` (`id`,`filePath`,`fileName`,`fileSize`,`mimeType`,`isVideo`,`duration`,`createdTime`,`thumbnailPath`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AliveImage entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFilePath() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFilePath());
        }
        if (entity.getFileName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFileName());
        }
        statement.bindLong(4, entity.getFileSize());
        if (entity.getMimeType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMimeType());
        }
        final int _tmp = entity.isVideo() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getDuration());
        statement.bindLong(8, entity.getCreatedTime());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getThumbnailPath());
        }
      }
    };
    this.__deletionAdapterOfAliveImage = new EntityDeletionOrUpdateAdapter<AliveImage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `alive_images` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AliveImage entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfAliveImage = new EntityDeletionOrUpdateAdapter<AliveImage>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `alive_images` SET `id` = ?,`filePath` = ?,`fileName` = ?,`fileSize` = ?,`mimeType` = ?,`isVideo` = ?,`duration` = ?,`createdTime` = ?,`thumbnailPath` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AliveImage entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFilePath() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFilePath());
        }
        if (entity.getFileName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getFileName());
        }
        statement.bindLong(4, entity.getFileSize());
        if (entity.getMimeType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMimeType());
        }
        final int _tmp = entity.isVideo() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getDuration());
        statement.bindLong(8, entity.getCreatedTime());
        if (entity.getThumbnailPath() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getThumbnailPath());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM alive_images WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AliveImage aliveImage, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfAliveImage.insertAndReturnId(aliveImage);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AliveImage aliveImage, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAliveImage.handle(aliveImage);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AliveImage aliveImage, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAliveImage.handle(aliveImage);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Object getById(final long id, final Continuation<? super AliveImage> $completion) {
    final String _sql = "SELECT * FROM alive_images WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AliveImage>() {
      @Override
      @Nullable
      public AliveImage call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfIsVideo = CursorUtil.getColumnIndexOrThrow(_cursor, "isVideo");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final AliveImage _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final boolean _tmpIsVideo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVideo);
            _tmpIsVideo = _tmp != 0;
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            _result = new AliveImage(_tmpId,_tmpFilePath,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpIsVideo,_tmpDuration,_tmpCreatedTime,_tmpThumbnailPath);
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
  public Flow<List<AliveImage>> getAllFlow() {
    final String _sql = "SELECT * FROM alive_images ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alive_images"}, new Callable<List<AliveImage>>() {
      @Override
      @NonNull
      public List<AliveImage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfIsVideo = CursorUtil.getColumnIndexOrThrow(_cursor, "isVideo");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final List<AliveImage> _result = new ArrayList<AliveImage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AliveImage _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final boolean _tmpIsVideo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVideo);
            _tmpIsVideo = _tmp != 0;
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            _item = new AliveImage(_tmpId,_tmpFilePath,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpIsVideo,_tmpDuration,_tmpCreatedTime,_tmpThumbnailPath);
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
  public Object getAll(final Continuation<? super List<AliveImage>> $completion) {
    final String _sql = "SELECT * FROM alive_images ORDER BY createdTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AliveImage>>() {
      @Override
      @NonNull
      public List<AliveImage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfIsVideo = CursorUtil.getColumnIndexOrThrow(_cursor, "isVideo");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfCreatedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createdTime");
          final int _cursorIndexOfThumbnailPath = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailPath");
          final List<AliveImage> _result = new ArrayList<AliveImage>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AliveImage _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpFileName;
            if (_cursor.isNull(_cursorIndexOfFileName)) {
              _tmpFileName = null;
            } else {
              _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final boolean _tmpIsVideo;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVideo);
            _tmpIsVideo = _tmp != 0;
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final long _tmpCreatedTime;
            _tmpCreatedTime = _cursor.getLong(_cursorIndexOfCreatedTime);
            final String _tmpThumbnailPath;
            if (_cursor.isNull(_cursorIndexOfThumbnailPath)) {
              _tmpThumbnailPath = null;
            } else {
              _tmpThumbnailPath = _cursor.getString(_cursorIndexOfThumbnailPath);
            }
            _item = new AliveImage(_tmpId,_tmpFilePath,_tmpFileName,_tmpFileSize,_tmpMimeType,_tmpIsVideo,_tmpDuration,_tmpCreatedTime,_tmpThumbnailPath);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
