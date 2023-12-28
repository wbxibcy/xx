package com.example.xx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceEditActivity extends AppCompatActivity {
    private final ApiHandler apiHandler;
    private String deviceId;
    private String deviceName;
    public DeviceEditActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit);

        deviceId = getIntent().getStringExtra("deviceId");

        deviceName = getIntent().getStringExtra("devicename");

        String apiUrl = "http://47.100.125.229:8000/devices";

        EditText editTextDeviceName = findViewById(R.id.devicename);
        EditText editTextCreatedTime = findViewById(R.id.createdtime);
        EditText editTextEnergy = findViewById(R.id.energy);

        Button cancelButton = findViewById(R.id.myButton);
        Button confirmButton = findViewById(R.id.anotherButton);

        cancelButton.setOnClickListener(v -> {
            // 处理取消按钮点击，例如返回到上一个活动
            finish();
        });

        confirmButton.setOnClickListener(v -> {
            // 从 EditText 字段获取设备名称、创建时间和能量信息
//            String deviceName = editTextDeviceName.getText().toString();
            String createdTime = editTextCreatedTime.getText().toString();
            String energy = editTextEnergy.getText().toString();

            // 验证输入字段
            if (createdTime.isEmpty() || energy.isEmpty()) {
                showToast("请填写所有字段");
                return;
            }

            // 创建 JSON 对象并添加设备信息
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("did", deviceId);
                jsonRequest.put("name", deviceName);
                jsonRequest.put("created_time", createdTime);
                jsonRequest.put("energy", energy);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // 转换 JSON 对象为字符串
            String jsonString = jsonRequest.toString();

            // 发送网络请求到更新设备信息的 API
            apiHandler.fetchData(apiUrl, "put", jsonString, responseBody -> {
                Log.d("API Response", responseBody);
                try {
                    // 处理 API 响应
                    handleUpdateDeviceResponse(responseBody);
                } catch (JSONException e) {
                    Log.d("更新设备信息失败", e.getMessage());
                    showToast("更新设备信息失败：" + e.getMessage());
                }
            });
        });
    }

    private void handleUpdateDeviceResponse(String responseBody) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseBody);

        if (jsonResponse.has("status")) {
            int status = jsonResponse.getInt("status");

            if (status == 200) {
                showToast("设备信息已成功更新");

                // 返回到上一个活动（设备详情页面），同时携带设备名称
                returnToDeviceDetailActivity(deviceName);
            } else {
                showToast("更新设备信息失败：" + jsonResponse.optString("message", "未知错误"));
            }
        } else {
            showToast("更新设备信息失败：" + jsonResponse.optString("message", "未知错误"));
        }
    }

    private void returnToDeviceDetailActivity(String deviceName) {
        // 创建一个 Intent 以导航到 DeviceDetailActivity
        Intent intent = new Intent(this, DeviceDetailActivity.class);

        // 将设备名称作为额外的信息放入Intent
        intent.putExtra("deviceName", deviceName);

        // 启动 DeviceDetailActivity 并结束当前的 UpdateDeviceActivity
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}