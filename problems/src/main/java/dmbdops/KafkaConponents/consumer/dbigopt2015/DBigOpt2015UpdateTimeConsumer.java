package dmbdops.KafkaConponents.consumer.dbigopt2015;

import dmbdops.dbigopt2015.updateTimeByKafka.AbstractDBigOpt2015UpdateTime;
import individual.ContinuousIndividual;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import problem.DynamicTestProblem;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DBigOpt2015UpdateTimeConsumer implements
        dmbdops.KafkaConponents.consumer.KafkaConsumer {
    KafkaConsumer<String,String> consumer;
    List<Double> timeList;
    String topic;
    Properties properties;
    AbstractDBigOpt2015UpdateTime problem;

    public DBigOpt2015UpdateTimeConsumer(Properties properties, String topic,  AbstractDBigOpt2015UpdateTime problem){
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
                    Duration.ofMillis(30000));
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