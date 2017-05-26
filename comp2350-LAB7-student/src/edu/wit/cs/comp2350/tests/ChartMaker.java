package edu.wit.cs.comp2350.tests;


import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import edu.wit.cs.comp2350.LAB7;


/**
* An example showing how to plot a simple function in JFreeChart.  Because
* JFreeChart displays discrete data rather than plotting functions, you need
* to create a dataset by sampling the function values.
*/
public class ChartMaker extends ApplicationFrame {
	private static final long serialVersionUID = 1L;


	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public ChartMaker(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 400));
        setContentPane(chartPanel);
    }
   
    /**
     * Creates a chart with three datasets of algorithm runtimes and
     * sets appearance of chart objects.
     *
     * @return A chart instance.
     */
    private static JFreeChart createChart() {
    	XYDataset[] data = new XYDataset[3];
    	
    	data[0] = createDataset('d', "dynamic programming");
    	data[1] = createDataset('e', "enumeration");
    	data[2] = createDataset('g', "greedy");
    	
    	
        JFreeChart chart = ChartFactory.createXYLineChart(
            "knapsack solution runtimes",  // chart title
            "number of items",     // x axis label
            "time(ms)",                // y axis label
            data[0],                   // data
            PlotOrientation.VERTICAL, 
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        XYLineAndShapeRenderer renderer[] = new XYLineAndShapeRenderer[3];
        
        for (int i = 0; i < 3; i++) {
            plot.setDataset(i, data[i]);
            renderer[i] = new XYLineAndShapeRenderer(); 
            plot.setRenderer(i, renderer[i]);
        }

        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
        plot.getRendererForDataset(plot.getDataset(2)).setSeriesPaint(0, Color.green);
        
        return chart;
    }
   
    /**
     * Creates a dataset based on algorithm runtimes.
     *
     * @param algo  The string used to represent algorithm
     * @param name  The name of algorithm to appear in the legend
     * 
     * @return A sample dataset.
     */
    public static XYDataset createDataset(char algo, String name) {
        XYDataset result = DatasetUtilities.sampleFunction2D(new AddRuns(algo),
                0.0, 11.0, 12, name);
        return result;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }
   
    /**
     * Generates all the runtimes for the graph.
     */
    static class AddRuns implements Function2D {
    	private char algo;
    	
    	public AddRuns(char a) { algo = a; }

    	// t is the number of characters in each string
    	public double getValue(double t) {
    		double sec = 0;
    		
    		if (t == 0)
    			return 0;
    		System.out.println(t);
    		for (int i = 0; i < 20; i++) {
    			LAB7.Item[] items = randItems(Double.valueOf(t).intValue());

    			long start = System.nanoTime();

    			switch (algo) {
    			case 'd':
    				LAB7.FindDynamic(items, 20);
    				break;
    			case 'e':
    				LAB7.FindEnumerate(items, 20);
    				break;
    			case 'g':
    				LAB7.FindGreedy(items, 20);
    				break;
    			default:
    				System.out.println("Invalid algorithm");
    				System.exit(0);
    				break;
    			}

    			long end = System.nanoTime();

    			sec += (end-start)/1E6;
    		}
    		return sec/20;
    	}

		private LAB7.Item[] randItems(int numItems) {
			LAB7.Item[] items = new LAB7.Item[numItems];
		    Random r = new Random();

		    for (int i = 0; i < numItems; i++)
		        items[i] = new LAB7.Item(r.nextInt(10), r.nextInt(20), i);
		    return items;
		}

    }

    /**
     * Starting point for the chartmaker.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        ChartMaker cm = new ChartMaker(
                "Chart Window");
        cm.pack();
        RefineryUtilities.centerFrameOnScreen(cm);
        cm.setVisible(true);
    }

}
