package com.example.cdt.model;



public class Coesui {
	
	private String userName;
	private String userId;
	private String deviceName;
	private String dateTime;
	private String parameter;
	private String metricvalue;
	private String location;
	
	
	public String getUserName() {   
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getMetricvalue() {
		return metricvalue;
	}
	public void setMetricvalue(String metricvalue) {
		this.metricvalue = metricvalue;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	@Override
	public String toString() {
		return "Coeusui [userName=" + userName + ", userId=" + userId + ", deviceName=" + deviceName + ", parameter="
				+ parameter + ", metricvalue=" + metricvalue + ", location=" + location + "]";
	}
	

}
