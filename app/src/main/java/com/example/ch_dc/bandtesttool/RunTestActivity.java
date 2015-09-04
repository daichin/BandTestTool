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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class RunTestActivity extends AppCompatActivity {
    private ArrayAdapter<String> mArrayAdapter = null;
    private ListView m_listview_test_function = null;

    String[] base_function_name = {"Function 1", "Function 2", "Function 3", "Function 4", "Function 5", "Function 6", "Function 7"} ;
    Integer[] imageId = {R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_test);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_DEVICE_NAME);
        setTitle(message);
        CustomList adapter = new CustomList(RunTestActivity.this, base_function_name, imageId);

        //mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        m_listview_test_function = (ListView)findViewById(R.id.listView_test_function);
        m_listview_test_function.setAdapter(adapter);
        m_listview_test_function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RunTestActivity.this, "You Clicked at " + base_function_name[+position], Toast.LENGTH_SHORT).show();
                ImageView imageView = (ImageView) view.findViewById(R.id.img);
                imageView.setImageResource(R.drawable.check);
            }
        });
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
