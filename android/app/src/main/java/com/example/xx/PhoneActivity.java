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

import java.util.Random;

public class PhoneActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;
    private String generatedVerificationCode;

    public PhoneActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        String apiUrl = "http://47.100.125.229:8000/login/phone-number";

        EditText editTextPhone = findViewById(R.id.editTextPhone);
        EditText editTextCode = findViewById(R.id.editTextCode);

        Button loginButton = findViewById(R.id.anotherButton);
        loginButton.setOnClickListener(v -> {
            // Get the account and password from EditText fields when the login button is clicked
            String phone = editTextPhone.getText().toString();
            String code = editTextCode.getText().toString();

            // Check if the verification code is not empty
            if (code.isEmpty()) {
                showToast("请输入验证码");
                return;
            }

            // Compare entered code with the generated code
            if (!isVerificationCodeValid(code)) {
                showToast("验证码错误");
                return;
            }

            // Create JSON object and add account and password
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("phone", phone);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Convert JSON object to a string
            String jsonString = jsonRequest.toString();

            // Make a network request to the login API
            apiHandler.fetchData(apiUrl, "post", jsonString, responseBody -> {
                try {
                    String uid = PhoneLoginData(responseBody);

                    // Check if the 'uid' is not empty
                    if (!uid.isEmpty()) {
                        // Start HomeActivity with the 'id' value as an extra
                        startHomeActivity(uid);
                    } else {
                        // Log a message or handle the case where 'uid' is empty
                        Log.e("PhoneActivity", "ID is empty or not found");
                    }
                } catch (JSONException e) {
                    // Handle JSON parsing error
                    Log.e("PhoneActivity", "Error parsing JSON: " + e.getMessage());
                }
            });
        });

        Button CodeButton = findViewById(R.id.buttonForgotPassword);
        CodeButton.setOnClickListener(v -> {
            // 获取手机号
            String phone = editTextPhone.getText().toString();

            // 检查手机号是否为空
            if (!phone.isEmpty()) {
                // 发送请求获取验证码
                sendVerificationCode(phone);
            } else {
                // 提示用户输入手机号
                showToast("请输入手机号");
            }
        });

        Button phoneLoginButton = findViewById(R.id.myButton); // 请根据你的实际按钮ID修改
        phoneLoginButton.setOnClickListener(v -> {
            // 创建一个 Intent 对象，从当前 Activity 跳转到 LoginActivity
            Intent intent = new Intent(PhoneActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean isVerificationCodeValid(String enteredCode) {
        // Compare entered code with the generated code
        return enteredCode.equals(generatedVerificationCode);
    }

    private void sendVerificationCode(String phone) {
        // Generate a random 4-digit verification code (you can customize the length)
        generatedVerificationCode = generateRandomCode(4);

        // Show the verification code in a popup or toast message
        showToast("Verification Code: " + generatedVerificationCode);
    }

    private String generateRandomCode(int length) {
        // Generate a random verification code of the specified length
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // append a random digit (0-9)
        }

        return code.toString();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String PhoneLoginData(String responseBody) throws JSONException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            // Check if "id" exists in the response
            if (jsonResponse.has("id")) {
                // Get the value of "id"
                return jsonResponse.getString("id");
            } else {
                // Handle the case where "id" is not present in the response
                Log.e("LoginActivity", "ID not found in the response");
                return ""; // or return null, depending on your requirements
            }

        } catch (JSONException e) {
            Log.d("ApiResponse", "响应体: " + responseBody);
            return "";
        }
    }

    private void startHomeActivity(String id) {
        // Start HomeActivity with the 'id' value as an extra
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);

        // Optionally, you can finish the LoginActivity to prevent going back to it when pressing the back button
        finish();
    }
}