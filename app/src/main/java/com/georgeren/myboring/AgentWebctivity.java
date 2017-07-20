package com.georgeren.myboring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class AgentWebctivity extends AppCompatActivity {
    private static final String TAG = "AgentWebctivity";
    private LinearLayout webRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_webctivity);
        webRoot = (LinearLayout) findViewById(R.id.webRoot);
    }
}
