package dmbdops.KafkaConponents.producer.bdfda;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import random.MRandom;

import java.util.Properties;

public class BDFDARandomUpdateTimeProducer implements
        dmbdops.KafkaConponents.producer.KafkaProducer {
    private Properties properties;
    private String topic;
    private Producer<String, String> producer;

    public BDFDARandomUpdateTimeProducer(String topic,
            Properties kafkaProperties) {
        this.topic = topic;
        this.properties = kafkaProperties;
        this.producer = new KafkaProducer<>(properties);
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double time = MRandom.getInstance().nextDouble(0.0, 4.0);
            producer.send(
                    new ProducerRecord<String, String>(this.topic, "updateTime",
                                                       String.valueOf(time)));
        }
    }
}