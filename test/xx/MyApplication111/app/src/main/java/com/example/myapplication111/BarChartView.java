package com.example.myapplication111;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class BarChartView extends View {

    private Paint paint;
    private double[] data; // 用于存储柱状图数据
    private final ApiHandler apiHandler;

    public BarChartView(Context context) {
        super(context);
        apiHandler = new ApiHandler();
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        apiHandler = new ApiHandler();
        init();
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apiHandler = new ApiHandler();
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);

        // 初始化数据
        data = new double[] {2.0, 3.0, 4.0, 2.0};
    }

    // 设置数据的方法，通过 API 调用获取数据
    public void setDataFromApi(@NotNull String apiUrl) {
        Log.d("api!", apiUrl);
        apiHandler.fetchData(apiUrl, responseBody -> {
            // 这里需要根据你的API响应的数据格式来解析
            double[] newData = parseResponseData(responseBody);

            // 设置新的数据并刷新视图
            setData(newData);
        });
    }

    // 在 BarChartView 中添加该方法，用于设置新的数据并刷新视图
    public void setData(double[] newData) {
        // 设置数据
        data = newData;

        // 请求成功后，刷新视图
        postInvalidate();
    }


    // 解析 API 响应的数据，这里需要根据你的API响应的数据格式来实现
//    private double[] parseResponseData(String responseBody) {
//        try {
//            JSONObject jsonObject = new JSONObject(responseBody);
//            JSONArray resultArray = jsonObject.getJSONArray("result");
//
//            double[] result = new double[resultArray.length()];
//            for (int i = 0; i < resultArray.length(); i++) {
//                result[i] = resultArray.getDouble(i);
//            }
//
//            return result;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            // 在实际应用中，你可能需要记录日志或进行其他错误处理操作
//            // 这里返回自定义的数据，你可以根据需要进行修改
//            return new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
//        }
//    }

    private double[] parseResponseData(String responseBody) {
        Log.d("123456",responseBody);
        try {
            JSONArray resultArray = new JSONArray(responseBody);

            double[] result = new double[resultArray.length()];
            for (int i = 0; i < resultArray.length(); i++) {
                result[i] = resultArray.getDouble(i);
            }

            // 打印获取的数据
            Log.d("BarChartView", "Parsed result data: " + Arrays.toString(result));

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            // 在实际应用中，你可能需要记录日志或进行其他错误处理操作
            // 这里返回自定义的数据，你可以根据需要进行修改
            Log.e("BarChartView", "Error parsing result data. Returning default data.");
            return new double[]{6.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 如果数据为空，则不进行绘制
        if (data == null || data.length == 0) {
            Log.d("BarChartData", "data == 0");
            return;
        }

        // 绘制柱状图
        float barWidth = getWidth() / (2 * data.length + 1); // 计算柱状图的宽度
        float maxData = getMaxData(data); // 获取数据中的最大值

        for (int i = 0; i < data.length; i++) {
            float barHeight = (float) data[i] / maxData * getHeight(); // 计算柱状图的高度
            float startX = (2 * i + 1) * barWidth; // 计算柱状图的起始 x 坐标
            float startY = getHeight() - barHeight; // 计算柱状图的起始 y 坐标
            float endX = startX + barWidth; // 计算柱状图的结束 x 坐标
            float endY = getHeight(); // 计算柱状图的结束 y 坐标

            canvas.drawRect(startX, startY, endX, endY, paint);
        }
    }


    private int getMaxData(double[] data) {
        double max = data[0];
        for (double value : data) {
            if (value > max) {
                max = value;
            }
        }
        return (int) max;
    }
}
