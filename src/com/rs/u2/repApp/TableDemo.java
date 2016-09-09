/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package com.rs.u2.repApp;

/*
 * TableDemo.java requires no other files.
 */
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;













import com.rs.u2.repApp.ProcessingDialog.TableWorker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/** 
 * TableDemo is an extended JPanel with table in it.
 */
public class TableDemo extends JPanel {
    private boolean DEBUG = false;
    LogAnalysisTool userInterface;
    JTable myTable;
    MyTableModel myTableModel;
    private JPopupMenu popupMenu;
    
    public TableDemo(LogAnalysisTool userInterface, MyTableModel myTableModel){
    	super(new GridLayout(1,0));
    	this.userInterface= userInterface;
    	myTable = new JTable(myTableModel);
         configTable();
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(myTable , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
    }
    public TableDemo(LogAnalysisTool userInterface, String[] pColumnNames, Object[][] pData , Class[] pClass) {
        super(new GridLayout(1,0));
    	this.userInterface= userInterface;
        myTableModel = new MyTableModel(pColumnNames, pData, pClass);
        myTable = new JTable(myTableModel);
        myTable.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				TableModel model = (TableModel)e.getSource();
				Object obj = model.getValueAt(row, col);
				
				if (DEBUG) {
	                System.out.println("cell changed at " + row + "," + col
	                                   + " to " + obj
	                                   + " (an instance of "
	                                   + obj.getClass() + ")");
	            }
			}
		});
       configTable();
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(myTable , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //JScrollPane scrollPane = new JScrollPane(myTable);
        
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setViewportView(myTable);
        //Add the scroll pane to this panel.
        add(scrollPane);
    }
    public ExportDialog createExportDialog(){
    	ExportDialog dialog = new ExportDialog(userInterface.frame, "Export", true);
		dialog.setVisible(true);
		return dialog;
    }
    private void createPopupmenu() {
    	 popupMenu = new JPopupMenu();
    	 JMenuItem menuItem = new JMenuItem("Export...");
    	 menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createExportDialog();
			}
		});
    	 
    	 popupMenu.add(menuItem);
    }
    
    private void configTable(){
    	myTable.setPreferredScrollableViewportSize(new Dimension(800, 700));
        myTable.setFillsViewportHeight(true);
        myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        myTable.setDefaultRenderer(Date.class, new MyDateRenderer());
        
        for (int i = 0; i < myTable.getColumnCount(); i++) {
        	if (Date.class == myTable.getColumnClass(i)) {
        		myTable.getColumnModel().getColumn(i).setPreferredWidth(150);
        	}
        	else
        		myTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        createPopupmenu();
        myTable.addMouseListener(new PopupListener());
    }
    public void addData(Object [] newData)
    {
    	MyTableModel myTab = (MyTableModel)myTable.getModel();
    	myTab.addRow(newData);
    }

    class ExportDialog extends JDialog {
    	JTextField txtFilename;
    	JButton btnBrowse;
    	JButton btnExport, btnCancel;
    	
    	public ExportDialog(Frame frame, String title, boolean modal) {
    		super(frame, title, modal);
    		
    		Container cp = getContentPane();
    		
			JPanel rootp = new JPanel(new BorderLayout(10, 10));
			rootp.add(new JLabel("Export data in this table to Excel file."), BorderLayout.NORTH);
			JPanel centerp = new JPanel(new BorderLayout(10, 10));
			
			centerp.add(new JLabel("File name:"), BorderLayout.WEST);
			centerp.add(txtFilename = new JTextField(), BorderLayout.CENTER);
			centerp.add(btnBrowse = new JButton("Browse..."), BorderLayout.EAST);
			btnBrowse.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Sheet", "xlsx");
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(filter);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setSelectedFile(new File(txtFilename.getText()));
					fileChooser.setLocation(ExportDialog.this.getLocation());
					int retval = fileChooser.showOpenDialog(null);
					
					if (retval == JFileChooser.APPROVE_OPTION) {
						try {
							String fname = fileChooser.getSelectedFile().getAbsolutePath();
							if (fname != null){
								if (!fname.matches("(.*).xlsx"))
									fname = fname.concat(".xlsx");
								txtFilename.setText(fname);
							}
						} catch (Exception e) {
							
						}
					}
				}
			});
			
			txtFilename.setPreferredSize(new Dimension(200, txtFilename.getPreferredSize().height));
			txtFilename.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					checkFilename();
				}
				
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					checkFilename();
				}
				
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					checkFilename();
				}
			});
			
			rootp.add(centerp, BorderLayout.SOUTH);
			rootp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			JPanel buttp = new JPanel(new BorderLayout(10, 10));
			buttp.add(btnExport = new JButton("Export", null), BorderLayout.WEST);
			buttp.add(btnCancel = new JButton("Cancel", null), BorderLayout.EAST);
			
			btnExport.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (doExport())
						ExportDialog.this.setVisible(false);
				}
			});
			
			btnCancel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					ExportDialog.this.setVisible(false);
				}
			});
			
			JPanel buttwrapper = new JPanel(new BorderLayout(10, 10));
			buttwrapper.add(buttp, BorderLayout.EAST);
			buttwrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			cp.setLayout(new BorderLayout());
			cp.add(rootp, BorderLayout.NORTH);
			cp.add(buttwrapper, BorderLayout.SOUTH);
			
            pack();
			setLocationRelativeTo(userInterface.frame);
			
			checkFilename();
    	}//ExportDialog(Frame frame, String title, boolean modal)

		protected void checkFilename() {
			String fname = txtFilename.getText().trim();
			btnExport.setEnabled(fname.length() > 0);
			if (fname.length() > 0) {
				File file = new File(fname);
				btnExport.setEnabled(!file.isDirectory());
			} else {
				
			}
		}//checkFilename()

		protected boolean doExport() {
			File file = new File(txtFilename.getText().trim());
			
			if (file.exists()) {
				int n = JOptionPane.showConfirmDialog(
					    userInterface.frame,
					    "The file exists.Do you want to overwrite it ?",
					    "Confirm overwrite",
					    JOptionPane.YES_NO_OPTION);
				if(n == 1)
					return false;
			}
			
			// everything is good, let's do it
            class ExportProcessingDialog extends ProcessingDialog{
            	File exportFile;
            	public ExportProcessingDialog(JFrame owner, String title, String content,File file){
            		super(owner, title, content);
            		exportFile = file;
            	}
            	public void startExport() {
            		new Thread(new ExportWorker()).start();
            	}
            	class ExportWorker implements Runnable {
                    public Void doInBackground() {
                    	try{
                    		toExcel(myTable, exportFile);

            			} catch (Exception e)
            			{
            				e.printStackTrace();
            			}
                    	
                    	return null;
                    }
                    
                    public void done() {
            			synchronized (ExportProcessingDialog.this) {
            	        	alreadyDone = true;
            			}
            			ExportProcessingDialog.this.setVisible(false);
                    }

            		@Override
            		public void run() {
            			doInBackground();
            			done();
            		}
            	}
            }
            
	    	ExportProcessingDialog dlgExporting = new ExportProcessingDialog(userInterface.frame, "Exporting", "Exporting, please wait...", file);
	    	dlgExporting.startExport();
	    	dlgExporting.open();
/*	    	ProcessingDialog dlgCreatingTable = new ProcessingDialog(userInterface.frame, "Creating Table", "The table is being createded, please wait...", "");
	    	dlgCreatingTable.startCreatingTable();
	    	dlgCreatingTable.open();*/
			return true;
		}
		
		
		public void toExcel(JTable table, File file){

			
			Workbook wb = new SXSSFWorkbook();
			
			Sheet sheet = wb.createSheet();
			TableModel model = table.getModel();
			// header
			Row header = sheet.createRow(0);
			for (int i = 0; i < model.getColumnCount(); i++) {
				header.createCell(i).setCellValue(model.getColumnName(i));
			}
			header = null;
            // for the data in the table
			for (int i = 0; i < model.getRowCount(); i++) {
				Row row = sheet.createRow(i+1);
				for (int j = 0; j < model.getColumnCount(); j++) {
					Cell cell = row.createCell(j);
					Object objVal = model.getValueAt(i, j);
					String strVal = objVal.toString();
					if (model.getColumnClass(j) == Integer.class)
						cell.setCellValue(Integer.parseInt(strVal));
					else 
						cell.setCellValue(strVal);
					cell = null;
				}
				row = null;
			}
            
			// set column wide
			for (int i = 0; i < model.getColumnCount(); i++) {
				if (model.getColumnClass(i) == Date.class)
					sheet.setColumnWidth(i, 30 * 256);
				else {
					int columnlenth = model.getColumnName(i).toString().length() + 2;
					sheet.setColumnWidth(i, columnlenth * 256);
				}
			}
			
			try {
				 FileOutputStream out = new FileOutputStream(file);
			        wb.write(out);
			        out.close();

			} catch (IOException e) {
				System.out.println(e);
			}
			
	    }//toExcel()
	
    }//ExportDialog
    
    
    
    
    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
          showPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
          showPopup(e);
        }

        private void showPopup(MouseEvent e) {
          if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      }
    
    class MyTableModel extends AbstractTableModel {

        
        private String[] columnNames ;
        private Object[][] data;
        private Class[] columnClaz;
        
        public MyTableModel (String[] pColumnNames, Object[][] pData, Class[] pClass){
        	columnNames = pColumnNames;
        	data = pData;
        	columnClaz = pClass;
        }
        
        public void addRow(Object [] newData)
        {
        	data = Arrays.copyOf(data, data.length + 1);
            data[data.length - 1] = newData;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        
        public Class getColumnClass(int c) {
           // return getValueAt(0, c) == null?String.class: getValueAt(0, c).getClass();
        	return columnClaz[c];
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowTable(TableDemo newContentPane) {
        //Create and set up the window.
        JFrame frame = new JFrame("TableDemo");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    
    class MyDateRenderer extends DefaultTableCellRenderer {
        DateFormat formatter;
        public MyDateRenderer() { super(); }

        public void setValue(Object value) {
            if (formatter==null) {
                formatter = DateFormat.getDateTimeInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }


    

    
    
}
