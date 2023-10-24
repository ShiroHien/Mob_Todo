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
import com.mob.todoapp.model.TasksGroup;
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
public final class TasksGroupDao_Impl implements TasksGroupDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TasksGroup> __insertionAdapterOfTasksGroup;

  private final EntityDeletionOrUpdateAdapter<TasksGroup> __deletionAdapterOfTasksGroup;

  private final EntityDeletionOrUpdateAdapter<TasksGroup> __updateAdapterOfTasksGroup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TasksGroupDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTasksGroup = new EntityInsertionAdapter<TasksGroup>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `TasksGroup` (`id`,`title`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TasksGroup value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
      }
    };
    this.__deletionAdapterOfTasksGroup = new EntityDeletionOrUpdateAdapter<TasksGroup>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `TasksGroup` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TasksGroup value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfTasksGroup = new EntityDeletionOrUpdateAdapter<TasksGroup>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `TasksGroup` SET `id` = ?,`title` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TasksGroup value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        stmt.bindLong(3, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from tasksgroup";
        return _query;
      }
    };
  }

  @Override
  public void insert(final TasksGroup tasksGroup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTasksGroup.insert(tasksGroup);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final TasksGroup tasksGroup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTasksGroup.handle(tasksGroup);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TasksGroup tasksGroup) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTasksGroup.handle(tasksGroup);
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
  public LiveData<List<TasksGroup>> getAllTasksGroup() {
    final String _sql = "select * from tasksgroup";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"tasksgroup"}, false, new Callable<List<TasksGroup>>() {
      @Override
      public List<TasksGroup> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final List<TasksGroup> _result = new ArrayList<TasksGroup>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TasksGroup _item;
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item = new TasksGroup(_tmpTitle);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
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
  public TasksGroup getTasksGroupById(final int id) {
    final String _sql = "select * from tasksgroup where id = (?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final TasksGroup _result;
      if(_cursor.moveToFirst()) {
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result = new TasksGroup(_tmpTitle);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
