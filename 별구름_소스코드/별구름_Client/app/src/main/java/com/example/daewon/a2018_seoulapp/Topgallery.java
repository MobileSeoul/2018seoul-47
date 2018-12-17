package com.example.daewon.a2018_seoulapp;

import android.os.Bundle;
import android.widget.Toast;

import com.example.daewon.a2018_seoulapp.Activity.BaseActivity;

public class Topgallery extends BaseActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topgallery);

    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
