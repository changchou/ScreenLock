package com.zhang.screenlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTestLock(View view) {
        startActivity(new Intent(this,TestLockActivity.class));
    }

    public void onClickSetLock(View view) {
        startActivity(new Intent(this,SetLockActivity.class));
    }
}
