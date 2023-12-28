package com.example.myapplication111;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 BarChartView
        BarChartView barChartView = findViewById(R.id.BarChartView);

        // 在这里调用 API 并更新 BarChartView 的数据
        String apiUrl = "http://47.100.125.229:8000/ts";
        String uidParam = "6";
        String urlWithParams = apiUrl + "?uid=" + uidParam;
        barChartView.setDataFromApi(urlWithParams);

        Log.d("mydata", "!!!!!!" + urlWithParams);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // 处理首页点击事件
                return true;
            } else if (item.getItemId() == R.id.navigation_device) {
                // 发送端代码
                Intent intent = new Intent(this, TestActivity.class);
                intent.putExtra("key", "value"); // 使用putExtra()传递参数
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_my) {
                // 处理我的点击事件
                return true;
            } else {
                return false;
            }

        });
    }
}
