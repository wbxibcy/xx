package com.example.xx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;
    private String uidParam;
    private LinearLayout deviceListLayout; // 设备名称列表的父级布局

    public DeviceActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        Intent getintent = getIntent();
        uidParam = getintent.getStringExtra("uid");

        Log.d("uid!!", uidParam);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_device);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // 处理首页点击事件
                Intent intent = new Intent(this, HomeActivity.class);
                Log.d("uid!!!", uidParam);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_device) {
                return true;
            } else if (item.getItemId() == R.id.navigation_my) {
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });

        // 手动设置选中第二个项
        bottomNavigationView.setSelectedItemId(R.id.navigation_device);

        // 获取设备列表的父级布局
        deviceListLayout = findViewById(R.id.device_list_layout);

        // 调用 API 并更新设备名称列表
        String apiUrl = "http://47.100.125.229:8000/dashboard";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("uid", uidParam);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Convert JSON object to a string
        String jsonString = jsonRequest.toString();

        apiHandler.fetchData(apiUrl, "post", jsonString, responseBody -> {
            try {
                // 假设响应是一个包含 "name" 字段的对象数组
                JSONArray dataArray = new JSONArray(responseBody);

                // 获取设备列表的父级布局
                LinearLayout deviceListLayout = findViewById(R.id.device_list_layout);

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    String deviceName = dataObject.getString("name");

                    // 创建新的 TextView
                    TextView deviceTextView = new TextView(this);

                    // 设置 TextView 的长宽
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            260 // 设置高度
                    );
                    deviceTextView.setLayoutParams(params);

//                    deviceTextView.setLayoutParams(new ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                    ));

                    deviceTextView.setTextSize(25); // 字体稍微大
                    deviceTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // 偏左边
                    deviceTextView.setPadding(32, 0, 0, 0); // 左边留出一些空白
                    deviceTextView.setText(deviceName);
                    deviceTextView.setTextColor(getResources().getColor(R.color.black)); // 黑色
                    deviceTextView.setTypeface(deviceTextView.getTypeface(), Typeface.BOLD); // 加粗

                    // 为TextView添加点击事件
                    deviceTextView.setOnClickListener(v -> {
                        Intent detailIntent = new Intent(this, DeviceDetailActivity.class);
                        detailIntent.putExtra("deviceName", deviceName);
                        startActivity(detailIntent);
                    });

                    // 创建圆角矩形背景
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setCornerRadius(30); // 设置圆角半径
                    drawable.setColor(Color.parseColor("#60FFFFFF"));
                    deviceTextView.setBackground(drawable);

                    // 将 TextView 添加到父级布局中
                    deviceListLayout.addView(deviceTextView);

                    // 在每个矩形之间添加一些空隙
                    View spaceView = new View(this);
                    spaceView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            16 // 空隙的高度，可以根据需要调整
                    ));
                    deviceListLayout.addView(spaceView);
                }
            } catch (JSONException e) {
                Log.e("DeviceActivity", "JSON 解析错误: " + e.getMessage());
            }
        });

    }
}
