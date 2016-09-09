package com.rs.u2.repApp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;




import javax.swing.*;
import javax.swing.border.Border;

import org.jfree.date.EasterSundayRule;

/****************
 * Class LogAnalysisTool is the class of the interface of this tool
 ****************/
public class LogAnalysisTool {
	 /**
	  * The data imported from the files is stored in replicationLog
	  */
	 ReplicationPerformanceLog replicationLog;
	 final String appName = "Log Analysis Tool";
	 /**
	  * The main frame of this application.
	  */
	 JFrame frame;

	 final Action action = new SwingAction();
	 JTextField textField;

	 JPanel jPnlSummaryTab, jPnlSummaryTabWrapper;
	 JPanel infoPaneWrapper;
	 JPanel tablePaneWrapper; 
	 JPanel jPnlTableTab, jPnlTableTabWrapper;
	 JPanel jPnlChartTab, jPnlChartTabWrapper;
	 JPanel chartPaneWrapper;
	 /**
	  * myBtnController is used to response all the actions in the userInterface
	  */
     btnController myBtnController;
     JButton btnGenerateTable;
     JList lstGenerateTable;//The List in the Table Tab to choose what the user want to see in the table 
     DefaultListModel lstMdlGenerateTable;
     JList lstChartAttr;//The List in the Chart Tab to choose the attributes that the user want to see in the chart 
     DefaultListModel lstMdlChartAttr;
     JList lstChartGroups;//The List in the Chart Tab to choose the groups that the user want to see in the chart 
     DefaultListModel lstMdlChartGroups;
     JList lstChartGenInfo;
     DefaultListModel lstMdlChartGenInfo;
     JScrollPane scrollPaneChartAttr;
     JScrollPane scrollPaneChartGroups;
     JScrollPane scrollPaneChartGenInfo;
     JScrollPane scrollPaneTable;
     private JLabel lblChartAttr;
     private JLabel lblChartGroups;
     JLabel lblGenInfo;
     JButton btnCreateChart;
     JButton btnCreateGenInfoChart;
     //Menu
     JMenuBar menuBar;
     JMenu menuFile;
     JMenuItem menuItmImportMenuFileMenuBar;
     JMenuItem menuItmExportMenuFileMenuBar;
     JMenuItem menuItmExitMenuFileMenuBar;
     JMenuItem menuItmRemortMenuFileMenuBar;
     
     DragPane jPnlChartSummaryTabSummary;
     DragPane jPnlChartTabChart;
     DragTablePane jPnlTableTabTable;
     JScrollPane jScrlPaneTableTabTable;
     JTabbedPane tabbedPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogAnalysisTool window = new LogAnalysisTool();
					window.frame.setVisible(true);
		//			window.frame2.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LogAnalysisTool() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		myBtnController = new btnController(this);
		replicationLog = new ReplicationPerformanceLog();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 907, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(appName);
		frame.getContentPane().setLayout(new BorderLayout());
		
		//add TabPane
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane.setBounds(20, 45, 861, 544);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		//initial jPnlTableTab
		
		//initial jPnlChartTab
		
		
		//add Tab to TabPane
		//tabbedPane.addTab("tabSummary", null);
		//add the text Area of in Summary Tab
		
		//use a scrollPane to contain the txtArea
		/********************* Tab Summary **************/
		jPnlSummaryTab = new JPanel();
		//Summary tab
		tabbedPane.addTab("summary", jPnlSummaryTab);
		//System.out.println(tabbedPane.getTabComponentAt(0));
		jPnlSummaryTab.setLayout(new BorderLayout(15, 15));
		jPnlSummaryTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JPanel infoPane = new JPanel(new BorderLayout(5, 5));
		
		scrollPaneChartGenInfo = new JScrollPane();
		//scrollPaneChartGenInfo.setBounds(622, 34, 178, 76);
		//frame.getContentPane().add(scrollPaneChartGenInfo);
		lstMdlChartGenInfo = new DefaultListModel();
		infoPane.add(scrollPaneChartGenInfo, BorderLayout.NORTH);
		lstChartGenInfo = new JList(lstMdlChartGenInfo);
		lstChartGenInfo.setToolTipText("You can have multiple choices by holding 'Shift' or 'Ctrl'.");
		scrollPaneChartGenInfo.setViewportView(lstChartGenInfo);
			
		btnCreateGenInfoChart = new JButton("Update The Chart");
		btnCreateGenInfoChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myBtnController.btnCreateGenInfoChart_Click();
			}
		});
		btnCreateGenInfoChart.setToolTipText("Click this button,then the Chart on the left \n will"
				+ " be updated based on the attributes you chose");
		//btnCreateGenInfoChart.setBounds(643, 121, 128, 23);
		//frame.getContentPane().add(btnCreateGenInfoChart);
		infoPane.add(btnCreateGenInfoChart, BorderLayout.SOUTH);
		btnCreateGenInfoChart.setEnabled(false);
		
		infoPaneWrapper =new JPanel(new BorderLayout());
		infoPaneWrapper.add(infoPane, BorderLayout.NORTH);
		
		jPnlChartSummaryTabSummary = new DragPane(this);
		//jPnlChartSummaryTabSummary.setBounds(10, 11, 591, 472);
		jPnlSummaryTabWrapper = new JPanel(new BorderLayout(15, 15));
		jPnlSummaryTabWrapper.add(infoPaneWrapper, BorderLayout.EAST);
		jPnlSummaryTabWrapper.add(jPnlChartSummaryTabSummary, BorderLayout.CENTER);
		jPnlSummaryTabWrapper.setVisible(false);
		
		jPnlSummaryTab.add(jPnlSummaryTabWrapper, BorderLayout.CENTER);
		
		/***************** Tab Summary********************/
		
	    /***************** Tab Table *********************/
		jPnlTableTab = new JPanel();
		jPnlTableTab.setLayout(new BorderLayout(15, 15));
		jPnlTableTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		//Table tab
		tabbedPane.addTab("Table",jPnlTableTab );
		//Chart tab
		
		scrollPaneTable = new JScrollPane();
		//frame.getContentPane().add(scrollPane);
		lstMdlGenerateTable = new DefaultListModel();
		jPnlTableTab.add(scrollPaneTable);
		lstGenerateTable = new JList(lstMdlGenerateTable);
		lstGenerateTable.setToolTipText("You can have multiple choices by holding 'Shift' or 'Ctrl'.");		
		scrollPaneTable.setViewportView(lstGenerateTable);	
		btnGenerateTable = new JButton("Update The Table");
		btnGenerateTable.setToolTipText("Click this button,then the table on the left \n will"
				+ " be updated based on the attributes you chose");
		btnGenerateTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myBtnController.btnGenerateTable_Click();
			}
		});
		btnGenerateTable.setEnabled(false); //It is not allowed to click the button before import a file.
		
		JPanel JpnlTablePane = new JPanel(new BorderLayout(5, 5));
		JpnlTablePane.add(scrollPaneTable, BorderLayout.NORTH);
		JpnlTablePane.add(btnGenerateTable, BorderLayout.SOUTH);
		//frame.getContentPane().add(btnGenerateTable);
		tablePaneWrapper = new JPanel(new BorderLayout());
		tablePaneWrapper.add(JpnlTablePane, BorderLayout.NORTH);

		jPnlTableTabTable = new DragTablePane(this);
		//jPnlChartSummaryTabSummary.setBounds(10, 11, 591, 472);
		jPnlTableTabWrapper = new JPanel(new BorderLayout(15, 15));
		jPnlTableTabWrapper.add(tablePaneWrapper, BorderLayout.EAST);
		jPnlTableTabWrapper.add(jPnlTableTabTable, BorderLayout.CENTER);
		jPnlTableTab.add(jPnlTableTabWrapper);
		jPnlTableTabWrapper.setVisible(false);
	
		/***************** Tab Table *********************/
		
		
		/***************** Tab Group Chart ****************/
		jPnlChartTab = new JPanel();
		tabbedPane.addTab("Chart", jPnlChartTab);
		jPnlChartTab.setLayout(new BorderLayout(15, 15));
		jPnlChartTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel jPnlGrpAttr = new JPanel(new BorderLayout());
		JPanel jPnlGrpGrps = new JPanel(new BorderLayout());
		JPanel jPnlAttrAndGrps = new JPanel(new BorderLayout());
		JPanel jPnlAttrGrpsBtn = new JPanel(new BorderLayout(5, 5));
		
		lblChartAttr = new JLabel("Group Attributes", JLabel.CENTER);
		jPnlGrpAttr.add(lblChartAttr, BorderLayout.NORTH);
		scrollPaneChartAttr = new JScrollPane();
		jPnlGrpAttr.add(scrollPaneChartAttr, BorderLayout.CENTER);
		lstMdlChartAttr = new DefaultListModel();
		lstChartAttr = new JList(lstMdlChartAttr);
		lstChartAttr.setToolTipText("Choose the Attributes here. " + "You can have multiple choices by holding 'Shift' or 'Ctrl'.");
		scrollPaneChartAttr.setRowHeaderView(lstChartAttr);	
		
		
		lblChartGroups = new JLabel("Groups", JLabel.CENTER);
		jPnlGrpGrps.add(lblChartGroups, BorderLayout.NORTH);
		scrollPaneChartGroups = new JScrollPane();
		jPnlGrpGrps.add(scrollPaneChartGroups, BorderLayout.CENTER);
		lstMdlChartGroups = new DefaultListModel();
		lstChartGroups = new JList(lstMdlChartGroups);
		lstChartGroups.setToolTipText("Choose the Groups here. " + "You can have multiple choices by holding 'Shift' or 'Ctrl'.");
		scrollPaneChartGroups.setViewportView(lstChartGroups);
		
		jPnlAttrAndGrps.add(jPnlGrpAttr, BorderLayout.WEST);		
		jPnlAttrAndGrps.add(jPnlGrpGrps, BorderLayout.EAST);
		
		btnCreateChart = new JButton("Update The Chart");
		btnCreateChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myBtnController.btnCreateChart_Click();
			}
		});
		btnCreateChart.setToolTipText("Click this button,then the Chart on the left \n will"
				+ " be updated based on the attributes and groups you chose");
		btnCreateChart.setEnabled(false);				
		jPnlAttrGrpsBtn.add(jPnlAttrAndGrps, BorderLayout.NORTH);
		jPnlAttrGrpsBtn.add(btnCreateChart, BorderLayout.SOUTH);
		
		
		
		chartPaneWrapper =new JPanel(new BorderLayout());
		chartPaneWrapper.add(jPnlAttrGrpsBtn, BorderLayout.NORTH);
		
		jPnlChartTabChart = new DragPane(this);
		jPnlChartTabWrapper = new JPanel(new BorderLayout(15, 15));			
		jPnlChartTabWrapper.add(chartPaneWrapper,BorderLayout.EAST);
		jPnlChartTabWrapper.add(jPnlChartTabChart,BorderLayout.CENTER);		
		jPnlChartTabWrapper.setVisible(false);
		jPnlChartTab.add(jPnlChartTabWrapper, BorderLayout.CENTER);
		
		
		
		
 //It is not allowed to click the button before import a file.
		/***************** Tab Group Chart ****************/
		
		
		lblGenInfo = new JLabel();
//		lblGenInfo.setBounds(20, 600, 835, 19);
		frame.getContentPane().add(lblGenInfo, BorderLayout.SOUTH);
		
		/****************** Menu ***********************/
		menuBar = new JMenuBar();
//		menuBar.setBounds(0, 0, 3097, 30);
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		menuFile = new JMenu("   File   ");
		menuBar.add(menuFile);
		
		// add Import menu item
		menuItmImportMenuFileMenuBar = new JMenuItem(" Import ");
		menuItmImportMenuFileMenuBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    myBtnController.menuItmImport_Click();
			}
		});
		menuFile.add(menuItmImportMenuFileMenuBar);
		menuItmImportMenuFileMenuBar.setToolTipText("Import a Local File");
		//menuFile.add
		
		// add Remote Import menu item
		menuItmRemortMenuFileMenuBar = new JMenuItem(" Remote Import ");
		menuItmRemortMenuFileMenuBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    myBtnController.menuItmRemote_Click();
			}
		});
		menuFile.add(menuItmRemortMenuFileMenuBar);
		menuItmRemortMenuFileMenuBar.setToolTipText("Import a remort File through FTP");
		//menuFile.add
		
		// add Export menu item
		menuItmExportMenuFileMenuBar = new JMenuItem(" Export ");
		menuItmExportMenuFileMenuBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myBtnController.menuItmExport_Click();
			}
		});
		menuFile.add(menuItmExportMenuFileMenuBar);
		menuItmExportMenuFileMenuBar.setToolTipText("Export the data to Excel");
		// add Exit menu item
		menuItmExitMenuFileMenuBar = new JMenuItem(" Exit ");
		menuItmExitMenuFileMenuBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    System.exit(0);
			}
		});
		menuFile.add(menuItmExitMenuFileMenuBar);
		menuItmExitMenuFileMenuBar.setToolTipText("Exit the program");
	    /*********************** Menu ***********************/
		 
		
		
		
		
		
	}//end private void initialize()
	
	

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		   
		}
	}
}
