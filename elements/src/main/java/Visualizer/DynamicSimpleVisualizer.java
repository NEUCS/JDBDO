package Visualizer;

import individual.ContinuousIndividual;
import individual.Individual;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import population.Population;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/27 11:10
 */
public class DynamicSimpleVisualizer<I extends Individual<?>> {
    private Map<String, XYChart> charts;
    private XYChart pf;
    private SwingWrapper<XYChart> swingWrapper;
    private int pfCounter = 0;
    private String chartName;


    public DynamicSimpleVisualizer(String chartName) {
        this.chartName = chartName;
        this.charts = new LinkedHashMap<>();
        initialize();
    }

    public void initialize() {
        this.pf = new XYChartBuilder().width(800).height(500).xAxisTitle("f1")
                .yAxisTitle("f2").theme(Styler.ChartTheme.GGPlot2).build();
        this.pf.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        this.pf.getStyler().setDefaultSeriesRenderStyle(
                XYSeries.XYSeriesRenderStyle.Scatter).setMarkerSize(5);
        this.pf.addSeries(this.chartName, new double[]{0},
                                              new double[]{0});
        this.charts.put("PF", this.pf);
        this.swingWrapper = new SwingWrapper<XYChart>(new ArrayList<>(this.charts.values()));
        this.swingWrapper.displayChartMatrix();
    }

    public void updateChart(Population<I> population){
        updatePf(population);
        repaintChart();
    }

    public void updatePf(Population<I> population){
        double[] x = new double[population.getPopulation().size()];
        double[] y = new double[population.getPopulation().size()];
        for (int i = 0; i < population.getPopulation().size(); i++) {
            x[i] = population.getIndividual(i).getObjectiveValue(0);
            y[i] = population.getIndividual(i).getObjectiveValue(1);
        }
        this.pf.addSeries("PF"+this.pfCounter, x, y );
        pfCounter++;
    }

    public void repaintChart(){
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < this.charts.values().size(); i++) {
            this.swingWrapper.repaintChart(i);
        }
    }
}