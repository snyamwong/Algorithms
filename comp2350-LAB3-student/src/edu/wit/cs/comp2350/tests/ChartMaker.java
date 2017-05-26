package edu.wit.cs.comp2350.tests;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import edu.wit.cs.comp2350.BadTable;
import edu.wit.cs.comp2350.HashTable;
import edu.wit.cs.comp2350.RandTable;
import edu.wit.cs.comp2350.SimpleTable;

public class ChartMaker extends ApplicationFrame
{
	private static JFreeChart createClusteredChart(String title, String categoryAxisLabel, String valueAxisLabel, IntervalXYDataset dataset) {

		NumberAxis domainAxis = new NumberAxis(categoryAxisLabel);
		domainAxis.setAutoRangeIncludesZero(false);

		ValueAxis valueAxis = new NumberAxis(valueAxisLabel);

		XYBarRenderer renderer = new ClusteredXYBarRenderer();

		XYPlot plot = new XYPlot(dataset, domainAxis, valueAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

		return chart;
	}

	private static final long serialVersionUID = 1L;
	public ChartMaker(String chartTitle)
	{
		super("Hashtable Chart");
		JFreeChart barChart = createClusteredChart(
				chartTitle,
				"Index",
				"Count",
				createDataset());

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
		XYPlot plot = (XYPlot) barChart.getPlot();
		XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
	    renderer.setShadowVisible(false);
	}
	private IntervalXYDataset createDataset()
	{
		String bad = "bad hash";
		String simple = "simple hash";
		String rand = "random table hash";
		String file = "text/dickens.txt";
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		int[] stats;
		try (Scanner s = new Scanner(new File(file))) {
			HashTable h = new BadTable(256);
			h.buildTable(s);
			stats = h.getStats();
	        XYSeries series = new XYSeries(bad);
			for (int i = 0; i < stats.length; i++)
				series.add(i, stats[i]);
			dataset.addSeries(series);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
		try (Scanner s = new Scanner(new File(file))) {
			HashTable h = new SimpleTable(256);
			h.buildTable(s);
			stats = h.getStats();
	        XYSeries series = new XYSeries(simple);
			for (int i = 0; i < stats.length; i++)
				series.add(i, stats[i]);
			dataset.addSeries(series);
		} catch (FileNotFoundException e) {
		}
		try (Scanner s = new Scanner(new File(file))) {
			HashTable h = new RandTable(256);
			h.buildTable(s);
			stats = h.getStats();
	        XYSeries series = new XYSeries(rand);
			for (int i = 0; i < stats.length; i++)
				series.add(i, stats[i]);
			dataset.addSeries(series);
		} catch (FileNotFoundException e) {
		}

		return dataset;
	}
	
	public static void main(String[] args)
	{
		ChartMaker chart = new ChartMaker("Distribution of hash table elements");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}