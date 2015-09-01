package com.example.ch_dc.bandtesttool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RunTestActivity extends AppCompatActivity {
    private ArrayAdapter<String> mArrayAdapter = null;
    private ListView m_listview_test_function = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_test);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_DEVICE_NAME);
        setTitle(message);


        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        m_listview_test_function = (ListView)findViewById(R.id.listView_test_function);
        m_listview_test_function.setAdapter(mArrayAdapter);
        m_listview_test_function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mArrayAdapter.add("Function 1");
        mArrayAdapter.add("Function 2");
        mArrayAdapter.add("Function 3");
        mArrayAdapter.add("Function 4");
        mArrayAdapter.add("Function 5");
        mArrayAdapter.add("Function 6");
        mArrayAdapter.add("Function 7");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run_test, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        // your code.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_open_script) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
