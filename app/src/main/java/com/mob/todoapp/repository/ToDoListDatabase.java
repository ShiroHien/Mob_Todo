package com.mob.todoapp.repository;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mob.todoapp.model.TasksGroup;

@Database(entities = {TasksGroup.class}, version = 1)
public abstract class ToDoListDatabase extends RoomDatabase {
    private static ToDoListDatabase instance;
    public abstract TasksGroupDao tasksGroupDao();

    public static synchronized ToDoListDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoListDatabase.class, "todo_list_databse")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TasksGroupDao tasksGroupDao;

        private PopulateDbAsyncTask(ToDoListDatabase db) {
            tasksGroupDao = db.tasksGroupDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
