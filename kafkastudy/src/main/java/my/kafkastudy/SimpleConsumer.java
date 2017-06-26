package my.kafkastudy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleConsumer {

	private static Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		List<PartitionInfo> partitionInfos = consumer.partitionsFor("mytopic");
		List<TopicPartition> topicPartitions = new ArrayList<TopicPartition>();
		for (PartitionInfo partitionInfo : partitionInfos) {
			topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
		}
		consumer.assign(topicPartitions);
		consumer.seekToBeginning(topicPartitions);

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				logger.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
			}
		}
	}
}
