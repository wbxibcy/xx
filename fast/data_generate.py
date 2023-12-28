import numpy as np
import time
import random
from sqlalchemy import create_engine, Column, Float, Integer, String, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship
from sqlalchemy.sql import func
from models import *


Base = declarative_base()


def generate_power_signal(time_points, frequency, amplitude, noise_strength):
    sine_wave = amplitude * np.sin(2 * np.pi * frequency * time_points)
    noise = noise_strength * np.random.normal(size=len(time_points))
    power_signal = sine_wave + noise
    return power_signal

def save_to_database(session, time_points, power_signal):
    # 设置用户ID
    user_id_to_associate = random.choice([1, 2, 3])

    for timestamp, value in zip(time_points, power_signal):
        # 插入数据到 ts 表
        ts_instance = TS(data=abs(value), user_id=user_id_to_associate)
        # ts_instance = TS(data=abs(value))
        session.add(ts_instance)

    # 提交更改
    session.commit()


def generate_and_save_to_database():
    # 连接到 MySQL 数据库
    engine = create_engine("mysql+mysqlconnector://root:powerful@47.100.125.229/xx")

    Session = sessionmaker(bind=engine)
    session = Session()

    try:
        while True:
            # 生成新的时间序列数据
            time_points = np.linspace(0, 1, np.random.randint(50, 150))

            # 设置信号参数
            frequency = np.random.uniform(49, 51)
            amplitude = np.random.uniform(0.8, 1.2)
            noise_strength = 0.1

            # 生成电力信号
            power_signal = generate_power_signal(time_points, frequency, amplitude, noise_strength)

            # 保存到数据库
            save_to_database(session, time_points, power_signal)

            time.sleep(1)  # 添加一秒的延迟

            print('好欸!!!')

    except KeyboardInterrupt:
        # 关闭数据库连接
        session.close()

if __name__ == "__main__":
    generate_and_save_to_database()
