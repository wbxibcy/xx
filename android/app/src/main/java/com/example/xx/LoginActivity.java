package com.example.xx;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;

    public LoginActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String apiUrl = "http://47.100.125.229:8000/login/account-password";

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.anotherButton);
        loginButton.setOnClickListener(v -> {

            // Get the account and password from EditText fields when the login button is clicked
            String account = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            // Create JSON object and add account and password
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("account", account);
                jsonRequest.put("password", password);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Convert JSON object to a string
            String jsonString = jsonRequest.toString();

            // Make a network request to the login API
            apiHandler.fetchData(apiUrl, "post", jsonString, responseBody -> {
                try {
                    String uid = LoginData(responseBody);

                    // Check if the 'uid' is not empty
                    if (!uid.isEmpty()) {
                        // Start HomeActivity with the 'id' value as an extra
                        startHomeActivity(uid);
                    } else {
                        // Log a message or handle the case where 'uid' is empty
                        Log.e("LoginActivity", "ID is empty or not found");
                    }
                } catch (JSONException e) {
                    // Handle JSON parsing error
                    Log.e("LoginActivity", "Error parsing JSON: " + e.getMessage());
                }
            });
        });

        Button phoneLoginButton = findViewById(R.id.myButton); // 请根据你的实际按钮ID修改
        phoneLoginButton.setOnClickListener(v -> {
            // 创建一个 Intent 对象，从当前 Activity 跳转到 PhoneActivity
            Intent intent = new Intent(LoginActivity.this, PhoneActivity.class);
            startActivity(intent);
        });

        Button forgotPasswordButton = findViewById(R.id.buttonForgotPassword); // 请根据你的实际按钮ID修改
        forgotPasswordButton.setOnClickListener(v -> {
            // 创建一个 Intent 对象，从当前 Activity 跳转到 ForgetActivity
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        });
    }


    private String LoginData(String responseBody) throws JSONException {
        try {JSONObject jsonResponse = new JSONObject(responseBody);
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