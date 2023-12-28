package com.example.xx;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;
    private String uidParam;

    // 无参数构造函数是必需的
    public UserActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_person);

        Intent getintent = getIntent();
        uidParam = getintent.getStringExtra("uid");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_user);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_device) {
                // 发送端代码
                Intent intent = new Intent(this, DeviceActivity.class);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_my) {
                return true;
            } else {
                return false;
            }
        });

        // 手动设置选中第二个项
        bottomNavigationView.setSelectedItemId(R.id.navigation_my);

        String userUrl = "http://47.100.125.229:8000/me";
        String urlWithParams = userUrl + "?uid=" + uidParam;

        apiHandler.fetchData(urlWithParams, "get", responseBody -> {
            try {
                // 解析 JSON 数据
                JSONObject userJson = new JSONObject(responseBody);

                // 提取用户信息
                String name = userJson.getString("name");
                String sex = userJson.getString("sex");
                String account = userJson.getString("account");
                String nickname = userJson.getString("nickname");
                String phone = userJson.getString("phone");

                // 在 UI 上更新用户信息
                updateUserUI(name, sex, account, nickname, phone);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("UserActivity", "Error parsing user data");
            }
        });

        Log.d("mydata", urlWithParams);

    }

    private void updateUserUI(String name, String sex, String account, String nickname, String phone) {
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView sexTextView = findViewById(R.id.sexTextView);
        TextView accountTextView = findViewById(R.id.accountTextView);
        TextView nicknameTextView = findViewById(R.id.nicknameTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);

        nameTextView.setText(name);
        sexTextView.setText(sex);
        accountTextView.setText(account);
        nicknameTextView.setText(nickname);
        phoneTextView.setText(phone);
    }
}
