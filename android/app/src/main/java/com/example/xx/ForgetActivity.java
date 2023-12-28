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

public class ForgetActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;

    public ForgetActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        String apiUrl = "http://47.100.125.229:8000/login";

        EditText editTextAccount = findViewById(R.id.editTextzhanghao);
        EditText editTextNewPassword = findViewById(R.id.editTextnew);
        EditText editTextConfirmPassword = findViewById(R.id.editTextqrmm);

        Button cancelButton = findViewById(R.id.myButton);
        Button confirmButton = findViewById(R.id.anotherButton);

        cancelButton.setOnClickListener(v -> {
            // Handle cancel button click, e.g., go back to the previous activity
            finish();
        });

        confirmButton.setOnClickListener(v -> {
            // Get account, new password, and confirm password from EditText fields
            String account = editTextAccount.getText().toString();
            String newPassword = editTextNewPassword.getText().toString();
            String confirmPassword = editTextConfirmPassword.getText().toString();

            // Validate input fields
            if (account.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showToast("请填写所有字段");
                return;
            }

            // Check if new password and confirm password match
            if (!newPassword.equals(confirmPassword)) {
                showToast("新密码和确认密码不匹配");
                return;
            }

            // Create JSON object and add account and new password
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("account", account);
                jsonRequest.put("newpassword", newPassword);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Convert JSON object to a string
            String jsonString = jsonRequest.toString();

            // Make a network request to the forgot password API
            apiHandler.fetchData(apiUrl, "put", jsonString, responseBody -> {
                try {
                    // Process the API response as needed
                    handleForgetPasswordResponse(responseBody);
                } catch (JSONException e) {
                    // Handle JSON parsing error
                    Log.d("忘记密码失败" , e.getMessage());
                    showToast("忘记密码失败：" + e.getMessage());
                }
            });
        });
    }

    private void handleForgetPasswordResponse(String responseBody) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Check the response status or any other relevant information
        // You can customize this part based on your API response structure
        if (!jsonResponse.has("status")) {
            String newpassword = jsonResponse.getString("password");

            showToast("密码已重置, 新密码为: " + newpassword);

            // Return to the account login screen
            returnToLoginActivity();

        } else {
            showToast("忘记密码失败：" + jsonResponse.optString("message", "未知错误"));
        }
    }

    private void returnToLoginActivity() {
        // Create an Intent to navigate to the LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);

        // You may add additional flags or extras to the intent if needed

        // Start the LoginActivity and finish the current ForgetActivity
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
