package com.example.bilal.ui.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.entity.User;
import com.example.bilal.data.repository.UserRepository;

public class HomeViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<User> user;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        user = repository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }
}