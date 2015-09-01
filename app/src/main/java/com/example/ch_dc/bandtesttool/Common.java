package com.example.ch_dc.bandtesttool;

import java.io.IOException;
import java.util.UUID;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class Common extends Application{
	private String TAG = "common";
	private BluetoothSocket btSocket = null;
    private Object myGloballyAccessibleObject; //make getter and setter
    private static Common singleInstance = null;
	private static final UUID MY_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805F9B34FB");

    public static Common getInstance(){
    	if (singleInstance == null){
    		singleInstance = new Common();
    	}
        return singleInstance;
    }
	public BluetoothSocket get_btSocker(){
		return btSocket;
	}
	public void create_socket(BluetoothDevice device){
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.d(TAG, "In onResume() and socket create failed: " + e.getMessage() + ".");
        }
		
	}
	public boolean connect_sock(){
		try {
            btSocket.connect();
            Log.d(TAG, "[Client]...Connection established and data link opened...");
            return true;
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "Unable to close socket during connection failure" + e2.getMessage() + ".");
            }

            return false;
        }
	}
}
