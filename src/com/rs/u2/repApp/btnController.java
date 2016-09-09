package com.rs.u2.repApp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class btnController is used to response all the actions in the userInterface
 */
public class btnController {
	LogAnalysisTool userInterface;// Point to the userInterface to response
	private final int groupDataLength = 17;
	private final int genInfoLength = 12;
	private final int rawGrpDataLength = 11;
	private final int rawGenInfoLength = 7;

	TableDemo myTableDemo;
	LineChart myGenInfoChart;

	/**
	 * The Header for the general information in the table
	 */
	final String[] genInfoColumnNames = { "USER.TIME", "SYS.TIME",
			"TCA.MODULO", "TOTAL.PG", "TCA.FILE.ACCESS", "TCR.CREATE",
			"TCR.DROP", "TOTAL.PUBDONE", "TOTAL.SUBDONE", "TOTAL.PUBCHANGE",
			"TOTAL.SUBCHANGE", "TOTAL.REPGAP" };
	final Class[] genInfoColumnClaz = { Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class };
	/**
	 * The Header for the group information in the table
	 */
	final String[] grpInfoColumnNames = { "GROUP.NO", "DATE.TIME", "PUB.DONE",
			"SUB.GOT", "SUB.AVAIL", "SUB.DONE", "LIEFSTART", "LEFNO.START",
			"LEFNO.LAST", "LEFPOS.START", "LEFPOS.END", "PUB.DONE -SUB.GOT",
			"SUB.GOT - SUB.AVAIL", "SUB.AVAIL - SUB.DONE",
			"PUB.DONE - SUB.DONE", "PUB.DONE.CHANGE", "SUB.DONE.CHANGE" };
	final Class[] grpInfoColumnClaz = { Integer.class, Date.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class, Integer.class,
			Integer.class, Integer.class, Integer.class };

	/**
	 * Initialize the btnController by giving it a userInterface
	 * 
	 * @param pLogAnalysisTool
	 */
	public btnController(LogAnalysisTool pLogAnalysisTool) {
		userInterface = pLogAnalysisTool;
	}

	/**
	 * To responds the action of clicking "Import" in the "File" menu.
	 */
	public void menuItmImport_Click() {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				".log File", "log");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setLocation(userInterface.jPnlChartSummaryTabSummary
				.getLocation());
		// String fileName = FileChooser.chooseFile();

		int retval = fileChooser.showOpenDialog(null);
		if (retval == JFileChooser.APPROVE_OPTION) {
			try {
				String fileName = fileChooser.getSelectedFile()
						.getAbsolutePath();
				importFile(fileName);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		System.gc();

	} // end menuItmImport_Click()

	/**
	 * To responds the action of clicking "Remote Import" in the "File" menu.
	 */
	public void menuItmRemote_Click() {
		FTPPanel pnlFTP = new FTPPanel(this);
		pnlFTP.parentFrame.setLocationRelativeTo(userInterface.frame);
		pnlFTP.parentFrame.setSize(300, 200);
		System.gc();
	}

	/**
	 * To responds the action of clicking "Export" in the "File" menu.
	 */
	public void menuItmExport_Click() {
		if (userInterface.replicationLog.getReplicationPerformanceInfoList()
				.size() == 0) {
			String str = "You have to import a .log file first!";
			JOptionPane.showMessageDialog(null, str);
			return;
		}
		showExportDialog();
		System.gc();
	} // end menuItmExport_Click()

	/**
	 * To responds the action of clicking "Update The Table" button in the Table
	 * Tab.
	 */
	public void btnGenerateTable_Click() {
		if (userInterface.lstGenerateTable.getSelectedIndices().length == 0) {
			String str = "You have to choose something to update the table!";
			JOptionPane.showMessageDialog(null, str);
		} else {
			ProcessingDialog dlgCreatingTable = new ProcessingDialog(
					userInterface.frame, "Creating Table",
					"The table is being createded, please wait...", "", this);
			dlgCreatingTable.startCreatingTable();
			dlgCreatingTable.open();
			System.gc();
		}

	} // end btnGenerateTable_Click()

	/**
	 * To responds the action of clicking "Update The Chart" button in the Chart
	 * Tab.
	 */
	public void btnCreateChart_Click() {
		if (userInterface.lstChartAttr.getSelectedIndices().length == 0) {
			String str = "You have to choose at least one attribute to update the chart!";
			JOptionPane.showMessageDialog(null, str);
			if (userInterface.lstChartGroups.getSelectedIndices().length == 0) {
				String str2 = "You have to choose at least one group to update the chart!";
				JOptionPane.showMessageDialog(null, str2);
			}
		} else if (userInterface.lstChartGroups.getSelectedIndices().length == 0) {
			String str = "You have to choose at least one group to update the chart!";
			JOptionPane.showMessageDialog(null, str);
		} else {
			ProcessingDialog dlgCreatingGrpChart = new ProcessingDialog(
					userInterface.frame, "Creating Chart",
					"The Chart is being createded, please wait...", "", this);
			dlgCreatingGrpChart.startCreatingGrpChart();
			dlgCreatingGrpChart.open();
		}
		System.gc();

	} // end btnCreateChart_Click()

	/**
	 * To responds the action of clicking "Update The Chart" button in the
	 * Summary Tab.
	 */
	public void btnCreateGenInfoChart_Click() {
		if (userInterface.lstChartGenInfo.getSelectedIndices().length == 0) {
			String str = "You have to choose something to update the Summary Chart!";
			JOptionPane.showMessageDialog(null, str);
		} else {
			ProcessingDialog dlgCreatingGenInfoChart = new ProcessingDialog(
					userInterface.frame, "Creating Chart",
					"The Chart is being createded, please wait...", "", this);
			dlgCreatingGenInfoChart.startCreatingGenChart();
			dlgCreatingGenInfoChart.open();
		}
		System.gc();
	}// end btnCreateGenInfoChart_Click()

	/**
	 * create and return a group ReplicationData from a string[] , by know the start point of the
	 * first field and return the new ReplicationData. The String[] is one line
	 * of the Log file.
	 * 
	 * @param eachGenData
	 * @param startIndex
	 * @param s
	 * @return
	 */
	private ReplicationData processGroupInfo(
			ReplicationGeneralInfo eachGenData, int startIndex, String[] s) {
		ReplicationData eachGrpData = new ReplicationData();
		int i = 1;
		try {
			eachGrpData.setGroupNum(Integer.parseInt(s[startIndex]));
			eachGrpData.setDateTime(new SimpleDateFormat(
					"HH:mm:ss dd MMM yyyy", Locale.ENGLISH)
					.parse(s[startIndex + 1]));
			eachGrpData.setPubDone(Integer.parseInt(s[startIndex + 2]));
			eachGrpData.setSubGot(Integer.parseInt(s[startIndex + 3]));
			eachGrpData.setSubAvail(Integer.parseInt(s[startIndex + 4]));
			eachGrpData.setSubDone(Integer.parseInt(s[startIndex + 5]));
			eachGrpData.setLiefStart(Integer.parseInt(s[startIndex + 6 + i]));
			eachGrpData.setLefNumStart(Integer.parseInt(s[startIndex + 7 + i]));
			eachGrpData.setLefNumLast(Integer.parseInt(s[startIndex + 8 + i]));
			eachGrpData.setLefPositionStart(Integer.parseInt(s[startIndex + 9
					+ i]));
			eachGrpData.setLefPositionEnd(Integer.parseInt(s[startIndex + 10
					+ i]));
			eachGenData.addTotalPubdone(eachGrpData.getPubDone());
			eachGenData.addTotalSubdone(eachGrpData.getSubDone());
		} catch (ParseException e) {
			System.out.println("Exception :" + e);

		}

		return eachGrpData;

	}// end ReplicationData processGroupInfo(int startIndex, String[] s)

	/**
	 * create a ReplicationGeneralInfo from a string[] , by know the start point
	 * of the first field. and return the new ReplicationGeneralInfo. The
	 * String[] is one line of the Log file.
	 * 
	 * @param eachGenData
	 * @param startIndex
	 * @param s
	 */
	private void processGeneralInfo(ReplicationGeneralInfo eachGenData,
			int startIndex, String[] s) {

		eachGenData.setUserTime(Integer.parseInt(s[startIndex]));
		eachGenData.setSystemTime(Integer.parseInt(s[startIndex + 1]));
		eachGenData.setTCAModulo(Integer.parseInt(s[startIndex + 2]));
		eachGenData.setTotalPage(Integer.parseInt(s[startIndex + 3]));
		eachGenData.setTCAFileAccess(Integer.parseInt(s[startIndex + 4]));
		eachGenData.setTCRCreate(Integer.parseInt(s[startIndex + 5]));
		eachGenData.setTCRDrop(Integer.parseInt(s[startIndex + 6]));

	}// end ReplicationGeneralInfo processGeneralInfo(int startIndex,String[] s)

	/**
	 * Display the number of groups and the number of records in the label at
	 * the bottom of the application.
	 */
	private void displayGenInfo() {
		// clear the label.
		userInterface.lblGenInfo.setText("");
		String s = "";
		s += "" + "Number of Groups : "
				+ userInterface.replicationLog.getNumOfGroups();
		s += "            "
				+ "Number of Records : "
				+ userInterface.replicationLog
						.getReplicationPerformanceInfoList().size();
		userInterface.lblGenInfo.setText(s);

	}// end displayGenInfo()

	/**
	 * This function is used to set up the value of PubDone Change and SubDone
	 * Change for each group. PubDone Change = Current PubDone - Previous
	 * PubDone SubDone Change = Current SubDone = Previous SubDone
	 * 
	 * @param pLog
	 */
	private void setUpGrpChangeValues(ReplicationPerformanceLog pLog) {
		int numOfRecords = pLog.getReplicationPerformanceInfoList().size();
		for (int j = 0; j < pLog.getNumOfGroups(); j++) {
			for (int i = 1; i < numOfRecords; i++) {
				ReplicationData preGrpData = pLog
						.getReplicationPerformanceInfoList().get(i - 1)
						.getReplicationDataList().get(j);
				ReplicationData curGrpData = pLog
						.getReplicationPerformanceInfoList().get(i)
						.getReplicationDataList().get(j);
				// set the value of PubDonechange
				int pubChange = curGrpData.getPubDone()
						- preGrpData.getPubDone();
				curGrpData.setPubDoneChange(pubChange);
				// set the value of SubDonechange
				int subChange = curGrpData.getSubDone()
						- preGrpData.getSubDone();
				curGrpData.setSubDoneChange(subChange);
			}
		}
	}// end setUpGrpChangeValues()

	/**
	 * This function is used to set up the value of Total PubDone Change and
	 * Total SubDone Change. PubDone Change = Current PubDone - Previous PubDone
	 * SubDone Change = Current SubDone = Previous SubDone
	 * 
	 * @param pLog
	 */
	private void setUpGenChangeValues(ReplicationPerformanceLog pLog) {
		int numOfRecords = pLog.getReplicationPerformanceInfoList().size();
		for (int i = 1; i < numOfRecords; i++) {
			ReplicationGeneralInfo preGenInfo = pLog
					.getReplicationPerformanceInfoList().get(i - 1)
					.getReplicationGeneralInfo();
			ReplicationGeneralInfo curGenInfo = pLog
					.getReplicationPerformanceInfoList().get(i)
					.getReplicationGeneralInfo();
			// set the value of PubDonechange
			int pubChange = curGenInfo.getTotalPubdone()
					- preGenInfo.getTotalPubdone();
			curGenInfo.setTotalPubchange(pubChange);
			// set the value of SubDonechange
			int subChange = curGenInfo.getTotalSubdone()
					- preGenInfo.getTotalSubdone();
			curGenInfo.setTotalSubchange(subChange);

		}
	}// end setUpChangeValues(ReplicationPerformanceLog pLog)

	/**
	 * This function sets up the user interface and import the file by calling
	 * readFile(String fileName) in the background.
	 * 
	 * @param fileName
	 */
	public void importFile(String fileName) {
		if (fileName != null) {
			// Clear the Lists
			userInterface.lstMdlChartGenInfo.removeAllElements();
			userInterface.lstMdlGenerateTable.removeAllElements();
			userInterface.lstMdlChartAttr.removeAllElements();
			userInterface.lstMdlChartGroups.removeAllElements();

			String appTitle = userInterface.appName + " - " + fileName;
			userInterface.frame.setTitle(appTitle);
			userInterface.jPnlSummaryTabWrapper.setVisible(false);
			userInterface.jPnlTableTabWrapper.setVisible(false);
			userInterface.jPnlChartTabWrapper.setVisible(false);
			userInterface.lblGenInfo.setText("");

			// pop a dialog to let the user wait
			ProcessingDialog dlgImporting = new ProcessingDialog(
					userInterface.frame, "Importing",
					"The File is being imported, please wait...", fileName,
					this);
			dlgImporting.startImporting();
			dlgImporting.open();

		}
	}// end importFile(String fileName)

	/**
	 * This function read the file line by line and store the data in Java
	 * objects.
	 */
	void readFile(String fileName) {
		FileReader fr;
		BufferedReader br;
		String name = fileName;
		int numOfGroups = 0;

		String context = "";
		try {
			fr = new FileReader(name);
			br = new BufferedReader(fr);
			String s;
			// newLog is used to store everything.
			ReplicationPerformanceLog newLog = new ReplicationPerformanceLog();
			try {
				// count the number of groups.
				if ((s = br.readLine()) != null) {
					String[] header = s.split("\t");
					for (int i = 1; i < header.length; i++) {
						if (header[i].equals("GROUP.NO"))
							numOfGroups++;
					}
					// set numOfGroups in ReplicationPerformanceLog
					newLog.setNumOfGroups(numOfGroups);
				}

				// read line by line and store them.
				while ((s = br.readLine()) != null) {
					String[] line = s.split("\t");
					// Process group info
					ReplicationPerformanceInfo aNewReplicationPerformanceInfo = new ReplicationPerformanceInfo();
					ReplicationGeneralInfo eachGenData = new ReplicationGeneralInfo();
					for (int i = 1; i < line.length - rawGenInfoLength; i += rawGrpDataLength)
					// inside of this for , add group information data.
					{
						aNewReplicationPerformanceInfo.getReplicationDataList()
								.add(processGroupInfo(eachGenData, i, line));

					}
                    
					//set up the data of general information
					processGeneralInfo(eachGenData, line.length
							- rawGenInfoLength, line);
					// add the general information
					aNewReplicationPerformanceInfo
							.setReplicationGeneralInfo(eachGenData);
					//add the whole Replication Performance Information (one line)
					newLog.addElement(aNewReplicationPerformanceInfo);

				}// end while

				// set up the totalPubchange and totalSubchange for each record
				setUpGenChangeValues(newLog);
				setUpGrpChangeValues(newLog);

				userInterface.replicationLog = newLog;
				br.close();
				fr.close();
			} catch (IOException e1) {
				System.out.println("IO exception");
			}

		} catch (FileNotFoundException e2) {
			System.out.println("File not found");

		} catch (java.lang.NullPointerException e3) {

		} // end try
        
		//display the number of groups and the number of records in the label at the bottom of the application.
		displayGenInfo();
	}// readFile(String fileName)


	/**
	 * addGenInfoData(Object[] newData, int recordNum) and addGroupData(Object[] newData, int GroupNumber,
	 *		int recordNum) is used for prepare the data for the table.
	 * @param newData
	 * @param recordNum
	 * @return
	 */
	private Object[] addGenInfoData(Object[] newData, int recordNum) {
		int oldLength = newData.length;
		newData = Arrays.copyOf(newData, oldLength + genInfoLength);
		ReplicationGeneralInfo genInfo = userInterface.replicationLog
				.getReplicationPerformanceInfoList().get(recordNum)
				.getReplicationGeneralInfo();
		newData[oldLength] = genInfo.getUserTime();
		newData[oldLength + 1] = genInfo.getSystemTime();
		newData[oldLength + 2] = genInfo.getTCAModulo();
		newData[oldLength + 3] = genInfo.getTotalPage();
		newData[oldLength + 4] = genInfo.getTCAFileAccess();
		newData[oldLength + 5] = genInfo.getTCRCreate();
		newData[oldLength + 6] = genInfo.getTCRDrop();
		newData[oldLength + 7] = genInfo.getTotalPubdone();
		newData[oldLength + 8] = genInfo.getTotalSubdone();
		newData[oldLength + 9] = genInfo.getTotalPubchange();
		newData[oldLength + 10] = genInfo.getTotalSubchange();
		newData[oldLength + 11] = genInfo.getTotalRepGap();
		return newData;
	}// end addGenInfoData(Object[] newData , int recordNum)

	/**
	 * addGenInfoData and addGroupData is used for prepare the data for the table.
	 * @param newData
	 * @param GroupNumber
	 * @param recordNum
	 * @return
	 */
	private Object[] addGroupData(Object[] newData, int GroupNumber,
			int recordNum) {
		int oldLength = newData.length;
		newData = Arrays.copyOf(newData, oldLength + groupDataLength);
		ReplicationData oneGroup = userInterface.replicationLog
				.getReplicationPerformanceInfoList().get(recordNum)
				.getReplicationDataList().get(GroupNumber);
		newData[oldLength] = oneGroup.getGroupNum();
		newData[oldLength + 1] = oneGroup.getDateTime();
		newData[oldLength + 2] = oneGroup.getPubDone();
		newData[oldLength + 3] = oneGroup.getSubGot();
		newData[oldLength + 4] = oneGroup.getSubAvail();
		newData[oldLength + 5] = oneGroup.getSubDone();
		newData[oldLength + 6] = oneGroup.getLiefStart();
		newData[oldLength + 7] = oneGroup.getLefNumStart();
		newData[oldLength + 8] = oneGroup.getLefNumLast();
		newData[oldLength + 9] = oneGroup.getLefPositionStart();
		newData[oldLength + 10] = oneGroup.getLefPositionEnd();
		newData[oldLength + 11] = oneGroup.getPubDoneSubGot();
		newData[oldLength + 12] = oneGroup.getSubGotSubAvailable();
		newData[oldLength + 13] = oneGroup.getSubAvailableSubDone();
		newData[oldLength + 14] = oneGroup.getPubDoneSubDone();
		newData[oldLength + 15] = oneGroup.getPubDoneChange();
		newData[oldLength + 16] = oneGroup.getSubDoneChange();
		return newData;
	}// end addGroupData(Object[] newData , int GroupNumber , int recordNum)
    
	/**
	 * According to the choices in the list to create and return a TableDemo.
	 * @param pList
	 * @return
	 */
	private TableDemo prepareTable(JList pList) {
		/* Get the Column Names */
		// array selection[i-1] is used to store whether the ith item is
		// selected ,
		// if it is selected , it should be one , otherwise it should be 0
		int[] selection = new int[userInterface.replicationLog.getNumOfGroups() + 1];
		for (int i = 0; i < selection.length; i++) {
			selection[i] = 0;
		}// end for

		int[] selectedIndex = pList.getSelectedIndices();

		for (int i = 0; i < selection.length; i++) {
			for (int j = 0; j < selectedIndex.length; j++) {
				if (i == selectedIndex[j]) {
					selection[i] = 1;
				}
			}// end for
		}// end for for
		String[] columnNames = {};
		Class[] columnClaz = {};
		// generate the columns Names for the Groups
		if (selectedIndex.length > selection[selection.length - 1]) {
			// There is some of Group Numbers selected
			for (int i = 0; i < selectedIndex.length
					- selection[selection.length - 1]; i++) {
				int oldLength = columnNames.length;
				columnNames = Arrays.copyOf(columnNames, oldLength
						+ groupDataLength);
				columnClaz = Arrays.copyOf(columnClaz, oldLength
						+ groupDataLength);
				// System.out.println(oldLength);
				// System.out.println(columnNames.length);

				for (int j = 0; j < groupDataLength; j++) {
					columnNames[oldLength + j] = grpInfoColumnNames[j];
					columnClaz[oldLength + j] = grpInfoColumnClaz[j];
				}
			}
		}
		// generate the columns Names General Information
		if (selection[selection.length - 1] == 1) {
			// General Information is selected
			columnNames = Arrays.copyOf(columnNames, columnNames.length
					+ genInfoLength);
			columnClaz = Arrays.copyOf(columnClaz, columnClaz.length
					+ genInfoLength);
			for (int j = 0; j < genInfoLength; j++) {
				columnNames[columnNames.length - genInfoLength + j] = genInfoColumnNames[j];
				columnClaz[columnClaz.length - genInfoLength + j] = genInfoColumnClaz[j];
			}

		}

		/* Get the data */
		Object[][] data = {};
		TableDemo oneTableDemo = new TableDemo(userInterface, columnNames,
				data, columnClaz);
		for (int i = 0; i < userInterface.replicationLog
				.getReplicationPerformanceInfoList().size(); i++) {

			// get the data for Group
			Object[] newData = {};
			for (int j = 0; j < selection.length - 1; j++) {
				if (selection[j] == 1) {
					newData = addGroupData(newData, j, i);
				}
			}
			// get the data for General Information
			if (selection[selection.length - 1] == 1) {
				newData = addGenInfoData(newData, i);
			}

			oneTableDemo.addData(newData);
		}
		return oneTableDemo;

	}// end prepareTable(JList pList)

	/**
	 * create a table and update the user interface
	 */
	void generateTable() {
		//create myTableDemo
		myTableDemo = prepareTable(userInterface.lstGenerateTable);
		//addTable adds myTableDemo as the TableDemo in the main frame.
		userInterface.jPnlTableTabTable.addTable(myTableDemo);
		//setdialogTable set another TableDemo for the Dragged out dialog. 
		userInterface.jPnlTableTabTable.setdialogTable(new TableDemo(
				userInterface, myTableDemo.myTableModel));
		
	}// end private void generateTable()

	/**
	 * To create a Line Chart based on the choices that the used choose
	 */
	void createChart() {
		myGenInfoChart = new LineChart();

		// array grpSelection[i-1] is used to store whether the ith item is
		// selected ,
		// if it is selected , it should be one , otherwise it should be 0

		int[] grpSelectedIndex = userInterface.lstChartGroups
				.getSelectedIndices();
		int[] attrSelectedIndex = userInterface.lstChartAttr
				.getSelectedIndices();
		// generate the names for each series in the Line Chart
		String[] seriesNames = new String[grpSelectedIndex.length
				* attrSelectedIndex.length];
		// get the data for each series
		for (int i = 0; i < grpSelectedIndex.length; i++)
			for (int j = 0; j < attrSelectedIndex.length; j++) {
				seriesNames[i * attrSelectedIndex.length + j] = "GROUP.NO*"
						+ Integer.toString(grpSelectedIndex[i]) + "("
						+ grpInfoColumnNames[11 + attrSelectedIndex[j]] + ")";
				double[][] data = new double[2][userInterface.replicationLog
						.getReplicationPerformanceInfoList().size()];
				for (int k = 0; k < userInterface.replicationLog
						.getReplicationPerformanceInfoList().size(); k++) {
					ReplicationData oneGroup = userInterface.replicationLog
							.getReplicationPerformanceInfoList().get(k)
							.getReplicationDataList().get(grpSelectedIndex[i]);
					data[0][k] = k;
					switch (attrSelectedIndex[j]) {
					case 0:
						data[1][k] = oneGroup.getPubDoneSubGot();
						break;
					case 1:
						data[1][k] = oneGroup.getSubGotSubAvailable();
						break;
					case 2:
						data[1][k] = oneGroup.getSubAvailableSubDone();
						break;
					case 3:
						data[1][k] = oneGroup.getPubDoneSubDone();
						break;
					case 4:
						data[1][k] = oneGroup.getPubDoneChange();
						break;
					case 5:
						data[1][k] = oneGroup.getSubDoneChange();
						break;
					default:
						data[1][k] = 0.0;
						break;

					}// end switch

				}// end for

				myGenInfoChart.addSerie(seriesNames[i
						* attrSelectedIndex.length + j], data);

			}
		// myGenInfoChart.draw();
		userInterface.jPnlChartTabChart.addChart(myGenInfoChart.getOneChart());
		// set up the myChartPanel in DragPane
		userInterface.jPnlChartTabChart.setDialogChartPanel(myGenInfoChart
				.getOneChart());

	}// end createChart()

	/**
	 * prepare the data for the General Information Chart
	 * @param genInfoSelectedIndex
	 * @param seriesNames
	 */
	private void prepareGenInfoChart(int[] genInfoSelectedIndex,
			String[] seriesNames) {

		myGenInfoChart = new LineChart();
		// get the data for each series
		for (int i = 0; i < genInfoSelectedIndex.length; i++) {
			seriesNames[i] = "GENERAL.INFO*" + "("
					+ genInfoColumnNames[9 + genInfoSelectedIndex[i]] + ")";
			double[][] data = new double[2][userInterface.replicationLog
					.getReplicationPerformanceInfoList().size()];
			for (int k = 0; k < userInterface.replicationLog
					.getReplicationPerformanceInfoList().size(); k++) {
				ReplicationGeneralInfo oneGenInfo = userInterface.replicationLog
						.getReplicationPerformanceInfoList().get(k)
						.getReplicationGeneralInfo();
				data[0][k] = k;
				switch (genInfoSelectedIndex[i]) {
				case 0:
					data[1][k] = oneGenInfo.getTotalPubchange();
					break;
				case 1:
					data[1][k] = oneGenInfo.getTotalSubchange();
					break;
				case 2:
					data[1][k] = oneGenInfo.getTotalRepGap();
					break;
				default:
					data[1][k] = 0.0;
					break;

				}// end switch

			}// end for

			myGenInfoChart.addSerie(seriesNames[i], data);

		}
	}// end prepareGenInfoChart

	/**
	 * create the general information chart.
	 */
	void createGenInfoChart() {

		// array genInfoSelection[i-1] is used to store whether the ith item is
		// selected ,
		// if it is selected , it should be one , otherwise it should be 0

		int[] genInfoSelectedIndex = userInterface.lstChartGenInfo
				.getSelectedIndices();
		createGenInfoChart(genInfoSelectedIndex);
		// generate the names for each series in the Line Chart

	}// createGenInfoChart()

	/**
	 * create the general information chart based the choices in the list.
	 * @param pSelection
	 */
	private void createGenInfoChart(int[] pSelection) {

		// array genInfoSelection[i-1] is used to store whether the ith item is
		// selected ,
		// if it is selected , it should be one , otherwise it should be 0

		int[] genInfoSelectedIndex = pSelection;
		// generate the names for each series in the Line Chart
		String[] seriesNames = new String[genInfoSelectedIndex.length];
		// get the data for each series

		prepareGenInfoChart(genInfoSelectedIndex, seriesNames);
		// show the Chart
		userInterface.jPnlChartSummaryTabSummary.addChart(myGenInfoChart
				.getOneChart());
		// set up the myChartPanel in DragPane
		userInterface.jPnlChartSummaryTabSummary
				.setDialogChartPanel(myGenInfoChart.getOneChart());
	}// end createGenInfoChart(pSelection)
    
	/**
	 * Initialize the Summary Tab, Table Tab and Chart Tab 
	 */
	void initializeTabbedPane() {
		// Set the correct context in the JList
		// Lists for groups
		String tmpStr = "";
		for (int i = 0; i < userInterface.replicationLog.getNumOfGroups(); i++) {
			tmpStr = "Group Number " + i;
			userInterface.lstMdlGenerateTable.addElement(tmpStr);
			userInterface.lstMdlChartGroups.addElement(tmpStr);
		}

		tmpStr = "General Information";
		userInterface.lstMdlGenerateTable.addElement(tmpStr);

		// List for Chart Attributes
		userInterface.lstMdlChartAttr.addElement("PUB.DONE - SUB.GOT");
		userInterface.lstMdlChartAttr.addElement("SUB.GOT - SUB.AVAIL");
		userInterface.lstMdlChartAttr.addElement("SUB.AVAIL - SUB.DONE");
		userInterface.lstMdlChartAttr.addElement("PUB.DONE - SUB.DONE");
		userInterface.lstMdlChartAttr.addElement("PUB.DONE.CHANGE");
		userInterface.lstMdlChartAttr.addElement("SUB.DONE.CHANGE");

		// List for General Information
		userInterface.lstMdlChartGenInfo.addElement("TOTAL.PUBCHANGE");
		userInterface.lstMdlChartGenInfo.addElement("TOTAL.SUBCHANGE");
		userInterface.lstMdlChartGenInfo.addElement("TOTAL.REPGAP");
		// end of setting up the JList

		// enable the button for Generating Table
		userInterface.btnGenerateTable.setEnabled(true);
		// enable the button for Creating Chart
		userInterface.btnCreateChart.setEnabled(true);
		// enable the button for Creating General Information Chart
		userInterface.btnCreateGenInfoChart.setEnabled(true);

		// make the jPnlSummaryTab, jPnlChartTab visible
		// userInterface.infoPaneWrapper.setVisible(true);
		userInterface.chartPaneWrapper.setVisible(true);
		userInterface.tablePaneWrapper.setVisible(true);

		// Summary Tab

		int[] SummaryInt = { 2 };
		createGenInfoChart(SummaryInt);
		userInterface.lstChartGenInfo.setSelectedIndices(SummaryInt);
		// Table Tab
		userInterface.lstGenerateTable.setSelectedIndex(0);
		generateTable();
		// Chart Tab
		userInterface.lstChartAttr.setSelectedIndex(0);
		userInterface.lstChartGroups.setSelectedIndex(0);
		createChart();
	} //end initializeTabbedPane()

	/**
	 * show the dialog for exporting the table.
	 */
	private void showExportDialog() {
		// TODO Auto-generated method stub
		JDialog DialogChooser = new JDialog();
		Container cp = DialogChooser.getContentPane();
		JPanel rootp = new JPanel(new BorderLayout());
		JPanel lstWraper = new JPanel(new BorderLayout());
		JPanel btnWraper = new JPanel(new BorderLayout());

		JButton btnNext = new JButton("NEXT");

		btnWraper.add(btnNext, BorderLayout.EAST);

		JScrollPane sclPane = new JScrollPane();
		DefaultListModel lstMdlExportTableGrps = userInterface.lstMdlGenerateTable;
		final JList lstExportTableGrps = new JList(lstMdlExportTableGrps);
		sclPane.setViewportView(lstExportTableGrps);

		lstWraper.add(sclPane, BorderLayout.EAST);
		rootp.add(lstWraper, BorderLayout.SOUTH);
		rootp.add(new JLabel("Chooser what to Export", JLabel.CENTER),
				BorderLayout.NORTH);

		cp.add(rootp, BorderLayout.NORTH);
		cp.add(btnWraper, BorderLayout.SOUTH);
		DialogChooser.pack();
		DialogChooser.setVisible(true);
		DialogChooser.setLocation(
				userInterface.tabbedPane.getLocationOnScreen().x,
				userInterface.tabbedPane.getLocationOnScreen().y);

		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (lstExportTableGrps.getSelectedIndices().length == 0) {
					String str = "You have to choose something to export!";
					JOptionPane.showMessageDialog(null, str);
				} else {
					TableDemo oneTableDemo = prepareTable(lstExportTableGrps);
					oneTableDemo.createExportDialog();
				}

			}
		});// end btnNext.addActionListener

	}// end howExportDialog()

}// end btnController{}

