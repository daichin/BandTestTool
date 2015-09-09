package com.example.ch_dc.bandtesttool;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class FunctionDetailActivity extends Activity {


    private TextView m_listview_sent_data_detail = null;
    //private Handler m_Handler = new Handler();
    //private Handler mThreadHandler;
    //private HandlerThread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_function_detail);
        setResult(Activity.RESULT_CANCELED);

        Intent intent = getIntent();
        String message = intent.getStringExtra(RunTestActivity.EXTRA_FUNCTION_NAME);
        setTitle(message);
        String detail = intent.getStringExtra(RunTestActivity.EXTRA_FUNCTION_DETAIL);
        m_listview_sent_data_detail = (TextView)findViewById(R.id.title_sent_data_detail);
        m_listview_sent_data_detail.setText(detail);

        // create thread for every function
/*        mThread = new HandlerThread("function_test");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        mThreadHandler.post(Test);*/
    }
/*
    @Override
    protected void onDestroy() {
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacks(Test);
        }

        if (mThread != null){
            mThread.quit();
        }

        super.onDestroy();
    }

    private Runnable Test = new Runnable() {
        public void run() {
        }
    };*/
}
