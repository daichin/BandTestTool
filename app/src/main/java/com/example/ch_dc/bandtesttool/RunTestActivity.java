package com.example.ch_dc.bandtesttool;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RunTestActivity extends AppCompatActivity {
    private ArrayAdapter<String> mArrayAdapter = null;
    private ListView m_listview_test_function = null;
    public final static String EXTRA_FUNCTION_NAME = "function_name";
    public final static String EXTRA_FUNCTION_DETAIL = "function_detail";


    String[] base_function_name = {"Function 1", "Function 2", "Function 3", "Function 4", "Function 5", "Function 6", "Function 7"} ;
    Integer[] imageId = {R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank, R.drawable.blank};
    String[] base_function_detail = {"Detail 1", "Detail 2", "Detail 3", "Detail 4", "Detail 5", "Detail 6", "Detail 7"};
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

                TextView textView = (TextView) view.findViewById(R.id.txt);
                String name = textView.getText().toString();
                String detail = base_function_detail[+position];

                Intent intent = new Intent();
                intent.putExtra(EXTRA_FUNCTION_NAME, name);
                intent.putExtra(EXTRA_FUNCTION_DETAIL, detail);
                intent.setClass(RunTestActivity.this, FunctionDetailActivity.class);
                startActivity(intent);
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
        switch (item.getItemId()) {
            case R.id.menu_open_script:
                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/json/");
                if (directory.exists()) {
                    File[] dirFiles = directory.listFiles();
                    if (dirFiles.length != 0) {
                        for (int index = 0 ; index < dirFiles.length ; index++) {
                            if (dirFiles[index].getName().toString().equals("default_test.json")) {
                                // open this file
                                File file = new File(directory, "default_test.json");
                                StringBuilder text = new StringBuilder();
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        text.append(line);
                                    }
                                    // json parse
                                    ArrayList<String> mFunctionDetailList = new ArrayList<String>();
                                    ArrayList<String> mFunctionNameList = new ArrayList<String>();
                                    ArrayList<Integer> mIntegerList = new ArrayList<Integer>();
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(text.toString());

                                        //Get the instance of JSONArray that contains JSONObjects
                                        JSONArray jsonArray = jsonRootObject.optJSONArray("items");

                                        //Iterate the jsonArray and print the info of JSONObjects
                                        for(int i=0; i < jsonArray.length(); i++){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name = jsonObject.optString("name").toString();
                                            String send_data = jsonObject.optString("send_data").toString();
                                            mFunctionNameList.add(name);
                                            mIntegerList.add(R.drawable.blank);
                                            mFunctionDetailList.add(send_data);
                                        }
                                        base_function_name = new String[0];
                                        base_function_name = mFunctionNameList.toArray(base_function_name);
                                        imageId = new Integer[0];
                                        imageId = mIntegerList.toArray(imageId);
                                        base_function_detail = new String[0];
                                        base_function_detail = mFunctionDetailList.toArray(base_function_detail);
                                        CustomList adapter = new CustomList(RunTestActivity.this, base_function_name, imageId);

                                        m_listview_test_function = (ListView)findViewById(R.id.listView_test_function);
                                        m_listview_test_function.setAdapter(adapter);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        }
                    }
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
