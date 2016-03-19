package com.ai.paas.ipaas.me.constant;

public class EventConstants {
	
	/**
	 * 消息队列的URL
	 */
	public static final String MESSAGE_SERVER = "http://{0}/put?topic={1}&channel={2}";
	public static final String MESSAGE_SERVER_HOST = "NSQ_HOST";
	public static final String MESSAGE_SERVER_TOPIC = "TOPIC";
	public static final String MESSAGE_SERVER_CHANNEL = "CHANNEL";
	
	
	
	public static final String DATA_CENTER = "DATA_CENTER";
	public static final String SOURCE_CENTER = "SOURCE_CENTER";
	
	
	//http://10.1.235.199:5051/slave(1)/state
	public static final String MESOS_SLAVE_SERVER = "http://{0}:{1}/slave(1)/state";
	public static final String MESOS_SLAVE_PORT = "MESOS_SLAVE_PORT";
	
	
	
	
	public static final String MESOS_EVENT_TYPE_4_STA = "status_update_event";
	public static final String NOT_NOTIFY = "-1";


	
	


}
