package com.zhang.screenlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

public class TestLockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lock);

        SharedPreferences sp = getSharedPreferences("GestureLock", Context.MODE_PRIVATE);

        final String password = sp.getString("password", "");

        GestureLock lock = (GestureLock) findViewById(R.id.lock_view);
        lock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean onDrawFinished(List<Integer> pointsNum) {
                StringBuilder sb = new StringBuilder();
                for (Integer i : pointsNum){
                    sb.append(i);
                }
                if (sb.toString().equals(password)){
                    Toast.makeText(TestLockActivity.this,"正确", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(TestLockActivity.this,"错误", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });


    }
}
