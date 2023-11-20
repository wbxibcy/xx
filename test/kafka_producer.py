from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers='localhost:9092')

# 发送消息到指定主题
producer.send('my_topic', value='Hello, Kafka from WSL!')

# 关闭生产者
producer.close()

