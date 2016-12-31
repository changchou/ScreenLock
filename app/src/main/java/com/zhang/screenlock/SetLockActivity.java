package com.zhang.screenlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class SetLockActivity extends AppCompatActivity {

    private List<Integer> pointsNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lock);

//        SharedPreferences sp = getSharedPreferences("GestureLock", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences("GestureLock", Context.MODE_PRIVATE).edit();

        final GestureLock lock = (GestureLock) findViewById(R.id.lock_view);
        Button btnReset = (Button) findViewById(R.id.btn_reset);
        Button btnSave = (Button) findViewById(R.id.btn_save);

        lock.setOnDrawFinishedListener(new GestureLock.OnDrawFinishedListener() {
            @Override
            public boolean onDrawFinished(List<Integer> pointsNum) {
                if (pointsNum.size() < 3) {
                    Toast.makeText(SetLockActivity.this, "密码不能少于3个点", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    SetLockActivity.this.pointsNum = pointsNum;
                    return true;
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock.resetPoints();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointsNum != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Integer i : pointsNum) {
                        sb.append(i);
                    }
                    editor.putString("password", sb.toString());
                    if (editor.commit()) {
                        Toast.makeText(SetLockActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
