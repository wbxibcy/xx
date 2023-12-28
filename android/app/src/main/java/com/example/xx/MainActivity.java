package com.example.xx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 接收端代码
        Intent intent = getIntent();
        if (intent != null) {
            String value = intent.getStringExtra("key"); // 获取String类型参数
            // 处理接收到的参数
            // 在TextView中显示接收到的值
            TextView textView = findViewById(R.id.test);
            textView.setText(value);
        }

    }
}
