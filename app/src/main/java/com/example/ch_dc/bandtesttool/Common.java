package com.example.ch_dc.bandtesttool;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

public class Common extends Application{
    public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
            0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
            0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
            0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
            0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
            0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
            0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
            0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
            0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
            0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
            0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
            0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
            0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
            0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
            0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
            0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
            0x207F, 0x00B2, 0x25A0, 0x00A0 };
	private String TAG = "common";
	private BluetoothSocket btSocket = null;
    private Object myGloballyAccessibleObject; //make getter and setter
    private static Common singleInstance = null;
	private static final UUID MY_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805F9B34FB");
    private String m_function_name = "";
    private String m_function_detail = "";

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
    public char getAscii(int code) {
        if (code >= 0x80 && code <= 0xFF) {
            return EXTENDED[code - 0x7F];
        }
        return (char) code;
    }

    public void printChar(int code) {
        Log.d(TAG, "Output ASCII:" + getAscii(code));
    }

    public char AsciiToChar(int asc){
        printChar(asc);
        return (char)asc;
    }

    public void SendMsg(String _function_name, String _function_detail) {
        m_function_name = _function_name;
        m_function_detail = _function_detail;
        Thread SendThread = new Thread(new Runnable() {
            public void run() {
                OutputStream outStream;
                String json = m_function_detail;
                int length = json.length();
                int x1 = ((length & 0xFF000000) >> 24);
                int x2 = ((length & 0x00FF0000) >> 16);
                int x3 = ((length & 0x0000FF00) >> 8);
                int x4 = ((length & 0x000000FF));
                AsciiToChar(x4);
                String message = ")!@#" + AsciiToChar(0) + Character.toString ((char)1) + Character.toString ((char)0) + Character.toString ((char)0) + Character.toString ((char)0) + Character.toString ((char)0) + Character.toString ((char)0) + Character.toString ((char)0) + Character.toString ((char)x1) + Character.toString ((char)x2) + Character.toString ((char)x3) + getAscii(x4) + json;
                byte[] msgBuffer = null;
                try {
                    msgBuffer = message.getBytes("Cp437");
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                String decodedData = new String(msgBuffer);  // Create new String Object and assign byte[] to it
                System.out.println("\nText Decryted : " + decodedData);
                String decodedDataUsingUTF8;
                try {
                    decodedDataUsingUTF8 = new String(msgBuffer, "UTF-8");  // Best way to decode using "UTF-8"
                    System.out.println("Text Decryted using UTF-8 : " + decodedDataUsingUTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    outStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    Log.d(TAG, "Output stream creation failed:" + e.getMessage() + ".");
                    return;
                }
                try {
                    outStream.write(msgBuffer);
                    Log.d(TAG, "Output stream write:" + message);
                } catch (IOException e) {
                    Log.d(TAG, "Output stream writing failed:" + e.getMessage() + ".");
                }
            }
        });
        SendThread.start();
    }
}
