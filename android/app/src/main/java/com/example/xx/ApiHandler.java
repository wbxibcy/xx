package com.example.xx;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHandler {

    private final OkHttpClient client;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public ApiHandler() {
        client = new OkHttpClient();
    }

//    public void fetchData(@NotNull String apiUrl, @NotNull OnDataFetchedListener listener) {
//        Log.d("api last", apiUrl);
//        // 创建 GET 请求
//        Request request = new Request.Builder()
//                .url(apiUrl)
//                .get()  // 使用GET方法
//                .build();
//
//        // 执行异步请求
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                // 处理请求失败的情况
//                handler.post(() -> listener.onDataFetched("网络请求失败: " + e.getMessage()));
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    // 获取响应体
//                    String responseBody = response.body().string();
//
//                    // Log the response body for debugging
//                    Log.d("ApiResponse", "Response body: " + responseBody);
//
//                    // 在主线程中回调数据
//                    handler.post(() -> listener.onDataFetched(responseBody));
//                } else {
//                    // 处理请求失败的情况
//                    handler.post(() -> listener.onDataFetched("请求失败，响应码: " + response.code()));
//                }
//
//                // 关闭响应体
//                response.close();
//            }
//        });
//    }

    public void fetchData(@NotNull String apiUrl, @NotNull String method, @NotNull OnDataFetchedListener listener) {
        fetchData(apiUrl, method, null, listener);
    }

    public void fetchData(@NotNull String apiUrl, @NotNull String method, String jsonData, @NotNull OnDataFetchedListener listener) {
        Log.d("api last", apiUrl);

        Request.Builder requestBuilder = new Request.Builder()
                .url(apiUrl);

        // Check the HTTP method and add the appropriate method-specific configurations
        if ("GET".equalsIgnoreCase(method)) {
            requestBuilder.get();
        } else if ("POST".equalsIgnoreCase(method)) {
            // Add POST-specific configurations
            RequestBody requestBody = RequestBody.create(jsonData, MediaType.parse("application/json"));
            requestBuilder.post(requestBody);
        } else if ("PUT".equalsIgnoreCase(method)) {
            // Add PUT-specific configurations
            RequestBody requestBody = RequestBody.create(jsonData, MediaType.parse("application/json"));
            requestBuilder.put(requestBody);
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // Add DELETE-specific configurations
            RequestBody requestBody = RequestBody.create(jsonData, MediaType.parse("application/json"));
            requestBuilder.delete(requestBody);
        }

        Request request = requestBuilder.build();

        // Execute asynchronous request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle request failure
                handler.post(() -> listener.onDataFetched("Network request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Get the response body
                    String responseBody = response.body().string();

                    // Log the response body for debugging
                    Log.d("ApiResponse", "Response body: " + responseBody);

                    // Callback data on the main thread
                    handler.post(() -> listener.onDataFetched(responseBody));
                } else {
                    // Handle request failure
                    handler.post(() -> listener.onDataFetched("Request failed, response code: " + response.code()));
                }

                // Close the response body
                response.close();
            }
        });
    }



    // 定义接口，用于回调数据获取结果
    public interface OnDataFetchedListener {
        void onDataFetched(String data);
    }
}
