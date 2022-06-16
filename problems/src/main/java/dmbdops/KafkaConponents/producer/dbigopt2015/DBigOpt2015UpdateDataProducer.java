package dmbdops.KafkaConponents.producer.dbigopt2015;

import com.google.gson.Gson;
import dmbdops.dbigopt2015.updateByFile.DBigOpt2015UpdateDataByKafka;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;

public class DBigOpt2015UpdateDataProducer implements
        dmbdops.KafkaConponents.producer.KafkaProducer {
    private Properties properties;
    private String topic;
    private Producer<String, String> producer;
    private DBigOpt2015UpdateDataByKafka problem;
    private String problemId;
    private final int tauT = 60;
    private final int nT = 10;


    public DBigOpt2015UpdateDataProducer(String topic,
            Properties kafkaProperties, DBigOpt2015UpdateDataByKafka problem,
            String problemId) {
        this.topic = topic;
        this.properties = kafkaProperties;
        this.producer = new KafkaProducer<>(properties);
        this.problem = problem;
        this.problemId = problemId;
    }


    @Override
    public void run() {
        int counter = 0;
        problem.loadData(problemId, "x.txt", 4, 256);
        problem.loadData(problemId, "S.txt", 4, 256);
        List<List<Double>> S =
                new ArrayList<>(problem.getIcaComponent().size());
        List<List<Double>> X = new ArrayList<>(problem.getMixed().size());
        for (int i = 0; i < problem.getIcaComponent().size(); i++) {
            for (double a : problem.getIcaComponent().get(i)) {
                S.add(new ArrayList<>());
                S.get(i).add(new Double(a));
            }
        }
        for (int i = 0; i < problem.getMixed().size(); i++) {
            for (double a : problem.getMixed().get(i)) {
                X.add(new ArrayList<>());
                X.get(i).add(new Double(a));
            }
        }

        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double time =
                    (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
            double pro = Math.abs(Math.sin(0.5 * Math.PI * time));
            List<List<Double>> s = new ArrayList<>(S.size());
            List<List<Double>> x = new ArrayList<>(X.size());
            for (int i = 0; i < S.size(); i++) {
                for (double a : S.get(i)) {
                    s.add(new ArrayList<>());
                    s.get(i).add(a * pro);
                }
            }
            for (int i = 0; i < X.size(); i++) {
                for (double a : X.get(i)) {
                    x.add(new ArrayList<>());
                    x.get(i).add(a * pro);
                }
            }
            Gson json = new Gson();
            Map<String, List<List<Double>>> map = new HashMap<>();
            map.put("X", x);
            map.put("S", s);
            Object j = json.toJson(map);
            String str = j.toString();
            producer.send(new ProducerRecord<String, String>(this.topic,
                                                             "updateDataSet",
                                                             str));

        }
    }
}