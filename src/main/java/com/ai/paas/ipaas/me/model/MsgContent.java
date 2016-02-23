package com.ai.paas.ipaas.me.model;

public class MsgContent {
	private String dockerImage;
	private String dockerName;
	private String host;
	private String taskStatus;
	private String timestamp;
	
	private String sourceCenter;
	private String dataCenter;
	public String getDockerImage() {
		return dockerImage;
	}
	public void setDockerImage(String dockerImage) {
		this.dockerImage = dockerImage;
	}
	public String getDockerName() {
		return dockerName;
	}
	public void setDockerName(String dockerName) {
		this.dockerName = dockerName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSourceCenter() {
		return sourceCenter;
	}
	public void setSourceCenter(String sourceCenter) {
		this.sourceCenter = sourceCenter;
	}
	public String getDataCenter() {
		return dataCenter;
	}
	public void setDataCenter(String dataCenter) {
		this.dataCenter = dataCenter;
	}
	
	@Override
	public String toString() {
		return "{dockerImage=" + dockerImage + ", dockerName="
				+ dockerName + ", host=" + host + ", taskStatus=" + taskStatus
				+ ", timestamp=" + timestamp + ", sourceCenter=" + sourceCenter
				+ ", dataCenter=" + dataCenter + "}";
	}
	
	

}
