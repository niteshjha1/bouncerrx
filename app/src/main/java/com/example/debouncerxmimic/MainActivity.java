package com.example.debouncerxmimic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    public static final String LOG_TAG = "DebounceExecutorTest.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    DebounceExecutor debounceExecutor = new DebounceExecutor(null);
                    DebounceExecutor.InnerClass innerClass = debounceExecutor.new InnerClass();
                    innerClass.bounceBounce();
                } catch (InterruptedException ex) {
                    Log.e(LOG_TAG, ex.toString());
                }
            }
        }).start();
    }
}