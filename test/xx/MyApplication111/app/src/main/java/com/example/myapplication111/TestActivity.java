package com.example.myapplication111;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // 接收端代码
        Intent intent = getIntent();
        if (intent != null) {
            String value = intent.getStringExtra("key"); // 获取String类型参数
            // 处理接收到的参数
            // 在TextView中显示接收到的值
            TextView textView = findViewById(R.id.mytestview); // 请将yourTextViewId替换为你的TextView的ID
            textView.setText(value);
        }


    }
}
