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

import edu.wit.cs.comp2350.Graph;
import edu.wit.cs.comp2350.LAB8;
import edu.wit.cs.comp2350.Node;


/**
* An example showing how to plot a simple function in JFreeChart.  Because
* JFreeChart displays discrete data rather than plotting functions, you need
* to create a dataset by sampling the function values.
*/
public class ChartMaker extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private static final int DATASETS = 2;

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
    	XYDataset[] data = new XYDataset[DATASETS];
    	
    	data[0] = createDataset('t', "topological sort");
    	data[1] = createDataset('n', "naive");
    	
    	
        JFreeChart chart = ChartFactory.createXYLineChart(
            "topological sort solution runtimes",  // chart title
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
        XYLineAndShapeRenderer renderer[] = new XYLineAndShapeRenderer[DATASETS];
        
        for (int i = 0; i < DATASETS; i++) {
            plot.setDataset(i, data[i]);
            renderer[i] = new XYLineAndShapeRenderer(); 
            plot.setRenderer(i, renderer[i]);
        }

        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
        
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
                0.0, 2000.0, 21, name);
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
    		
    		for (int i = 0; i < 5; i++) {
    			Graph g = _genRandDAG(Double.valueOf(t).intValue());

    			long start = System.nanoTime();

    			switch (algo) {
    			case 'n':
    				LAB8.FindNaive(g);
    				break;
    			case 't':
    				LAB8.FindTopo(g);
    				break;
    			default:
    				System.out.println("Invalid algorithm");
    				System.exit(0);
    				break;
    			}

    			long end = System.nanoTime();

    			sec += (end-start)/1E6;
    		}
    		return sec/5;
    	}

    	private Graph _genRandDAG(int size) {
    		Graph g = new Graph();
    		boolean[][] adj = new boolean[size][size];
    		Random r = new Random();
    		
    		for (int i = 0; i < size; i++) {	// random lower triangular adjacency matrix
    			for (int j = 0; j < i; j++) {
    				adj[i][j] = r.nextBoolean();
    			}
    		}
    		
    		for (int i = size - 1; i > 0; i--) {	// shuffle rows
    			int swapWith = r.nextInt(i);
    			
    			for (int j = 0; j < size; j++) {
    				boolean temp = adj[i][j];
    				adj[i][j] = adj[swapWith][j];
    				adj[swapWith][j] = temp;
    			}
    		}
    		
    		for (int i = 0; i < size; i++) {	//  convert matrix to Graph
    			g.AddNode(new Node(i, "file" + Integer.toString(i)));
    			for (int j = 0; j < i; j++) {
    				if (adj[i][j])
    					g.AddEdge(j, i);
    			}
    		}
    		
    		return g;
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
