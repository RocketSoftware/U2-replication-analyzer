package com.rs.u2.repApp;
import java.util.Date;

public class ReplicationData {
	private int groupNum ;
	private Date dateTime;
	private int pubDone;
	private int subGot;
	private int subAvail;
	private int subDone;
	private int liefStart;
	private int lefNumStart;
	private int lefNumLast;
	private int lefPositionStart;
	private int lefPositionEnd;
	private int pubDoneChange;
	private int subDoneChange;
	
	
	
	//groupNum
	public int getGroupNum(){
		return groupNum;
	}
	
	public void setGroupNum(int pGroupNum){
		groupNum = pGroupNum;
	}
	//dateTime
	public Date getDateTime(){
		return dateTime;
	}
	
	public void setDateTime(Date pdateTime){
		dateTime = pdateTime;
	}
	
	//pubDone
	public int getPubDone(){
		return pubDone;
	}
	
	public void setPubDone(int ppubDone){
		pubDone = ppubDone;
	}
	
	//subGot
	public int getSubGot(){
		return subGot;
	}
	
	public void setSubGot(int psubGot){
		subGot = psubGot;
	}
	
	//subAvail
	public int getSubAvail(){
		return subAvail;
	}
	
	public void setSubAvail(int psubAvail){
		subAvail = psubAvail;
	}
	
	//subDone
	public int getSubDone(){
		return subDone;
	}
	
	public void setSubDone(int psubDone){
		subDone = psubDone;
	}
	
	//liefStart
	public int getLiefStart(){
		return liefStart;
	}
	
	public void setLiefStart(int pliefStart){
		liefStart = pliefStart;
	}
	
	//lefNumStart
	public int getLefNumStart(){
		return lefNumStart;
	}
	
	public void setLefNumStart(int plefNumStart){
		lefNumStart = plefNumStart;
	}
	
	//lefNumLast
	public int getLefNumLast(){
		return lefNumLast;
	}
	
	public void setLefNumLast(int plefNumLast){
		lefNumLast = plefNumLast;
	}
	
	//lefPositionStart
	public int getLefPositionStart(){
		return lefPositionStart;
	}
	
	public void setLefPositionStart(int plefPositionStart){
		lefPositionStart = plefPositionStart;
	}
	
	//subDoneChange
	public int getSubDoneChange(){
		return subDoneChange;
	}
	
	public void setSubDoneChange(int pSubDoneChange){
		subDoneChange = pSubDoneChange;
	}
	//pubDoneChange
	public int getPubDoneChange(){
		return pubDoneChange;
	}
	
	public void setPubDoneChange(int pPubDoneChange){
		pubDoneChange = pPubDoneChange;
	}
	//
	public int getLefPositionEnd(){
		return lefPositionEnd;
	}
	
	public void setLefPositionEnd(int plefPositionEnd){
		lefPositionEnd = plefPositionEnd;
	}
	
	
	// PubDone - SubGot
	public int getPubDoneSubGot(){
		return pubDone - subGot;
	} 
	
	//SubGot - SubAvaible
	public int getSubGotSubAvailable(){
		return subGot -  subAvail;
	} 
	
	//SubAvaibl - SubDone
	public int getSubAvailableSubDone(){
		return subAvail - subDone;
	} 
	
	//PubDone - SubDone
	public int getPubDoneSubDone(){
		return pubDone - subDone;
	} 
}
