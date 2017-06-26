package my.kafkastudy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleProducer {

	private static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");
		props.put("request.required.acks", "1");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		for(int i = 0; i < 100; i++) {
			ProducerRecord record = new ProducerRecord<String, String>("mytopic", Integer.toString(i), Integer.toString(i));
			logger.info("send (key = {}, value = {})", record.key(), record.value());
			producer.send(record);
			Thread.sleep(1000);
		}

		producer.close();
	}
}