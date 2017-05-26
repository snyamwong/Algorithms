package edu.wit.cs.comp2350.tests;


import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

import edu.wit.cs.comp2350.BinTree;
import edu.wit.cs.comp2350.Hash;
import edu.wit.cs.comp2350.Linear;
import edu.wit.cs.comp2350.Speller;
import edu.wit.cs.comp2350.Trie;



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
     * Creates a chart with four datasets of algorithm runtimes and
     * sets appearance of chart objects.
     *
     * @return A chart instance.
     */
    private static JFreeChart createChart() {
    	XYDataset[] data = new XYDataset[3];
    	
    	data[0] = createDataset('b', "binTree");
    	data[1] = createDataset('h', "hash");
    	data[2] = createDataset('t', "trie");
    	
    	
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Randomized retrieval runtimes",// chart title
            "number of searches",              // x axis label
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
     * @param algo  The character used to represent algorithm
     * @param name  The name of algorithm to appear in the legend
     * 
     * @return A sample dataset.
     */
    public static XYDataset createDataset(char algo, String name) {
        XYDataset result = DatasetUtilities.sampleFunction2D(new AddRuns(algo),
                0.0, 400.0, 21, name);
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
    	
    	// t is the number of words to search for
        public double getValue(double t) {
    		String file = "text/10000.txt";
    		ArrayList<String> words = new ArrayList<String>();
        	double sec = 0;
    		Random r = new Random();
    		
        	if (t == 0)
        		return 0;
        	
    		try (Scanner s = new Scanner(new File(file))) {
    			Speller l = new Linear();
    			
    			switch (algo) {
    			case 'b':
    				l = new BinTree();
    				break;
    			case 'h':
    				l = new Hash();
    				break;
    			case 't':
    				l = new Trie();
    				break;
    			default:
    				System.out.println("Invalid data structure");
    				System.exit(0);
    				break;
    			}
    			
    			while (s.hasNext()) {
    				String w = s.next().toLowerCase().replaceAll("[^a-z ]","");
    				l.insertWord(w);
    				words.add(w);
    			}
    			
            	for (int i = 0; i < Double.valueOf(t).intValue(); i++) {
            		
            		// alternate between a word in the list and a merging of two words in the list
            		String word = (i%2==0)?words.get(r.nextInt(words.size()-1)):shmerge(words.get(r.nextInt(words.size()-1)), words.get(r.nextInt(words.size()-1)));
            		long start = System.nanoTime();
            		
            		if (!l.contains(word))
            			l.getSugg(word);
            		
            		long end = System.nanoTime();

            		sec += (end-start)/1E6;
            	}
    		} catch (FileNotFoundException e) {
    			System.err.println("Cannot open file " + file + ". Exiting.");
    			System.exit(0);
    		}

            return sec;
        }

		private String shmerge(String s1, String s2) {
			return s1.substring(0, s1.length()/2) + s2.substring(s2.length()/2, s2.length());
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
