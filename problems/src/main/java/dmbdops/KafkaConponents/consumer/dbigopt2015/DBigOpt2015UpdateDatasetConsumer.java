package dmbdops.KafkaConponents.consumer.dbigopt2015;

import com.google.gson.Gson;
import dmbdops.dbigopt2015.updateByFile.DBigOpt2015UpdateDataByKafka;
import dmbdops.dbigopt2015.updateTimeByKafka.AbstractDBigOpt2015UpdateTime;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.*;

public class DBigOpt2015UpdateDatasetConsumer implements
        dmbdops.KafkaConponents.consumer.KafkaConsumer {
    KafkaConsumer<String,String> consumer;
    List<String> datasetList;
    String topic;
    Properties properties;
    DBigOpt2015UpdateDataByKafka problem;

    public DBigOpt2015UpdateDatasetConsumer(Properties properties, String topic, DBigOpt2015UpdateDataByKafka problem){
        this.topic = topic;
        this.properties = properties;
        this.consumer = new KafkaConsumer<>(this.properties);
        this.consumer.subscribe(Collections.singleton(this.topic));
        this.datasetList = new LinkedList<>();
        this.problem = problem;
    }
    @Override
    public void run() {
        Gson gson = new Gson();
        while (true){
            ConsumerRecords<String,String> consumerRecords = consumer.poll(
                    Duration.ofMillis(30000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                if (!datasetList.contains(consumerRecord.value())){
                    datasetList.add(consumerRecord.value());
                    Map<String, List<List<Double>>> map =
                            (Map<String, List<List<Double>>>) gson
                                    .fromJson(consumerRecord.value(), Map.class);
                    problem.setIcaComponent(map.get("S"));
                    problem.setMixed(map.get("X"));
                    problem.problemHasBeenModified();
                }
            }
            consumer.commitSync();
        }
    }
}