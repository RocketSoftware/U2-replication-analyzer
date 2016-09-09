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
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;

/**
 * 
 * The Pane to contain a table extends JPanel
 */
public class DragTablePane extends JPanel {

    private static final String TITLE = "Drag me!";
    private static final int W = 640;
    private static final int H = 480;
    private Point origin = new Point(W / 2, H / 2);
    private Point mousePt;
    /**
     * In the main frame
     */
    private JPanel dragablePanel = new JPanel(new BorderLayout());
    private TableDemo tabletabTable;
    
    JFrame parentFrame;
    private JFrame controlFrame = null;
    private Point dialogLocation;
    /**
     * For the dragged out Table frame.
     */
    private TableDemo dialogTable;
    private LogAnalysisTool userInterface;
    private JScrollPane JsclPaneTable;

    public DragTablePane(LogAnalysisTool pUserInterface) {
    	super(new BorderLayout());
    	this.parentFrame = pUserInterface.frame;
    	this.userInterface = pUserInterface;
    	this.add(dragablePanel, BorderLayout.NORTH);
    	this.setToolTipText("Click and hold the left button on your mouse, then drag the table out!");
        dragablePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                //repaint();
				if (dialogTable != null) {
					if (controlFrame == null) {

						controlFrame = createFrame();
					}

					
					Point locationPnlChartSummaryTabSummary = tabletabTable.getLocationOnScreen();
					//Point tabLocation = userInterface.tabbedPane.getTabComponentAt(0).getLocation();
					Rectangle b = getBounds();
//					controlFrame.setBounds(new Rectangle(
//							//b.x + frameLocation.x + tabbedPaneLation.x , b.y + frameLocation.y+ tabbedPaneLation.y ,
//							tabSummary.x , tabSummary.y,
//							b.width, b.height));
					
					Insets insets = controlFrame.getInsets();
					Dimension chartSize = tabletabTable.getSize();
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
                //repaint();
                
                dialogLocation.setLocation(dialogLocation.x + dx, dialogLocation.y + dy);
                controlFrame.setLocation(dialogLocation);
            	}
            }
            
            
        });
    }
    /**
     * Add the table in the main frame
     * @param oneChart
     */
	public void addTable(TableDemo oneTableDemo) {
		this.removeAll();
		dragablePanel.removeAll();
		this.add(dragablePanel, BorderLayout.NORTH);
		JLabel label = new JLabel("Hold and Drag Here", JLabel.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		dragablePanel.add(label, BorderLayout.NORTH);
		//dragablePanel.add(new JLabel(" "), BorderLayout.SOUTH);
		dragablePanel.setOpaque(true);
		dragablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
		//dragablePanel.setBackground(Color.GRAY);
		this.tabletabTable = oneTableDemo;
		this.add(tabletabTable, BorderLayout.CENTER);
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
     * Set the table for dragged out chart frame
     * @param pChartPnl
     */
    public void setTabletabTable(TableDemo pTabletabTable){
    	tabletabTable = pTabletabTable;
    }
    
	public TableDemo getTabChartPanel(){
		return tabletabTable;
	}
    
    public void setdialogTable(TableDemo pDialogTable){
    	dialogTable = pDialogTable;
    }
    
	public TableDemo getdialogTable(){
		return dialogTable;
	}
	
	/**
	 * create a frame to contain the table that has been dragged out.
	 * @return
	 */
    private JFrame createFrame() {
		
       
		
		final JFrame frame = new JFrame("Table");
		//dialog.getContentPane().add(pChart);
		frame.setContentPane(dialogTable);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//frame.setModal(false);
		frame.pack();
		
		return frame;
	}

}