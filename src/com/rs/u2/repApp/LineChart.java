package com.rs.u2.repApp;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class LineChart {
	
	DefaultXYDataset dataset;
	int numOfSeries;
	
	public LineChart(){
		dataset = new DefaultXYDataset();
		numOfSeries = 0;
	}
	
	public void draw() {
		 
        
                JFrame frame = new JFrame("Charts");
 
                frame.setSize(600, 400);
                //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
 
                //XYDataset ds = createDataset(dataset);
               
                ChartPanel cp = getOneChart();
                frame.getContentPane().add(cp);
            
        
 
    } 
	
	public ChartPanel getOneChart(){
		JFreeChart chart = ChartFactory.createXYLineChart("Analysis Chart",
                "Record Number", "Value", dataset, PlotOrientation.VERTICAL, true, true,
                false);
         
        ChartPanel cp = new ChartPanel(chart);
        cp.setToolTipText("Right click the chart to get more option.");
        
        return cp;

	}
	
	public void addSerie(String name,double[][] data){
		numOfSeries += 1;
		dataset.addSeries(name, data);
	}
 
/*    private static XYDataset createDataset(double[][] data) {
    	
 
        DefaultXYDataset ds = new DefaultXYDataset();
 
        //double[][] data = { {0.1, 0.2, 0.3}, {1, 2, 3} };
 
        ds.addSeries("series1", data);
 
        return ds;
    }*/
}
