package com.mob.todoapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mob.todoapp.model.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTask;

    public TaskRepository(Application application) {
        ToDoListDatabase db = ToDoListDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTask = taskDao.getAllTask();
    }

    public void insert(Task task) {
        new TaskRepository.InsertTaskAsyncTask(taskDao).execute(task);
    }
    public void update(Task task) {
        new TaskRepository.UpdateTaskAsyncTask(taskDao).execute(task);
    }
    public void delete(Task task) {
        new TaskRepository.DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAll() {
        new TaskRepository.DeleteAllTaskAsyncTask(taskDao).execute();
    }
    public LiveData<List<Task>> getAllTask() {
        return allTask;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }


    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTaskAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private DeleteAllTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }
}
