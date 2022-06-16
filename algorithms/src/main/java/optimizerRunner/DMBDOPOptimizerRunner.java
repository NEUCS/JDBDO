package optimizerRunner;

import Visualizer.Visualizer;
import algorithm.DynamicMetaheuristic;
import comparators.DominanceComparator;
import dmbdops.KafkaConponents.consumer.KafkaConsumer;
import dmbdops.KafkaConponents.consumer.bddf.BDDFUpdateTimeConsumer;
import dmbdops.KafkaConponents.consumer.bdfda.BDFDAUpdateTimeConsumer;
import dmbdops.KafkaConponents.consumer.dbigopt2015.DBigOpt2015UpdateDatasetConsumer;
import dmbdops.KafkaConponents.consumer.dbigopt2015.DBigOpt2015UpdateTimeConsumer;
import dmbdops.KafkaConponents.producer.KafkaProducer;
import dmbdops.KafkaConponents.producer.bddf.BDDFUpdateTimeProducer;
import dmbdops.KafkaConponents.producer.bdfda.BDFDAUpdateTimeProducer;
import dmbdops.KafkaConponents.producer.dbigopt2015.DBigOpt2015UpdateDataProducer;
import dmbdops.KafkaConponents.producer.dbigopt2015.DBigOpt2015UpdateTimeProducer;
import dmbdops.bddf.*;
import dmbdops.bdfda.BDFDA;
import dmbdops.bdfda.BDFDA1;
import dmbdops.bdfda.BDFDA2;
import dmbdops.bdfda.BDFDA3;
import dmbdops.dbigopt2015.updateByFile.DBigOpt2015UpdateDataByKafka;
import dmbdops.dbigopt2015.updateTimeByKafka.AbstractDBigOpt2015UpdateTime;
import dmbdops.dbigopt2015.updateTimeByKafka.DBigOpt2015UpdateTime;
import dynamicMetaheuristic.nagaii.DynamicNSGAII;
import individual.ContinuousIndividual;
import operators.crossover.Crossover;
import operators.crossover.DECrossover;
import operators.crossover.SBXCrossover;
import operators.mutation.Mutation;
import operators.mutation.PolynomiaMutation;
import operators.selection.Selection;
import operators.selection.TournamentSelection;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import population.Population;
import population.evaluator.ContinuousPopulationEvaluator;
import population.evaluator.PopulationEvaluator;
import problem.DynamicTestProblem;

import java.io.IOException;
import java.util.Properties;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/14 15:42
 */
public class DMBDOPOptimizerRunner {
    private int populationSize;
    private DynamicTestProblem<ContinuousIndividual> problem;
    DynamicMetaheuristic<Population<ContinuousIndividual>> metaheuristic;
    private Crossover<ContinuousIndividual> crossover;
    private operators.crossover.DECrossover DECrossover;
    private Mutation<ContinuousIndividual> mutation;
    private Selection<Population<ContinuousIndividual>, ContinuousIndividual>
            selection;
    private PopulationEvaluator<ContinuousIndividual> populationEvaluator;
    private int limitedNumberOfEvaluationsOrIterations;
    private Visualizer visualizer;
    private KafkaProducer producer;
    private KafkaConsumer consumer;
    private String topic;
    private Properties kafkaProducerProperty;
    private Properties kafkaConsumerProperty;

    public DMBDOPOptimizerRunner() throws IOException {
        this.populationSize = 100;
        this.limitedNumberOfEvaluationsOrIterations = 25000;
        this.problem = new DBigOpt2015UpdateDataByKafka("D4");
        this.crossover = new SBXCrossover(1.0, 20.0);
        this.DECrossover = new DECrossover(1.0, 0.5);
        this.mutation =
                new PolynomiaMutation(1.0 / problem.getNumberOfVariables(), 20);
        this.selection = new TournamentSelection<ContinuousIndividual>(
                new DominanceComparator<ContinuousIndividual>(), 2);
        this.populationEvaluator = new ContinuousPopulationEvaluator<>();
        this.metaheuristic =
                new DynamicNSGAII<>(problem, 25000, 100, 100, 100, crossover,
                                    mutation, selection,
                                    new DominanceComparator<>(),
                                    new ContinuousPopulationEvaluator<ContinuousIndividual>(),
                                    10);
        this.kafkaProducerProperty = new Properties();
        this.kafkaProducerProperty.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                       "Hadoop1:9092");
        this.kafkaProducerProperty.put(ProducerConfig.CLIENT_ID_CONFIG, "Producer");
        this.kafkaProducerProperty.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization" +
                               ".StringSerializer");
        this.kafkaProducerProperty.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                       "org.apache.kafka.common.serialization" +
                               ".StringSerializer");

        this.kafkaConsumerProperty = new Properties();
        this.kafkaConsumerProperty.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "Hadoop1:9092");
        this.kafkaConsumerProperty.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.kafkaConsumerProperty.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.kafkaConsumerProperty.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        this.kafkaConsumerProperty.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        this.producer =
                new DBigOpt2015UpdateDataProducer(topic, kafkaProducerProperty,
                                                  (DBigOpt2015UpdateDataByKafka) problem,
                                                  "D4");
        this.consumer =
                new DBigOpt2015UpdateDatasetConsumer(kafkaConsumerProperty,
                                                     topic,
                                                     (DBigOpt2015UpdateDataByKafka) problem);
    }

    public DMBDOPOptimizerRunner setPopulationSize(int populationSize) {
        if (populationSize < 0) {
            throw new RuntimeException("Population size can not be negative!");
        } else if (populationSize > 300) {
            throw new RuntimeException("Population size is too large!");
        } else if (populationSize == 0) {
            throw new RuntimeException("Population size can not be zero!");
        }
        this.populationSize = populationSize;
        return this;
    }

    public DMBDOPOptimizerRunner setLimitedNumberOfEvaluationsOrIterations(
            int limitedNumberOfEvaluationsOrIterations) {
        if (populationSize < 0) {
            throw new RuntimeException(
                    "Number of evaluations or iterations can not be negative!");
        } else if (populationSize == 0) {
            throw new RuntimeException(
                    "Number of evaluations or iterations can not be zero!");
        }
        this.limitedNumberOfEvaluationsOrIterations =
                limitedNumberOfEvaluationsOrIterations;
        return this;
    }

    public DMBDOPOptimizerRunner setProblem(String problemName) {
        if (problemName.equals("BDDF1")) {
            this.problem = new BDDF1();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF2")) {
            this.problem = new BDDF2();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF3")) {
            this.problem = new BDDF3();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF4")) {
            this.problem = new BDDF4();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF5")) {
            this.problem = new BDDF5();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF6")) {
            this.problem = new BDDF6();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF7")) {
            this.problem = new BDDF7();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF8")) {
            this.problem = new BDDF8();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDDF9")) {
            this.problem = new BDDF9();
            this.producer = new BDDFUpdateTimeProducer(this.topic,
                                                       this.kafkaProducerProperty);
            this.consumer =
                    new BDDFUpdateTimeConsumer(this.kafkaConsumerProperty,
                                               this.topic, (BDDF) this.problem);
        } else if (problemName.equals("BDFDA1")) {
            this.problem = new BDFDA1();
            this.producer = new BDFDAUpdateTimeProducer(this.topic,
                                                        this.kafkaProducerProperty);
            this.consumer =
                    new BDFDAUpdateTimeConsumer(this.kafkaConsumerProperty,
                                                this.topic,
                                                (BDFDA) this.problem);
        } else if (problemName.equals("BDFDA2")) {
            this.problem = new BDFDA2();
            this.producer = new BDFDAUpdateTimeProducer(this.topic,
                                                        this.kafkaProducerProperty);
            this.consumer =
                    new BDFDAUpdateTimeConsumer(this.kafkaConsumerProperty,
                                                this.topic,
                                                (BDFDA) this.problem);
        } else if (problemName.equals("BDFDA3")) {
            this.problem = new BDFDA3();
            this.producer = new BDFDAUpdateTimeProducer(this.topic,
                                                        this.kafkaProducerProperty);
            this.consumer =
                    new BDFDAUpdateTimeConsumer(this.kafkaConsumerProperty,
                                                this.topic,
                                                (BDFDA) this.problem);
        } else if (problemName.equals("D4")) {
            this.problem = new DBigOpt2015UpdateTime("D4");
            this.producer = new DBigOpt2015UpdateTimeProducer(this.topic,
                                                              this.kafkaProducerProperty);
            this.consumer = new DBigOpt2015UpdateTimeConsumer(
                    this.kafkaConsumerProperty, this.topic,
                    (AbstractDBigOpt2015UpdateTime) this.problem);
        } else if (problemName.equals("D4N")) {
            this.problem = new DBigOpt2015UpdateTime("D4N");
            this.producer = new DBigOpt2015UpdateTimeProducer(this.topic,
                                                              this.kafkaProducerProperty);
            this.consumer = new DBigOpt2015UpdateTimeConsumer(
                    this.kafkaConsumerProperty, this.topic,
                    (AbstractDBigOpt2015UpdateTime) this.problem);
        } else {
            throw new RuntimeException(
                    "Framework does not contain this problem!");
        }
        return this;
    }

    public DMBDOPOptimizerRunner setCrossover(
            Crossover<ContinuousIndividual> crossover) {
        this.crossover = crossover;
        return this;
    }

    public DMBDOPOptimizerRunner setDECrossover(DECrossover deCrossover) {
        this.DECrossover = deCrossover;
        return this;
    }

    public DMBDOPOptimizerRunner setMutation(
            Mutation<ContinuousIndividual> mutation) {
        this.mutation = mutation;
        return this;
    }

    public DMBDOPOptimizerRunner setPopulationEvaluator(
            PopulationEvaluator<ContinuousIndividual> populationEvaluator) {
        this.populationEvaluator = populationEvaluator;
        return this;
    }

    public DMBDOPOptimizerRunner setProducerProperties(
            Properties properties) {
        this.kafkaProducerProperty = properties;
        return this;
    }

    public DMBDOPOptimizerRunner setConsumerProperties(
            Properties properties) {
        this.kafkaConsumerProperty = properties;
        return this;
    }

    public DMBDOPOptimizerRunner setMetaheuristic(String algorithmName)
            throws IOException {
        if (algorithmName.equals("Dynamic NSGA-II")) {
            this.metaheuristic = new DynamicNSGAII<>(problem,
                                                     limitedNumberOfEvaluationsOrIterations,
                                                     populationSize,
                                                     populationSize,
                                                     populationSize, crossover,
                                                     mutation, selection,
                                                     new DominanceComparator<>(),
                                                     populationEvaluator, 10);
        }
        return this;
    }


    public void run() {
        Thread thread_1 = new Thread(this.metaheuristic);
        Thread thread_2 = new Thread(this.producer);
        Thread thread_3 = new Thread(this.consumer);
        thread_1.start();
        thread_2.start();
        thread_3.start();

        try {
            thread_1.join();
            thread_2.join();
            thread_3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error");
        }
    }
}