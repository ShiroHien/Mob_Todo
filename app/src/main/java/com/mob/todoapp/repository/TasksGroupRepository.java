package com.mob.todoapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mob.todoapp.model.TasksGroup;

import java.util.List;

public class TasksGroupRepository {
    private TasksGroupDao tasksGroupDao;
    private LiveData<List<TasksGroup>> allTasksGroup;

    public TasksGroupRepository(Application application) {
        ToDoListDatabase db = ToDoListDatabase.getInstance(application);
        tasksGroupDao = db.tasksGroupDao();
        allTasksGroup = tasksGroupDao.getAllTasksGroup();
    }

    public void insert(TasksGroup tasksGroup) {
        new InsertTasksGroupAsyncTask(tasksGroupDao).execute(tasksGroup);
    }
    public void update(TasksGroup tasksGroup) {
        new UpdateTasksGroupAsyncTask(tasksGroupDao).execute(tasksGroup);
    }
    public void delete(TasksGroup tasksGroup) {
        new DeleteTasksGroupAsyncTask(tasksGroupDao).execute(tasksGroup);
    }

    public void deleteAll() {
        new DeleteAllTasksGroupAsyncTask(tasksGroupDao).execute();
    }
    public LiveData<List<TasksGroup>> getAllTasksGroup() {
        return allTasksGroup;
    }

    private static class InsertTasksGroupAsyncTask extends AsyncTask<TasksGroup, Void, Void> {
        private TasksGroupDao tasksGroupDao;
        private InsertTasksGroupAsyncTask(TasksGroupDao tasksGroupDao) {
            this.tasksGroupDao = tasksGroupDao;
        }
        @Override
        protected Void doInBackground(TasksGroup... tasksGroups) {
            tasksGroupDao.insert(tasksGroups[0]);
            return null;
        }
    }


    private static class UpdateTasksGroupAsyncTask extends AsyncTask<TasksGroup, Void, Void> {
        private TasksGroupDao tasksGroupDao;
        private UpdateTasksGroupAsyncTask(TasksGroupDao tasksGroupDao) {
            this.tasksGroupDao = tasksGroupDao;
        }
        @Override
        protected Void doInBackground(TasksGroup... tasksGroups) {
            tasksGroupDao.update(tasksGroups[0]);
            return null;
        }
    }

    private static class DeleteTasksGroupAsyncTask extends AsyncTask<TasksGroup, Void, Void> {
        private TasksGroupDao tasksGroupDao;
        private DeleteTasksGroupAsyncTask(TasksGroupDao tasksGroupDao) {
            this.tasksGroupDao = tasksGroupDao;
        }
        @Override
        protected Void doInBackground(TasksGroup... tasksGroups) {
            tasksGroupDao.delete(tasksGroups[0]);
            return null;
        }
    }

    private static class DeleteAllTasksGroupAsyncTask extends AsyncTask<Void, Void, Void> {
        private TasksGroupDao tasksGroupDao;
        private DeleteAllTasksGroupAsyncTask(TasksGroupDao tasksGroupDao) {
            this.tasksGroupDao = tasksGroupDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            tasksGroupDao.deleteAll();
            return null;
        }
    }
}
