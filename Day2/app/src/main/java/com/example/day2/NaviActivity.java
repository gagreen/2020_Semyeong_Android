package com.example.day2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.day2.databinding.ActivityNaviBinding;

public class NaviActivity extends AppCompatActivity {

    ActivityNaviBinding b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_navi);

        b.btnMainActivity.setOnClickListener(new View.OnClickListener() { // 메인 액티비티로 가는
            @Override
            public void onClick(View view) {

            }
        });

        b.btnNaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://naver.com"));
                startActivity(i);

            }
        });

        b.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
