import numpy as np
from pmdarima import auto_arima

def time_series_prediction(time_series, order=(0, 1, 1)):
    """
    使用ARIMA模型进行时间序列预测

    参数:
    - time_series: 输入的时间序列数据（list或numpy数组）
    - order: ARIMA模型的阶数, 格式为 (p, d, q)

    返回:
    - 预测的下一个数据值
    """

    # 将时间序列数据转换为numpy数组
    time_series_np = np.array(time_series)

    # 使用指定的pdq值拟合ARIMA模型
    model = auto_arima(time_series_np, order=order, suppress_warnings=True)
    model.fit(time_series_np)

    # 预测下一个数据
    next_data_pred = model.predict(n_periods=1)

    # result = time_series.append(next_data_pred[0])
    # print(result)

    return next_data_pred[0]

# 例子
time_series = [-0.0714116, 0.0222752, 0.146227, 0.457576, 0.359557, 0.337603, 0.548104, 0.508198, 0.774135, 0.930303, 0.892596, 0.931638, 1.19324, 1.20328, 1.19736, 1.12241, 0.979497, 1.29053, 1.13859, 1.16603]
# pdq = (1, 1, 1)

next_data = time_series_prediction(time_series)
print("预测的下一个数据:", next_data)
