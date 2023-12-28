package com.example.xx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceDetailActivity extends AppCompatActivity {
    private final ApiHandler apiHandler;

    private String deviceName;

    public DeviceDetailActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        // 获取传递的参数
        deviceName = getIntent().getStringExtra("deviceName");

        String userUrl = "http://47.100.125.229:8000/devices";
        String urlWithParams = userUrl + "?dname=" + deviceName;

        apiHandler.fetchData(urlWithParams, "get", responseBody -> {
            try {
                // 解析 JSON 数据
                JSONObject deviceJson = new JSONObject(responseBody);

                // 提取信息
                String deviceId = deviceJson.getString("id");
                String name = deviceJson.getString("name");
                String time = deviceJson.getString("created_time");
                String energy = deviceJson.getString("energy");

                // 在 UI 上更新用户信息
                updateDeviceUI(name, time, energy);

                // 获取更新信息的按钮
                Button updateButton = findViewById(R.id.myButton);

                // 为按钮添加点击事件
                updateButton.setOnClickListener(v -> {
                    // 创建一个新的 Intent
                    Intent updateIntent = new Intent(DeviceDetailActivity.this, DeviceEditActivity.class);
                    // 将设备的 ID 通过 Intent 传递到下一个 Activity
                    updateIntent.putExtra("deviceId", deviceId);
                    updateIntent.putExtra("devicename", deviceName);
                    // 启动下一个 Activity
                    startActivity(updateIntent);
                });

                // 获取返回按钮
                ImageButton returnButton = findViewById(R.id.returnbutton);

                // 为返回按钮添加点击事件
                returnButton.setOnClickListener(v -> {
                    // 关闭当前 Activity，返回上一个 Activity
                    finish();
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("UserActivity", "Error parsing user data");
            }
        });
    }

    private void updateDeviceUI(String name, String time, String energy) {
        TextView nameTextView = findViewById(R.id.devicename);
        TextView timeTextView = findViewById(R.id.createdtime);
        TextView energyTextView = findViewById(R.id.energy);

        nameTextView.setText(name);
        timeTextView.setText(time);
        energyTextView.setText(energy);
    }
}
