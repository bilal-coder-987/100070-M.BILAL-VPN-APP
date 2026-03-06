package com.example.bilal.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.bilal.data.model.Server;

public class VpnViewModel extends ViewModel {
    private final MutableLiveData<Server> selectedServer = new MutableLiveData<>();
    private final MutableLiveData<Boolean> connectionStatus = new MutableLiveData<>(false);

    public void selectServer(Server server) {
        selectedServer.setValue(server);
    }

    public LiveData<Server> getSelectedServer() {
        return selectedServer;
    }

    public void setConnected(boolean connected) {
        connectionStatus.setValue(connected);
    }

    public LiveData<Boolean> getConnectionStatus() {
        return connectionStatus;
    }
}