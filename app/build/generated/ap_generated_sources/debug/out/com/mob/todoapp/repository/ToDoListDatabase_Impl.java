package com.mob.todoapp.repository;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ToDoListDatabase_Impl extends ToDoListDatabase {
  private volatile TasksGroupDao _tasksGroupDao;

  private volatile TaskDao _taskDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TasksGroup` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `task` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tasksGroupId` INTEGER NOT NULL, `title` TEXT, `timeExpired` TEXT, `isLoop` INTEGER NOT NULL, `isAddedToMyDay` INTEGER NOT NULL, `isRemind` INTEGER NOT NULL, `note` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ce31114caafa5b6de0e8ce1045fde6c8')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TasksGroup`");
        _db.execSQL("DROP TABLE IF EXISTS `task`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTasksGroup = new HashMap<String, TableInfo.Column>(2);
        _columnsTasksGroup.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasksGroup.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasksGroup = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasksGroup = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTasksGroup = new TableInfo("TasksGroup", _columnsTasksGroup, _foreignKeysTasksGroup, _indicesTasksGroup);
        final TableInfo _existingTasksGroup = TableInfo.read(_db, "TasksGroup");
        if (! _infoTasksGroup.equals(_existingTasksGroup)) {
          return new RoomOpenHelper.ValidationResult(false, "TasksGroup(com.mob.todoapp.model.TasksGroup).\n"
                  + " Expected:\n" + _infoTasksGroup + "\n"
                  + " Found:\n" + _existingTasksGroup);
        }
        final HashMap<String, TableInfo.Column> _columnsTask = new HashMap<String, TableInfo.Column>(8);
        _columnsTask.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("tasksGroupId", new TableInfo.Column("tasksGroupId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("timeExpired", new TableInfo.Column("timeExpired", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("isLoop", new TableInfo.Column("isLoop", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("isAddedToMyDay", new TableInfo.Column("isAddedToMyDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("isRemind", new TableInfo.Column("isRemind", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTask.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTask = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTask = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTask = new TableInfo("task", _columnsTask, _foreignKeysTask, _indicesTask);
        final TableInfo _existingTask = TableInfo.read(_db, "task");
        if (! _infoTask.equals(_existingTask)) {
          return new RoomOpenHelper.ValidationResult(false, "task(com.mob.todoapp.model.Task).\n"
                  + " Expected:\n" + _infoTask + "\n"
                  + " Found:\n" + _existingTask);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ce31114caafa5b6de0e8ce1045fde6c8", "f8f7fd158e3ee782743a48d357ca1628");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "TasksGroup","task");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `TasksGroup`");
      _db.execSQL("DELETE FROM `task`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TasksGroupDao.class, TasksGroupDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public TasksGroupDao tasksGroupDao() {
    if (_tasksGroupDao != null) {
      return _tasksGroupDao;
    } else {
      synchronized(this) {
        if(_tasksGroupDao == null) {
          _tasksGroupDao = new TasksGroupDao_Impl(this);
        }
        return _tasksGroupDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }
}
