package com.example.smokeapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BlueTooth {
	private final BluetoothAdapter mAdapter;
	
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    
    private Handler getNextOpenTimeHandler;
    private Handler getOpenTimeHandler;
    private Handler setOpenTimeHandler;
    private Handler getOpenLogHandler;
    private Handler restoreHandler;
    
    public static final byte BT_CMD_OPEN_TIME = 0x01;
    public static final byte BT_CMD_OPEN_LOG = 0x02;
    public static final byte BT_CMD_OPEN_NEXT = 0x03;
    public static final byte BT_CMD_RESTORE = 0x04;
    public static final byte BT_OPT_SET = (byte) 0xb2;
    public static final byte BT_OPT_GET = (byte) 0xb2;
    
    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    
	private BlueTooth() {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}  
    private static volatile BlueTooth instance = null;  
    public static BlueTooth getInstance() {  
           if (instance == null) {    
             synchronized (BlueTooth.class) {    
                if (instance == null) {    
                	instance = new BlueTooth();   
                }    
             }    
           }   
           return instance;  
    } 
    
    
    

    public synchronized void connect(String address, boolean secure) {
    	BluetoothDevice device = mAdapter.getRemoteDevice(address);
        if (mConnectThread != null) {
        	mConnectThread.cancel(); 
        	mConnectThread = null;
        }
        if (mConnectedThread != null) {
        	mConnectedThread.cancel(); 
        	mConnectedThread = null;
        }
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
    }
    
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();
    }
    

    private byte xorCheck(byte[] dat,int len) {
		byte ret = 0;
		for (int i = 0; i < len; i++) {
			ret^=dat[i]; 
		}
		return ret;
	}
    
    private void fillBtHeader(byte[] dat) {
		dat[0] = (byte) 0xeb;
		dat[1] = (byte) 0xa5;
	}
    
    public void getNextOpenTime(Handler handler) {
    	getNextOpenTimeHandler = handler;
    	byte[] dat = new byte[6];
    	fillBtHeader(dat);
    	dat[2] = 2;
    	dat[3] = BT_OPT_GET;
    	dat[4] = BT_CMD_OPEN_NEXT;
    	dat[5] = xorCheck(dat,5);
    	mConnectedThread.write(dat);
    }
    public void getOpenTime(Handler handler) {
    	getOpenTimeHandler = handler;
    	byte[] dat = new byte[6];
    	fillBtHeader(dat);
    	dat[2] = 2;
    	dat[3] = BT_OPT_GET;
    	dat[4] = BT_CMD_OPEN_TIME;
    	dat[5] = xorCheck(dat,5);
    	mConnectedThread.write(dat);
    }
    public void setOpenTime(Handler handler,int time,int add) {
    	setOpenTimeHandler = handler;
    	byte[] dat = new byte[8];
    	fillBtHeader(dat);
    	dat[2] = 4;
    	dat[3] = BT_OPT_SET;
    	dat[4] = BT_CMD_OPEN_TIME;
    	dat[5] = (byte)time;
    	dat[6] = (byte)add;
    	dat[7] = xorCheck(dat,7);
    	mConnectedThread.write(dat);
    }
    public void getOpenLog(Handler handler) {
    	getOpenLogHandler = handler;
    	byte[] dat = new byte[8];
    	fillBtHeader(dat);
    	dat[2] = 2;
    	dat[3] = BT_OPT_GET;
    	dat[4] = BT_CMD_OPEN_LOG;
    	dat[5] = xorCheck(dat,5);
    	mConnectedThread.write(dat);
    }
    public void restore(Handler handler) {
    	restoreHandler = handler;
    	byte[] dat = new byte[8];
    	fillBtHeader(dat);
    	dat[2] = 2;
    	dat[3] = BT_OPT_SET;
    	dat[4] = BT_CMD_RESTORE;
    	dat[5] = xorCheck(dat,5);
    	mConnectedThread.write(dat);
    }
    
    
    
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                            MY_UUID_SECURE);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(
                            MY_UUID_INSECURE);
                }
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            setName("ConnectThread" + mSocketType);
            mAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                	
                }
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (this) {
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            	
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
//                    mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
                	//restart
                    break;
                }
            }
        }
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
//
//                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

}
