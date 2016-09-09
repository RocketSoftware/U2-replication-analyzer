package com.rs.u2.repApp;

import java.util.LinkedList;

public class ReplicationPerformanceInfo {
		private LinkedList<ReplicationData> replicationDataList;
		private ReplicationGeneralInfo replicationGeneralInfo;
		
		ReplicationPerformanceInfo()
		{
			replicationDataList = new LinkedList<ReplicationData>();
			replicationGeneralInfo = null;
		}
		
		//replicationDataList
		public LinkedList<ReplicationData> getReplicationDataList(){
			return replicationDataList;
		}
		
		public void setReplicationDataList(LinkedList<ReplicationData> preplicationDataList){
			replicationDataList = preplicationDataList;
		}
		
		//replicationGeneralInfo
		public ReplicationGeneralInfo getReplicationGeneralInfo(){
			return replicationGeneralInfo;
		}
		
		public void setReplicationGeneralInfo(ReplicationGeneralInfo preplicationGeneralInfo){
			replicationGeneralInfo = preplicationGeneralInfo;
		}
		
}
