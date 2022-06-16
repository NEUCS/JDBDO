package dmbdops.KafkaConponents.producer.bdfda;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class BDFDAUpdateTimeProducer implements
        dmbdops.KafkaConponents.producer.KafkaProducer {
    private final int tauT = 5;
    private final int nT = 10;
    private Properties properties;
    private String topic;
    private Producer<String, String> producer;

    public BDFDAUpdateTimeProducer(String topic,
            Properties kafkaProperties) {
        this.topic = topic;
        this.properties = kafkaProperties;
        this.producer = new KafkaProducer<>(properties);
    }


    @Override
    public void run() {
        int counter = 0;
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double time =
                    (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
            producer.send(
                    new ProducerRecord<String, String>(this.topic, "updateTime",
                                                       String.valueOf(time)));
            counter++;
        }
    }
}