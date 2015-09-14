package com.example.ch_dc.bandtesttool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "BandTestTool";
    private ArrayAdapter<String> mArrayAdapter = null;
    private ListView m_listview_bt_device = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice mDevice;
    public final static String EXTRA_DEVICE_NAME = "device_name";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcast, filter);
        Context context = getApplicationContext();
        String yourFilePath = context.getFilesDir() + "/" + "hello.txt";

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        m_listview_bt_device = (ListView) findViewById(R.id.listView_device_list);
        m_listview_bt_device.setAdapter(mArrayAdapter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        m_listview_bt_device.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String info = mArrayAdapter.getItem(position);
                String address = info.substring(info.length() - 17);
                String name = info.substring(0, (info.length() - 17));
                Log.d(TAG, "Server MAC: " + info);
                mDevice = mBluetoothAdapter.getRemoteDevice(address);
                Common myApp = Common.getInstance();
                myApp.create_socket(mDevice);
                mBluetoothAdapter.cancelDiscovery();
                if (myApp.connect_sock())
                {
                    // start the function testing page
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DEVICE_NAME, name);
                    intent.setClass(MainActivity.this, RunTestActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0)
        {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices)
            {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else
        {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String name = info.substring(0, (info.length() - 17));
            String address = info.substring(info.length() - 17);

            mDevice = mBluetoothAdapter.getRemoteDevice(address);
            Common myApp = Common.getInstance();
            myApp.create_socket(mDevice);
            mBluetoothAdapter.cancelDiscovery();
            if (myApp.connect_sock())
            {
                // start the function testing page
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_NAME, name);
                intent.setClass(MainActivity.this, RunTestActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.menu_bt_scan:
                // start bt scan
                startScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BroadcastReceiver mBroadcast = new BroadcastReceiver()
    {
        private final static String MY_MESSAGE = "com.givemepass.sendmessage";

        @Override
        public void onReceive(Context mContext, Intent intent)
        {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null && device.getName().substring(0, 3).contentEquals("CB_"))
                {
                    for (int i = 0; i < mArrayAdapter.getCount(); i++)
                    {
                        String item = mArrayAdapter.getItem(i);

                        if (item.equals(device.getName() + "\n" + device.getAddress()))
                        {
                            return;
                        }
                    }
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

    private void startScan()
    {
        mBluetoothAdapter.cancelDiscovery();
        mArrayAdapter.clear();
        mBluetoothAdapter.startDiscovery();
    }

}
