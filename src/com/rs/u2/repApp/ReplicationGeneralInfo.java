package com.rs.u2.repApp;

public class ReplicationGeneralInfo {
		private int userTime;
		private int systemTime;
		private int TCAModulo;
		private int totalPage;
		private int TCAFileAccess;
		private int TCRCreate;
		private int TCRDrop;
		private int totalPubdone;
		private int totalSubdone;
		private int totalPubchange;
		private int totalSubchange;
		

		
		/*public ReplicationGeneralInfo(){
			totalPubdone = 0;
			totalSubdone = 0;
			totalPubchange = 0;
			totalSubchange = 0;
		}*/
		
		//userTime
		public int getUserTime(){
			return userTime;
		}
		
		public void setUserTime(int puserTime){
			userTime = puserTime;
		}
		
		//systemTime
		public int getSystemTime(){
			return systemTime;
		}
		
		public void setSystemTime(int psystemTime){
			systemTime = psystemTime;
		}
		
		//TCAModulo
		public int getTCAModulo(){
			return TCAModulo;
		}
		
		public void setTCAModulo(int pTCAModulo){
			TCAModulo = pTCAModulo;
		}
		
		//totalPage
		public int getTotalPage(){
			return totalPage;
		}
		
		public void setTotalPage(int ptotalPage){
			totalPage = ptotalPage;
		}
		
		//TCAFileAccess
		public int getTCAFileAccess(){
			return TCAFileAccess;
		}
		
		public void setTCAFileAccess(int pTCAFileAccess){
			TCAFileAccess = pTCAFileAccess;
		}
		
		//TCRCreate
		public int getTCRCreate(){
			return TCRCreate;
		}
		
		public void setTCRCreate(int pTCRCreate){
			TCRCreate = pTCRCreate;
		}
		
		//TCRDrop
		public int getTCRDrop(){
			return TCRDrop;
		}
		
		public void setTCRDrop(int pTCRDrop){
			TCRDrop = pTCRDrop;
		}
		
		// totalSubdone
		public int getTotalSubdone(){
			return totalSubdone;
		}
		
		public void setTotalSubdone(int pTotalSubdone){
			totalSubdone = pTotalSubdone;
		}
		// totalPubdone
		public int getTotalPubdone(){
			return totalPubdone;
		}
		
		public void setTotalPubdone(int pTotalPubdone){
			totalPubdone = pTotalPubdone;
		}
		

		// totalPubchange
		public int getTotalPubchange(){
			return totalPubchange;
		}
		
		public void setTotalPubchange(int pTotalPubchange){
			totalPubchange = pTotalPubchange;
		}
		
		//totalSubchange
		public int getTotalSubchange(){
			return totalSubchange;
		}
		
		public void setTotalSubchange(int pTotalSubchange){
			totalSubchange = pTotalSubchange;
		}
		
		
		//totalRepGap
		public int getTotalRepGap(){
			return totalPubdone - totalSubdone;
		}
		

		public void addTotalPubdone(int num){
			totalPubdone += num;
		}
		
		public void addTotalSubdone(int num){
			totalSubdone += num;
		}
		
}
