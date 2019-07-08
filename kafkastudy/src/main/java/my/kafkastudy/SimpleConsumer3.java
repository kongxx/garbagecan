package my.kafkastudy;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleConsumer3 {

	private static Logger logger = LoggerFactory.getLogger(SimpleConsumer3.class);

	public static void main(String[] args) throws Exception {
		for (int i = 0; i< 3; i++) {
			final String group = "group_" + i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						consumeByGroup(group);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		}
	}
	
	public static void consumeByGroup(String group) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", group);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		consumer.subscribe(Arrays.asList("mytopic"));

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
			for (ConsumerRecord<String, String> record : records) {
				logger.info("group = {}, offset = {}, key = {}, value = {}", group, record.offset(), record.key(), record.value());
			}
		}
	}

}
