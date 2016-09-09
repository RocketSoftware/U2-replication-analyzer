package com.rs.u2.repApp;

import java.io.*;
import javax.swing.*;
public class FileChooser {
	
	public static String chooseFile() {
		//FileReader fr;
		//PrintWriter outfile;
		String name ="";
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getAbsolutePath());
	    }
		try{
			
			name = chooser.getSelectedFile().getAbsolutePath() ;

			System.out.println(name);
	        
		}catch (java.lang.NullPointerException e3){
		    name = "File not found";
		} // end outer catch
		
		return name;
	}//end main

}// end class