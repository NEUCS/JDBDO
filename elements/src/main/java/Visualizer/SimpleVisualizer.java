package Visualizer;

import individual.ContinuousIndividual;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import population.Population;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @program: framework
 * @description:
 * @author: Zheng Xuanyu
 * @create time: 2021/12/27 11:10
 */
public class SimpleVisualizer implements Visualizer {
    double[] a;
    double[] b;

    public SimpleVisualizer(Population<ContinuousIndividual> population) {
        this.a = new double[population.getPopulation().size()];
        this.b = new double[population.getPopulation().size()];
        for (int i = 0; i < population.getPopulation().size(); i++) {
            a[i] = population.getPopulation().get(i).getObjectiveValue(0);
            b[i] = population.getPopulation().get(i).getObjectiveValue(1);
        }
    }

    public void show() {
        // Create Chart
        final XYChart chart =
                new XYChartBuilder().width(600).height(500).title("Dynamic ABC")
                        .xAxisTitle("f1").yAxisTitle("f2")
                        .theme(Styler.ChartTheme.Matlab).build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(
                XYSeries.XYSeriesRenderStyle.Scatter);
        // Series
        chart.addSeries("PF1", this.a, this.b);
        //        for (int i = 0; i < a.size(); i++) {
        //            chart.addSeries(sn[i], a.get(i), b.get(i));
        //        }

        //        new SwingWrapper<XYChart>(chart).displayChart();


        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("ParetoFront");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel chartPanel = new XChartPanel<XYChart>(chart);
                frame.add(chartPanel, BorderLayout.CENTER);
                JLabel label =
                        new JLabel("Nearly-Optimal PF", SwingConstants.CENTER);
                frame.add(label, BorderLayout.SOUTH);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}