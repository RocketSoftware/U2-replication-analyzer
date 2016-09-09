package com.rs.u2.repApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class ProcessingDialog extends JDialog {
	String filename;
	JProgressBar progressBar;
	boolean alreadyDone = false;
	btnController myBtnController;
	FTPPanel myFTPPanel;

	
	public ProcessingDialog(JFrame owner, String title, String content, String filename, btnController pBtnController) {
		super(owner, title);
		this.myBtnController = pBtnController;
		this.filename = filename;
		
		Container dlgImporting = getContentPane();
	    dlgImporting.setLayout(new BorderLayout());
	    
	    JPanel pane = new JPanel(new BorderLayout(15, 15));
	    pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    
	    JLabel lblImporting = new JLabel(content, JLabel.CENTER);
	    pane.add(lblImporting, BorderLayout.NORTH);
	    progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    pane.add(progressBar, BorderLayout.SOUTH);
	    
		dlgImporting.add(pane, BorderLayout.CENTER);
		
		setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
	}
	
	
	public ProcessingDialog(JFrame owner, String title, String content
			) {
		super(owner, title);
	
		Container dlgImporting = getContentPane();
		dlgImporting.setLayout(new BorderLayout());

		JPanel pane = new JPanel(new BorderLayout(15, 15));
		pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JLabel lblImporting = new JLabel(content, JLabel.CENTER);
		pane.add(lblImporting, BorderLayout.NORTH);
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		pane.add(progressBar, BorderLayout.SOUTH);

		dlgImporting.add(pane, BorderLayout.CENTER);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	public ProcessingDialog(JFrame owner, String title, String content, FTPPanel pFTPPanel
			) {
		super(owner, title);
		this.myFTPPanel = pFTPPanel;
		
		Container dlgImporting = getContentPane();
		dlgImporting.setLayout(new BorderLayout());

		JPanel pane = new JPanel(new BorderLayout(15, 15));
		pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JLabel lblImporting = new JLabel(content, JLabel.CENTER);
		pane.add(lblImporting, BorderLayout.NORTH);
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		pane.add(progressBar, BorderLayout.SOUTH);

		dlgImporting.add(pane, BorderLayout.CENTER);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	
	public void open() {
		synchronized (this) {
			if (alreadyDone)
				return;
		}

		pack();
		setAlwaysOnTop(true);
		setModal(true);

		setLocationRelativeTo(getOwner());
		setVisible(true);
	}
	
	public void startImporting() {
		new Thread(new ImportWorker()).start();
	}
	public void startCreatingGrpChart() {
		new Thread(new GrpChartWorker()).start();
	}
	public void startCreatingGenChart() {
		new Thread(new GenChartWorker()).start();
	}
	public void startCreatingTable() {
		new Thread(new TableWorker()).start();
	}
	public void startFTP() {
		new Thread(new FTPWorker()).start();
		
	}

	public void startFTPDownloading() {
		// TODO Auto-generated method stub
		new Thread(new FTPDownloadWorker()).start();
	}
	
	class FTPWorker implements Runnable {
        public Void doInBackground() {
        	try{
				
        		myFTPPanel.ConnectFTP();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
	        	
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}
	
	
	class FTPDownloadWorker implements Runnable {
        public Void doInBackground() {
        	try{
				
        		myFTPPanel.downloadFile();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
	        	
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}
	
	class ImportWorker implements Runnable {
        public Void doInBackground() {
        	try{
				myBtnController.readFile(filename);
			    
				if (myBtnController.userInterface.replicationLog
						.getReplicationPerformanceInfoList().size() != 0) {
					// Initialize TabbedPane
					myBtnController.initializeTabbedPane();
					// Initialize
					
/*                      userInterface.jPnlSummaryTab.setVisible(true);
                    userInterface.jPnlTableTab.setVisible(true);
                    userInterface.jPnlChartTab.setVisible(true);*/
					myBtnController.userInterface.jPnlSummaryTabWrapper.setVisible(true);
					myBtnController.userInterface.jPnlTableTabWrapper.setVisible(true);
					myBtnController.userInterface.jPnlChartTabWrapper.setVisible(true);
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}
	
	class TableWorker implements Runnable {
        public Void doInBackground() {
        	try{
        		myBtnController.generateTable();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			while(ProcessingDialog.this.isVisible() )
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}
	
	class GrpChartWorker implements Runnable {
        public Void doInBackground() {
        	try{
        		myBtnController.createChart();	

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}

	class GenChartWorker implements Runnable {
        public Void doInBackground() {
        	try{
        		myBtnController.createGenInfoChart();	

			} catch (Exception e)
			{
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        public void done() {
			synchronized (ProcessingDialog.this) {
	        	alreadyDone = true;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			ProcessingDialog.this.setVisible(false);
        }

		@Override
		public void run() {
			doInBackground();
			done();
		}
	}
	

	

}