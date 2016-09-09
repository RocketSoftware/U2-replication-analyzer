package com.rs.u2.repApp;

import java.util.LinkedList;

public class ReplicationPerformanceLog {
	private LinkedList<ReplicationPerformanceInfo> replicationPerformanceInfoList;
	private int numOfGroups;
	
	ReplicationPerformanceLog(){
		replicationPerformanceInfoList = new LinkedList<ReplicationPerformanceInfo>();
		numOfGroups = 0;
	}
	public void addElement(ReplicationPerformanceInfo pReplicationPerformanceInfo)
	{
		replicationPerformanceInfoList.add(pReplicationPerformanceInfo);
	}
	
	public LinkedList<ReplicationPerformanceInfo> getReplicationPerformanceInfoList(){
		return replicationPerformanceInfoList;
	}
	
	public void setReplicationPerformanceInfoList(LinkedList<ReplicationPerformanceInfo> pReplicationPerformanceInfoList){
		replicationPerformanceInfoList = pReplicationPerformanceInfoList;
	}
	
	public int getNumOfGroups(){
		return numOfGroups;
	}
	
	public void setNumOfGroups(int pNum){
		numOfGroups = pNum;
	}

}
