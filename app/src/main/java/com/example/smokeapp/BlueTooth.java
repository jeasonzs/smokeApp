package com.example.smokeapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.UUID;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
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
	public static final byte BT_CMD_OPEN_TIME = 0x01;
    public static final byte BT_CMD_OPEN_LOG = 0x02;
    public static final byte BT_CMD_OPEN_NEXT = 0x03;
    public static final byte BT_CMD_RESTORE = 0x04;
    public static final byte BT_OPT_SET = (byte) 0xb2;
    public static final byte BT_OPT_GET = (byte) 0xb1;
    
    public static final String PREF_KEY_DEVICE_NAME_AND_ADDR= "prefKeyDeviceNameAndAddr";
	private final BluetoothAdapter mAdapter;
	private final static String TAG = "smoke";
	private BluetoothGatt mBluetoothGatt;
	
	private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
	
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    
    private Handler optHandler;

    
    private String deviceNameAndAddr; 
    private byte[] datSend;
    
    private OpenLog openLog;
    
    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    
	private BlueTooth() {
		openLog = new OpenLog();
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		deviceNameAndAddr = utils.getPrefString(PREF_KEY_DEVICE_NAME_AND_ADDR);
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
    
    public synchronized void setDeviceNameAndAddr(String deviceAndAddr) {
    	this.deviceNameAndAddr = deviceAndAddr;
    	utils.setPrefString(PREF_KEY_DEVICE_NAME_AND_ADDR, deviceAndAddr);
    }
    
    public synchronized String getDeviceNameAndAddr() {
    	return utils.getPrefString(PREF_KEY_DEVICE_NAME_AND_ADDR);
    }
    

    private synchronized void sendData() {
    	if(deviceNameAndAddr == null || mAdapter == null) {
    		return;
    	}	
    	if(mConnectedThread == null) {
    		String address = deviceNameAndAddr.substring(deviceNameAndAddr.length() - 17);
        	BluetoothDevice device = mAdapter.getRemoteDevice(address);
//	        mConnectThread = new ConnectThread(device, true);
//	        mConnectThread.start();
        	mBluetoothGatt = device.connectGatt(null, false, mGattCallback);
    	}
    	else {
    		write(datSend);
    	}
    }
    
    
    
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                intentAction = ACTION_GATT_CONNECTED;
//                mConnectionState = STATE_CONNECTED;
//                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                intentAction = ACTION_GATT_DISCONNECTED;
//                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
//                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
               // broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            	List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();
            	for (BluetoothGattService gattService : gattServices) {
            		String uuid= gattService.getUuid().toString();
            		//Log.w(TAG, "gattService: " + uuid);
            		if(uuid.equals("0000ffe0-0000-1000-8000-00805f9b34fb")) {
            			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            				uuid =  gattCharacteristic.getUuid().toString();
            				Log.w(TAG, "gattCharacteristic: " + uuid);
            				mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
            			}
            		}
            		
            		if(uuid.equals("0000ffe5-0000-1000-8000-00805f9b34fb")) {
            			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            			for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
            				uuid =  gattCharacteristic.getUuid().toString();
            				Log.w(TAG, "gattCharacteristic: " + uuid);
            				gattCharacteristic.setValue(datSend);
            				mBluetoothGatt.writeCharacteristic(gattCharacteristic);
            			}
            		}

            	}
            	
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
               // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            	Log.w(TAG, "onCharacteristicRead received: " + status);
            }
            Log.w(TAG, "onCharacteristicRead received: " + status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
           // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        	Log.w(TAG, "onCharacteristicChanged received: ");
        }
        @Override
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
        	Log.w(TAG, "onCharacteristicWrite: " + status+","+BluetoothGatt.GATT_SUCCESS);
        }
    };

    
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, final String socketType) {
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();
    }
    

    private byte xorCheck(byte[] dat) {
		byte ret = 0;
		for (int i = 3; i < dat.length-1; i++) {
			ret^=dat[i]; 
		}
		return ret;
	}
    
    private void fillBtHeader(byte[] dat) {
		dat[0] = (byte) 0xeb;
		dat[1] = (byte) 0xa5;
	}
    
    public void getNextOpenTime(Handler handler) {
    	optHandler = handler;
    	datSend = new byte[6];
    	fillBtHeader(datSend);
    	datSend[2] = 2;
    	datSend[3] = BT_OPT_GET;
    	datSend[4] = BT_CMD_OPEN_NEXT;
    	datSend[5] = xorCheck(datSend);
    	sendData();
    	
    }
//    public void getOpenTime(Handler handler) {
//    	openTimeHandler = handler;
//    	byte[] dat = new byte[6];
//    	fillBtHeader(dat);
//    	dat[2] = 2;
//    	dat[3] = BT_OPT_GET;
//    	dat[4] = BT_CMD_OPEN_TIME;
//    	dat[5] = xorCheck(dat);
//    	mConnectedThread.write(dat);
//    }
//    public void setOpenTime(Handler handler,int time,int add) {
//    	openTimeHandler = handler;
//    	byte[] dat = new byte[8];
//    	fillBtHeader(dat);
//    	dat[2] = 4;
//    	dat[3] = BT_OPT_SET;
//    	dat[4] = BT_CMD_OPEN_TIME;
//    	dat[5] = (byte)time;
//    	dat[6] = (byte)add;
//    	dat[7] = xorCheck(dat);
//    	mConnectedThread.write(dat);
//    }
    public void getOpenLog(Handler handler) {
    	optHandler = handler;
    	datSend = new byte[6];
    	fillBtHeader(datSend);
    	datSend[2] = 2;
    	datSend[3] = BT_OPT_GET;
    	datSend[4] = BT_CMD_OPEN_LOG;
    	datSend[5] = xorCheck(datSend);
    	sendData();
    	openLog.randTest(90, 2048);
    	//optHandler.obtainMessage(BT_CMD_OPEN_NEXT, dat[5], 0).sendToTarget();
    	optHandler.obtainMessage(BT_CMD_OPEN_LOG, openLog).sendToTarget();
    }
//    public void restore(Handler handler) {
//    	restoreHandler = handler;
//    	byte[] dat = new byte[8];
//    	fillBtHeader(dat);
//    	dat[2] = 2;
//    	dat[3] = BT_OPT_SET;
//    	dat[4] = BT_CMD_RESTORE;
//    	dat[5] = xorCheck(dat);
//    	mConnectedThread.write(dat);
//    }
    
    
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
//            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
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
            	return;
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
            connected(mmSocket, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            
            }
        }
    }

    private void handlePkg(byte[] dat,int len) {
    	switch(dat[4]) {
    	case BT_CMD_OPEN_NEXT:
    		if(optHandler != null) {
    			optHandler.obtainMessage(BT_CMD_OPEN_NEXT, dat[5], 0).sendToTarget();
    		}
    		break;
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
            write(datSend);
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    handlePkg(buffer,bytes);
                    break;
                } catch (IOException e) {
                	//restart
                    break;
                }
            }
            cancel();
            mConnectedThread = null;
        }
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
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
