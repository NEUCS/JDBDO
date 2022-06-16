package Archive.KRArchive;

import Archive.AbstractArchive;
import Archive.KRArchive.tools.Quadruple;
import com.google.gson.Gson;
import individual.ContinuousIndividual;
import population.Population;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2022/6/3 14:47
 */
public class KRArchive<I extends ContinuousIndividual>
        extends AbstractArchive<I> {
    public KRArchive(int archiveSize) {
        super(archiveSize);

    }

    @Override
    public void loadExternalArchive(String path,
            Population<ContinuousIndividual> population) throws IOException {
        Population<ContinuousIndividual> population1 = new Population<>();
        for (ContinuousIndividual indi : population.getPopulation()) {
            population1.add((ContinuousIndividual) indi.copy());
        }
        Gson gson = new Gson();
        String json = "";
        File file = new File(path);
        Reader reader = new InputStreamReader(new FileInputStream(file),
                                              StandardCharsets.UTF_8);
        int ch = 0;
        StringBuffer buffer = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            buffer.append((char) ch);
        }
        reader.close();
        json = buffer.toString();
        Map<String, List<List<Double>>> map =
                (Map<String, List<List<Double>>>) gson
                        .fromJson(json, Map.class);
        for (Map.Entry<String, List<List<Double>>> entry : map.entrySet()) {
            Population<I> newpop =  new Population<>();
            for (int i = 0; i < population1.getPopulation().size(); i++) {
                population1.getIndividual(i).setAllVariables((List<Double>) entry.getValue().get(i)) ;
            }
            for (int i = 0; i < population1.getPopulation().size(); i++) {
                newpop.add((I) population1.getIndividual(i).copy());
            }
            addToArchive(entry.getKey(), newpop);
        }
    }

    @Override
    public void outputArchiveToFile(String path) throws IOException {
        Gson json = new Gson();
        Map<String, List<List<Double>>> map = new HashMap<>();
        for (Quadruple<I> q : archive) {
            List<List<Double>> list = new LinkedList<>();
            for (I individual : q.getPopulation().getPopulation()) {
                list.add(individual.getAllVariables());
            }
            map.put(q.getFeature(), list);
        }
        Object j = json.toJson(map);
        String str = j.toString();
        OutputStreamWriter writer =
                new OutputStreamWriter(new FileOutputStream(path),
                                       StandardCharsets.UTF_8);
        writer.write(str);
        writer.close();

    }

    public void addToArchive(String feature, Population<I> population) {
        this.archive.add(new Quadruple<>(feature, population, 0, 0));
    }

    public Population<I> getPopulationFromArchive(String feature) {
        Population<I> population = null;
        for (Quadruple<I> q : archive) {
            if (q.getFeature().equals(feature)) {
                population = q.getPopulation();
            }
        }
        return population;
    }
}