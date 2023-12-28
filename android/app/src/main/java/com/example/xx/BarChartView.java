package com.example.xx;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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

        // 自定义渐变颜色
        int startColor = applyAlphaToColor("#D7843C", 90); // 上半部分颜色，80是透明度
        int endColor = applyAlphaToColor("#2E6C1D", 90);   // 下半部分颜色，80是透明度
        Shader shader = new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // 初始化数据
        data = new double[] {2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
    }

    private int applyAlphaToColor(String colorCode, int alpha) {
        int color = Color.parseColor(colorCode);
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    // 设置数据的方法，通过 API 调用获取数据
    public void setDataFromApi(@NotNull String apiUrl) {
        Log.d("api!", apiUrl);
        apiHandler.fetchData(apiUrl, "get", responseBody -> {
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

    private double[] parseResponseData(String responseBody) {
        Log.d("123456",responseBody);
        try {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray resultArray = responseJson.getJSONArray("result");

            double[] result = new double[resultArray.length()];
            for (int i = 0; i < resultArray.length(); i++) {
                result[i] = resultArray.getDouble(i);
            }

            // 打印获取的数据
            Log.d("BarChartView", "Parsed result data: " + Arrays.toString(result));

            // 格式化数据，保留一位小数
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            for (int i = 0; i < result.length; i++) {
                result[i] = Double.parseDouble(decimalFormat.format(result[i]));
            }

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            // 在实际应用中，你可能需要记录日志或进行其他错误处理操作
            // 这里返回自定义的数据，你可以根据需要进行修改
            Log.e("BarChartView", "Error parsing result data. Returning default data.");
            return new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置渐变效果
        int startColor = applyAlphaToColor("#D7843C", 90); // 上半部分颜色，80是透明度
        int endColor = applyAlphaToColor("#2E6C1D", 90);   // 下半部分颜色，80是透明度
        Shader shader = new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // 如果数据为空，则不进行绘制
        if (data == null || data.length == 0) {
            Log.d("BarChartData", "data == 0");
            return;
        }

        // 绘制柱状图和标注数据值
        float barWidth = getWidth() / (2 * data.length + 1); // 计算柱状图的宽度
        float maxData = getMaxData(data); // 获取数据中的最大值

        for (int i = 0; i < data.length; i++) {
            float barHeight = (float) data[i] / maxData * getHeight(); // 计算柱状图的高度
            float startX = (2 * i + 1) * barWidth; // 计算柱状图的起始 x 坐标
            float startY = getHeight() - barHeight; // 计算柱状图的起始 y 坐标
            float endX = startX + barWidth; // 计算柱状图的结束 x 坐标
            float endY = getHeight(); // 计算柱状图的结束 y 坐标

            // 绘制柱状图
            canvas.drawRect(startX, startY, endX, endY, paint);

            // 在柱状图上方绘制数据值
            String dataValue = String.valueOf(data[i]);
            float textX = startX + barWidth / 2; // 文本的 x 坐标为柱状图的中心点
            float textY = startY + 25; // 文本的 y 坐标，偏移一些距离以避免与柱状图重叠
            paint.setColor(Color.BLACK); // 设置文本颜色为白色
            paint.setTextSize(20); // 设置文本大小

            // 如果文本超出 View 的上边界，调整文本的位置
            if (textY < 0) {
                textY = 30; // 设置一个合适的偏移值，确保文本在 View 内部可见
            }

            canvas.drawText(dataValue, textX, textY, paint);

            // 恢复画笔颜色
            paint.setColor(Color.BLACK);
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
