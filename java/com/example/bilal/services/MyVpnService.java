package com.example.bilal.services;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class MyVpnService extends VpnService {
    private ParcelFileDescriptor vpnInterface = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "ACTION_CONNECT".equals(intent.getAction())) {
            establishVpn();
        } else if (intent != null && "ACTION_DISCONNECT".equals(intent.getAction())) {
            stopVpn();
            stopSelf();
        }
        return START_STICKY;
    }

    private void establishVpn() {
        try {
            if (vpnInterface == null) {
                Builder builder = new Builder();
                builder.setSession("MyVPN")
                       .addAddress("10.0.0.2", 24)
                       .addDnsServer("8.8.8.8")
                       .addRoute("0.0.0.0", 0);
                vpnInterface = builder.establish();
                Log.d("VPN", "VPN Interface established");
            }
        } catch (Exception e) {
            Log.e("VPN", "Failed to establish VPN", e);
        }
    }

    private void stopVpn() {
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
                Log.d("VPN", "VPN Interface closed");
            }
        } catch (Exception e) {
            Log.e("VPN", "Failed to stop VPN", e);
        }
    }

    @Override
    public void onDestroy() {
        stopVpn();
        super.onDestroy();
    }

    @Override
    public void onRevoke() {
        stopVpn();
        super.onRevoke();
    }
}