package com.example.qpdjg.a2018_seoulapp_owner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.qpdjg.a2018_seoulapp_owner.Activitys.LoginActivity;

public class splash extends AppCompatActivity {

    ImageView splash_image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.parseColor("#321c54"));
        }


        splash_image_view = (ImageView)findViewById(R.id.splash_image);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(splash_image_view);
        Glide.with(this).load(R.drawable.splash).into(imageViewTarget);

    }
    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), LoginActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            splash.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }
    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}
