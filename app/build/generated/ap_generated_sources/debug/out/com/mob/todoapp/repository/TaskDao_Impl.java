package com.mob.todoapp.repository;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mob.todoapp.model.Task;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Task> __insertionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter<Task> __deletionAdapterOfTask;

  private final EntityDeletionOrUpdateAdapter<Task> __updateAdapterOfTask;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TaskDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTask = new EntityInsertionAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `task` (`id`,`tasksGroupId`,`title`,`timeExpired`,`isLoop`,`isAddedToMyDay`,`isRemind`,`note`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getTasksGroupId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getTimeExpired() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTimeExpired());
        }
        final int _tmp = value.isLoop() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1 = value.isAddedToMyDay() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        final int _tmp_2 = value.isRemind() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        if (value.getNote() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getNote());
        }
      }
    };
    this.__deletionAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `task` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfTask = new EntityDeletionOrUpdateAdapter<Task>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `task` SET `id` = ?,`tasksGroupId` = ?,`title` = ?,`timeExpired` = ?,`isLoop` = ?,`isAddedToMyDay` = ?,`isRemind` = ?,`note` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Task value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getTasksGroupId());
        if (value.getTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTitle());
        }
        if (value.getTimeExpired() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTimeExpired());
        }
        final int _tmp = value.isLoop() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1 = value.isAddedToMyDay() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        final int _tmp_2 = value.isRemind() ? 1 : 0;
        stmt.bindLong(7, _tmp_2);
        if (value.getNote() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getNote());
        }
        stmt.bindLong(9, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from task";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Task task) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTask.insert(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Task task) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Task task) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Task>> getAllTask() {
    final String _sql = "select * from task";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"task"}, false, new Callable<List<Task>>() {
      @Override
      public List<Task> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTasksGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "tasksGroupId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTimeExpired = CursorUtil.getColumnIndexOrThrow(_cursor, "timeExpired");
          final int _cursorIndexOfIsLoop = CursorUtil.getColumnIndexOrThrow(_cursor, "isLoop");
          final int _cursorIndexOfIsAddedToMyDay = CursorUtil.getColumnIndexOrThrow(_cursor, "isAddedToMyDay");
          final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Task _item;
            _item = new Task();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpTasksGroupId;
            _tmpTasksGroupId = _cursor.getInt(_cursorIndexOfTasksGroupId);
            _item.setTasksGroupId(_tmpTasksGroupId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpTimeExpired;
            if (_cursor.isNull(_cursorIndexOfTimeExpired)) {
              _tmpTimeExpired = null;
            } else {
              _tmpTimeExpired = _cursor.getString(_cursorIndexOfTimeExpired);
            }
            _item.setTimeExpired(_tmpTimeExpired);
            final boolean _tmpIsLoop;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLoop);
            _tmpIsLoop = _tmp != 0;
            _item.setLoop(_tmpIsLoop);
            final boolean _tmpIsAddedToMyDay;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAddedToMyDay);
            _tmpIsAddedToMyDay = _tmp_1 != 0;
            _item.setAddedToMyDay(_tmpIsAddedToMyDay);
            final boolean _tmpIsRemind;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsRemind);
            _tmpIsRemind = _tmp_2 != 0;
            _item.setRemind(_tmpIsRemind);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item.setNote(_tmpNote);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
