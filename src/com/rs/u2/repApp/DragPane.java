package com.rs.u2.repApp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.jfree.chart.ChartPanel;

/**
 * The Pane is an extended JPanel to contain a chart.
 * 
 */
public class DragPane extends JPanel {

    private static final String TITLE = "Drag me!";
    private static final int W = 640;
    private static final int H = 480;
    private Point origin = new Point(W / 2, H / 2);
    private Point mousePt;
    /**
     * In the main frame
     */
    private JPanel dragablePanel = new JPanel(new BorderLayout());//In the main frame
    private ChartPanel tabChartPanel;
    
    JFrame parentFrame;
    private JFrame controlFrame = null;
    private Point dialogLocation;
    /**
     * For the dragged out Chart frame.
     */
    private ChartPanel dialogChartPanel; // for the dragged out dialog
    private LogAnalysisTool userInterface;

    public DragPane(LogAnalysisTool pUserInterface) {
    	super(new BorderLayout());
    	this.parentFrame = pUserInterface.frame;
    	this.userInterface = pUserInterface;
    	this.add(dragablePanel, BorderLayout.NORTH);
    	this.setToolTipText("Click and hold the left button on your mouse, then drag the chart out!");
        dragablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                //repaint();
				if (dialogChartPanel != null) {
					if (controlFrame == null) {

						controlFrame = createFrame();
					}

					
					Point locationPnlChartSummaryTabSummary = tabChartPanel.getLocationOnScreen();
					//Point tabLocation = userInterface.tabbedPane.getTabComponentAt(0).getLocation();
					Rectangle b = getBounds();
//					controlFrame.setBounds(new Rectangle(
//							//b.x + frameLocation.x + tabbedPaneLation.x , b.y + frameLocation.y+ tabbedPaneLation.y ,
//							tabSummary.x , tabSummary.y,
//							b.width, b.height));
					
					Insets insets = controlFrame.getInsets();
					Dimension chartSize = tabChartPanel.getSize();
//					Point locOffset = myChartPanel.getLocation();
//					controlFrame.setLocation(tabSummary.x  - locOffset.x, tabSummary.y - locOffset.y);
					controlFrame.setBounds(new Rectangle(locationPnlChartSummaryTabSummary.x  - insets.left, locationPnlChartSummaryTabSummary.y - insets.top, 
							chartSize.width + insets.left + insets.right,
							chartSize.height + insets.top + insets.bottom));
					dialogLocation = controlFrame.getLocation();
					controlFrame.setVisible(true);
				}
                
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            	controlFrame = null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
            	if (dialogLocation != null && controlFrame != null){
                int dx = e.getX() - mousePt.x;
                int dy = e.getY() - mousePt.y;
                origin.setLocation(origin.x + dx, origin.y + dy);
                mousePt = e.getPoint();
                dialogLocation.setLocation(dialogLocation.x + dx, dialogLocation.y + dy);
                controlFrame.setLocation(dialogLocation);
            	}
            }
            
            
        });
    }
    
    /**
     * Add the chart in the main frame
     * @param oneChart
     */
	public void addChart(ChartPanel oneChart) {
		this.removeAll();
		dragablePanel.removeAll();
		this.add(dragablePanel, BorderLayout.NORTH);
		JLabel label = new JLabel("Hold and Drag Here", JLabel.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		dragablePanel.add(label, BorderLayout.NORTH);
		dragablePanel.setOpaque(true);
		dragablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
		this.tabChartPanel = oneChart;
		this.add(oneChart, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	

	
	public void removeChart() {
		this.removeAll();
		this.revalidate();
		this.repaint();
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(W, H);
    }

    /**
     * Set the Chart for dragged out chart frame
     * @param pChartPnl
     */
    public void setTabChartPanel(ChartPanel pChartPnl){
    	tabChartPanel = pChartPnl;
    }
    
	public ChartPanel getTabChartPanel(){
		return tabChartPanel;
	}
    
    public void setDialogChartPanel(ChartPanel pChartPnl){
    	dialogChartPanel = pChartPnl;
    }
    
	public ChartPanel getDialogChartPanel(){
		return dialogChartPanel;
	}
	
	/**
	 * create a frame to contain the chart that has been dragged out.
	 * @return
	 */
    private JFrame createFrame() {
		
       
		final JFrame frame = new JFrame("Chart");
		
		//dialog.getContentPane().add(pChart);
		frame.setContentPane(dialogChartPanel);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.pack();
		
		return frame;
	}

}