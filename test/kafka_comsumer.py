from kafka import KafkaConsumer

consumer = KafkaConsumer('my_topic', bootstrap_servers='localhost:9092', group_id='my_group')

# 循环读取消息
for message in consumer:
    print(f"Received message: {message.value.decode('utf-8')}")

# 关闭消费者
consumer.close()
