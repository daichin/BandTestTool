package com.example.ch_dc.bandtesttool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class FunctionDetailActivity extends Activity
{


    private TextView m_listview_sent_data_detail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_function_detail);
        setResult(Activity.RESULT_CANCELED);

        Intent intent = getIntent();
        String message = intent.getStringExtra(RunTestActivity.EXTRA_FUNCTION_NAME);
        setTitle(message);
        String detail = intent.getStringExtra(RunTestActivity.EXTRA_FUNCTION_DETAIL);
        m_listview_sent_data_detail = (TextView) findViewById(R.id.title_sent_data_detail);
        m_listview_sent_data_detail.setText(detail);
    }
}
