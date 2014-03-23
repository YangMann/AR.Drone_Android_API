package com.parrot.freeflight.service.commands;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.parrot.freeflight.drone.*;
import com.parrot.freeflight.service.DroneControlService;

/**
 * Created by Yang Zhang on 2014/3/22.
 */
public class DisconnectCommand extends DroneServiceCommand
        implements DroneProxyDisconnectedReceiverDelegate, DroneProxyConnectionFailedReceiverDelegate {
    private DroneProxy droneProxy;

    private LocalBroadcastManager bm;

    private DroneProxyDisconnectedReceiver disconnectedReceiver;
    private DroneProxyConnectionFailedReceiver connFailedReceiver;


    public DisconnectCommand(DroneControlService context) {
        super(context);
        droneProxy = DroneProxy.getInstance(context.getApplicationContext());

        bm = LocalBroadcastManager.getInstance(context.getApplicationContext());

        disconnectedReceiver = new DroneProxyDisconnectedReceiver(this);
        connFailedReceiver = new DroneProxyConnectionFailedReceiver(this);
    }


    @Override
    public void execute() {
        registerListeners();
        droneProxy.doResume();
        droneProxy.doDisconnect();
    }


    public void onToolConnectionFailed(int reason) {
        unregisterListeners();
        // Ignore this event
    }


    public void onToolDisconnected() {
        unregisterListeners();
        context.onCommandFinished(this);
    }


    private void registerListeners() {
        bm.registerReceiver(disconnectedReceiver, new IntentFilter(DroneProxy.DRONE_PROXY_DISCONNECTED_ACTION));
        bm.registerReceiver(connFailedReceiver, new IntentFilter(DroneProxy.DRONE_PROXY_CONNECTION_FAILED_ACTION));
    }


    private void unregisterListeners() {
        bm.unregisterReceiver(disconnectedReceiver);
        bm.unregisterReceiver(connFailedReceiver);
    }
}
