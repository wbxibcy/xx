package com.example.xx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final ApiHandler apiHandler;
    private String uidParam;;

    // 无参数构造函数是必需的
    public HomeActivity() {
        this.apiHandler = new ApiHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent getintent = getIntent();
        uidParam = getintent.getStringExtra("uid");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // 处理首页点击事件
                return true;
            } else if (item.getItemId() == R.id.navigation_device) {
                // 发送端代码
                Intent intent = new Intent(this, DeviceActivity.class);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.navigation_my) {
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("uid", uidParam); // 使用 putExtra() 传递参数
                startActivity(intent);
                // 处理点击事件
                return true;
            } else {
                return false;
            }
        });

        // 手动设置选中第二个项
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // 初始化 BarChartView
        BarChartView barChartView = findViewById(R.id.BarChartView);

        // 调用 API 并更新 BarChartView 的数据
        String apiUrl = "http://47.100.125.229:8000/ts";
        String urlWithParams = apiUrl + "?uid=" + uidParam;

        String LogUrl = "http://47.100.125.229:8000/batteries-advices";
        String LogurlWithParams = LogUrl + "?uid=" + uidParam;

        barChartView.setDataFromApi(urlWithParams);

        apiHandler.fetchData(urlWithParams, "get", responseBody -> {
            double[] newData = preparseResponseData(responseBody);

            // 设置 TextView 的文本为 newData 的第一个值
            if (newData.length > 0) {
                double firstValue = newData[0];
                updateTextViewText(firstValue);
            }
        });

        Log.d("mydata", urlWithParams);

        apiHandler.fetchData(LogurlWithParams, "get", responseBody -> {
            List<String> AdviceData = LogparseResponseData(responseBody);

            // 获取包裹 TextView 的容器布局
            LinearLayout containerLayout = findViewById(R.id.containerLayout);

            for (String text : AdviceData) {
                // 创建新的 TextView
                TextView textView = new TextView(this);

                // 设置 TextView 的属性
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setPadding(8, 8, 8, 8);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                textView.setText(text);

                // 将 TextView 添加到容器布局中
                containerLayout.addView(textView);
            }
        });
    }

    private List<String> LogparseResponseData(String responseBody) {
        Log.d("123", responseBody);
        try {
            JSONArray jsonArray = new JSONArray(responseBody);

            List<String> logData = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject logObject = jsonArray.getJSONObject(i);
                String logText = logObject.getString("text");
                logData.add(logText);
                Log.e("mylog", logText);
            }

            return logData;
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("preErr", "Error parsing result data. Returning default data.");
            List<String> NormalData = new ArrayList<>();
            NormalData.add("目前为止, 一切安好O.O");
            NormalData.add("我觉得不需要任何建议o.o");
            NormalData.add("真嘟假嘟? o.O");

            return NormalData;
        }
    }

    private void updateTextViewText(double newTextValue) {
        // 获取 TextView
        TextView textView = findViewById(R.id.textView);

        // 设置 TextView 的文本为新值
        textView.setText(String.valueOf(newTextValue));
    }

    private double[] preparseResponseData(String responseBody) {
        Log.d("123", responseBody);
        try {
            JSONObject responseJson = new JSONObject(responseBody);
            double prediction = responseJson.getDouble("prediction");

            // 使用 DecimalFormat 将预测值格式化为两位小数
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedPrediction = decimalFormat.format(prediction);

            // 打印获取的数据
            Log.d("pre", "Parsed result data: " + Double.parseDouble(formattedPrediction));

            // 返回一个包含预测值的数组
            return new double[]{Double.parseDouble(formattedPrediction)};
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("preErr", "Error parsing result data. Returning default data.");
            return new double[]{1.0}; // 返回默认数据
        }
    }

}
