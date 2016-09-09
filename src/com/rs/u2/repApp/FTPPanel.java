package com.rs.u2.repApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;

import org.apache.commons.net.ftp.FTPClient;

import com.rs.u2.repApp.ProcessingDialog.FTPDownloadWorker;

public class FTPPanel extends JPanel{
    JLabel lblConnnectHeader = new JLabel("FTP Connection" , JLabel.CENTER);
    JLabel lblRPathHeader = new JLabel("Input the path of remote file" , JLabel.CENTER);
    
    JPanel pnlHost = new JPanel(new BorderLayout(10, 10));
    JLabel lblHost = new JLabel("Host:             ");
    JTextField txtFldHost = new JTextField();
    
    JPanel pnlUserName = new JPanel(new BorderLayout(10, 10));
    JLabel lblUserName = new JLabel("username:         ");
    JTextField txtFldUserName = new JTextField();
    
    JPanel pnlPassword = new JPanel(new BorderLayout(10, 10));
    JLabel lblPassword = new JLabel("password:         ");
    JPasswordField txtFldPassword = new JPasswordField(26);
    
    JPanel pnlRemoteFilePth = new JPanel(new BorderLayout(10, 10));
    JLabel lblRemoteFilePth = new JLabel("Remote File Path: ");
    JTextField txtFldRemoteFilePth = new JTextField();
    
    JPanel pnlHostUserName = new JPanel(new BorderLayout(10, 10));
    JPanel pnlPwdWrapper = new JPanel(new BorderLayout(10, 10));
    JPanel pnlHostUsernamePwdRPth = new JPanel(new BorderLayout(10, 10));
    JPanel pnlRoot = new JPanel(new BorderLayout());
    JPanel pnlRPthWrapper = new JPanel(new BorderLayout(10, 10));
    JPanel pnlRootImport = new JPanel(new BorderLayout());
    
    JButton btnImport = new JButton(" Import ");
    JButton btnConnect = new JButton(" Connect ");
    JFrame parentFrame;
    JDialog dlgImport;
    btnController myBtnController;
    
   // private FTPClient ftp;
    final String tmpFileName ; 
    int result = -1;
    //FTPClientDemo myFTPClientDemo;
    public FTPPanel(btnController pBtnController){
    	
        myBtnController = pBtnController;
        final String workingDirectory = System.getProperty("user.dir");
        tmpFileName = workingDirectory + "\\" + "TempFile.log";
        
    	pnlHost.add(lblHost, BorderLayout.WEST);
    	pnlHost.add(txtFldHost, BorderLayout.CENTER);
    	
    	pnlUserName.add(lblUserName, BorderLayout.WEST);
    	pnlUserName.add(txtFldUserName, BorderLayout.CENTER);
    	
    	pnlPassword.add(lblPassword, BorderLayout.WEST);
    	pnlPassword.add(txtFldPassword, BorderLayout.CENTER);
    	
    	pnlRemoteFilePth.add(lblRemoteFilePth, BorderLayout.WEST);
    	pnlRemoteFilePth.add(txtFldRemoteFilePth, BorderLayout.CENTER);
    	
    	pnlHostUserName.add(pnlHost, BorderLayout.NORTH);
    	pnlHostUserName.add(pnlUserName, BorderLayout.SOUTH);
    	pnlPwdWrapper.add(pnlPassword, BorderLayout.NORTH);
    	//pnlPwdRPth.add(pnlRemoteFilePth, BorderLayout.SOUTH);
    	pnlHostUsernamePwdRPth.add(pnlHostUserName, BorderLayout.NORTH);
    	pnlHostUsernamePwdRPth.add(pnlPwdWrapper, BorderLayout.SOUTH);
    	
    	pnlRoot.add(lblConnnectHeader, BorderLayout.NORTH);
    	pnlRoot.add(pnlHostUsernamePwdRPth, BorderLayout.CENTER);
    	pnlRoot.add(btnConnect, BorderLayout.SOUTH);
    	
    	
    	parentFrame = createFrame(pnlRoot);
    	parentFrame.setVisible(true);
    	parentFrame.getRootPane().setDefaultButton(btnConnect);
    	
    	pnlRPthWrapper.add(pnlRemoteFilePth, BorderLayout.NORTH);
    	
    	pnlRootImport.add(lblRPathHeader, BorderLayout.NORTH);
    	pnlRootImport.add(pnlRPthWrapper, BorderLayout.CENTER);
    	pnlRootImport.add(btnImport, BorderLayout.SOUTH);
    	
    	
    	
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ifEmpty())
					return;
				// System.out.println(downloadFile());
				try {
					ProcessingDialog dlgFTP = new ProcessingDialog(parentFrame,
							"FTP", "Connecting FTP , please wait...",
							FTPPanel.this);
					dlgFTP.startFTP();
					dlgFTP.open();
				} catch (Exception e) {
					System.out.print(e);
					System.out.println("In Exception Block");
				}
				if (result == -1) {// -1 means Could not connect to server.
					JOptionPane.showMessageDialog(FTPPanel.this.parentFrame,
							"Could not connect to server.");
					return;
				} else if (result == -2) {
					JOptionPane.showMessageDialog(FTPPanel.this.parentFrame,
							"Cann't login,usename or password is not correct.");
					return;
				} else if (result == -9) {
					JOptionPane.showMessageDialog(FTPPanel.this.parentFrame,
							"An exception happened !");
					return;
				}
				if (result == 0) {
					dlgImport = new JDialog(parentFrame, "Remote Import", true);
					dlgImport.setContentPane(pnlRootImport);
					dlgImport.getRootPane().setDefaultButton(btnImport);
					dlgImport
							.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dlgImport.setLocationRelativeTo(FTPPanel.this.parentFrame);
					dlgImport.setSize(350, 250);
					dlgImport.setVisible(true);
					
				}
			}// actionPerformed(ActionEvent arg0)
		});//btnImport.addActionListener
    	
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ifEmpty2())
					return;
				// System.out.println(downloadFile());

				try {
					ProcessingDialog dlgFTPDownload = new ProcessingDialog(
							parentFrame, "Download Remote File",
							"Downloading file , please wait...", FTPPanel.this);
					dlgFTPDownload.startFTPDownloading();
					dlgFTPDownload.open();
				} catch (Exception e) {
					System.out.print(e);
				}

				if (result == -3) {
					JOptionPane.showMessageDialog(dlgImport,
							"The remote file may not exist.!");
					return;
				} else if (result == -9) {
					JOptionPane.showMessageDialog(FTPPanel.this.parentFrame,
							"An exception happened !");
					return;
				}
				
				if (result == 0) {
					dlgImport.dispose();
					parentFrame.dispose();
					if (tmpFileName.length() != 0) {
						try {

							myBtnController.importFile(tmpFileName);
						} catch (Exception e) {
							System.out.println(e);
						}// end try
					}// end if
				}// end if (result == 0)
			}//actionPerformed(ActionEvent arg0)
		});//btnImport.addActionListener
    }
    
    private boolean ifEmpty(){
		if (txtFldHost.getText().length() == 0
				|| txtFldUserName.getText().length() == 0
				|| txtFldPassword.getText().length() == 0
				) {
			JOptionPane.showMessageDialog(parentFrame,
					"Please fill up all the fields!");
			return true;
		} else
			return false;
    	
    }
    
    private boolean ifEmpty2(){
		if (txtFldRemoteFilePth.getText().length() == 0) {
			JOptionPane.showMessageDialog(parentFrame,
					"Please Input the Remote File Path!");
			return true;
		} else
			return false;
    	
    }
    
    public void ConnectFTP(){
    	String[] args = {txtFldHost.getText().trim(),txtFldUserName.getText().trim(),txtFldPassword.getText().trim()};
    	try{
    	   result = FTPClientDemo.connect(args);
    	}
    	catch (Exception e){
    		System.out.print(e);
    	}
    }
    /**
     *  Sample javadoc
     */
    public void downloadFile(){
        File f = new File("TempFile.log");
        if (f.exists())
        	f.delete();
        
		if (!FTPClientDemo.ftp.isConnected())
			ConnectFTP();
    	try{
    	   result = FTPClientDemo.download(txtFldRemoteFilePth.getText().trim(), tmpFileName);
    	}
    	catch (Exception e){
    		System.out.print(e);
    	}
    	
    	
    }
    
    private JFrame createFrame(JPanel root) {
		
        
		final JFrame frame = new JFrame("Import file through FTP");
		
		//dialog.getContentPane().add(pChart);
		frame.setContentPane(root);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.pack();
		
		return frame;
	}
}
