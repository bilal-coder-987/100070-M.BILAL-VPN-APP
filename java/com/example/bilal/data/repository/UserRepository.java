package com.example.bilal.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.AppDatabase;
import com.example.bilal.data.local.dao.UserDao;
import com.example.bilal.data.local.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private LiveData<User> user;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        user = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.updateUser(user));
    }
}