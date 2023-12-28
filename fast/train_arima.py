import numpy as np
import matplotlib.pyplot as plt
from pmdarima import auto_arima

# 输入的时间序列数据
time_series = [-0.0714116, 0.0222752, 0.146227, 0.457576, 0.359557, 0.337603, 0.548104, 0.508198, 0.774135, 0.930303, 0.892596, 0.931638, 1.19324, 1.20328, 1.19736, 1.12241, 0.979497, 1.29053, 1.13859, 1.16603]

# 将时间序列数据转换为numpy数组
time_series_np = np.array(time_series)

# 使用auto_arima函数自动选择ARIMA模型的参数
model = auto_arima(time_series_np, suppress_warnings=True)

# 获取ARIMA模型的参数
order = model.get_params()['order']

# 拟合ARIMA模型
model.fit(time_series_np)

# 预测下一个数据
next_data_pred = model.predict(n_periods=1)

print("自动选择的ARIMA模型参数:", order)
print("预测的下一个数据:", next_data_pred[0])

# # 绘制原始数据和预测结果
# plt.plot(range(len(time_series)), time_series, label='原始数据')
# plt.scatter(len(time_series), next_data_pred[0], color='red', label='预测的下一个数据')
# plt.xlabel('时间步')
# plt.ylabel('数值')
# plt.legend()
# plt.show()
