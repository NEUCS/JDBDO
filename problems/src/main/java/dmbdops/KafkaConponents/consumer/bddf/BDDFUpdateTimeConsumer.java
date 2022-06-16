package dmbdops.KafkaConponents.consumer.bddf;

import dmbdops.bddf.BDDF;
import dmbdops.dbigopt2015.updateTimeByKafka.AbstractDBigOpt2015UpdateTime;
import individual.Individual;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import problem.DynamicTestProblem;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class BDDFUpdateTimeConsumer implements
        dmbdops.KafkaConponents.consumer.KafkaConsumer {
    KafkaConsumer<String,String> consumer;
    List<Double> timeList;
    String topic;
    Properties properties;
    BDDF problem;

    public BDDFUpdateTimeConsumer(Properties properties, String topic, BDDF problem){
        this.topic = topic;
        this.properties = properties;
        this.consumer = new KafkaConsumer<>(this.properties);
        this.consumer.subscribe(Collections.singleton(this.topic));
        this.timeList = new LinkedList<>();
        this.problem = problem;
    }
    @Override
    public void run() {
        while (true){
            ConsumerRecords<String,String> consumerRecords = consumer.poll(
                    Duration.ofMillis(500));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                if (!timeList.contains(Double.parseDouble(consumerRecord.value()))){
                    timeList.add(Double.parseDouble(consumerRecord.value()));
                    problem.setTime(Double.parseDouble(consumerRecord.value()));
                }
            }
            consumer.commitSync();
        }
    }
}