package com.ai.paas.ipaas.me.service.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.me.constant.EventConstants;
import com.ai.paas.ipaas.me.model.MsgContent;
import com.ai.paas.ipaas.me.model.StateResp;
import com.ai.paas.ipaas.me.model.StateResp.FrameWork;
import com.ai.paas.ipaas.me.model.StateResp.FrameWork.Executor;
import com.ai.paas.ipaas.me.service.IEventSv;
import com.ai.paas.ipaas.me.util.ConfUtil;
import com.ai.paas.ipaas.me.util.GsonUtil;
import com.ai.paas.ipaas.me.util.HttpUtil;
import com.ai.paas.ipaas.me.util.ParamUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class EventSvImp implements IEventSv {
	private static final transient Logger LOG = LoggerFactory
			.getLogger(EventSvImp.class);

	/**
	 * 1、参数 2、获取相应参数 发消息 NSQ
	 * 
	 * @param request
	 */
	@Override
	public boolean sendMsg(HttpServletRequest request) {
//		printParams(request);
		try {
			String msg = getMsg(request);
			if(EventConstants.NOT_NOTIFY.equals(msg))
				return false;
			sendMsg(msg);
			return true;
		} catch (Exception e) {
			LOG.error("--"+e.getMessage(),e);		
		}
		return false;
	}

	private void sendMsg(String message) {
		String url = ParamUtil
				.fillStringByArgs(
						EventConstants.MESSAGE_SERVER,
						new String[] {
								ConfUtil.getProperty(EventConstants.MESSAGE_SERVER_HOST),
								ConfUtil.getProperty(EventConstants.MESSAGE_SERVER_TOPIC),
								ConfUtil.getProperty(EventConstants.MESSAGE_SERVER_CHANNEL)});
		HttpUtil.sendMessageToEndPoint(url, message);
	}

	private String getMsg(HttpServletRequest request) {
		String res = "";
		try {
			StringBuilder sb = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			LOG.debug("--------InputStream--{}---------",sb.toString());
			MsgContent content = new MsgContent();

			JsonParser parser = new JsonParser();
			JsonElement je = parser.parse(sb.toString());
			JsonObject in = je.getAsJsonObject();
			String event = in.get("eventType").getAsString();
			if(!EventConstants.MESOS_EVENT_TYPE_4_STA.equals(event))
				return EventConstants.NOT_NOTIFY;
			String  taskStatus = in.get("taskStatus").getAsString();
			String dockerImage = in.get("appId").getAsString();
			String host = in.get("host").getAsString();
			String timestamp = in.get("timestamp").getAsString();
			LOG.debug("--------timestamp--{}---------",timestamp);

			String dockerName = in.get("slaveId").getAsString();
			String taskId = in.get("taskId").getAsString();
			content.setDockerImage(dockerImage);
			content.setDockerName(handleName(dockerName,host,taskId));
			content.setHost(host);
			content.setTaskStatus(handleStatus(taskStatus));
			content.setTimestamp(convetTime(timestamp));
			content.setDataCenter(ConfUtil.getProperty(EventConstants.DATA_CENTER));
			content.setSourceCenter(ConfUtil.getProperty(EventConstants.SOURCE_CENTER));
			res = GsonUtil.objToGson(content);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return res;
	}

	private String handleStatus(String taskStatus) {
		String sta = "KILLED";
		switch (taskStatus) {
		case "TASK_STAGING":
			sta = "KILLED";
			break;
		case "TASK_STARTING":
			sta = "RUNNING";
			break;
		case "TASK_RUNNING":
			sta = "RUNNING";
			break;
		case "TASK_FINISHED":
			sta = "KILLED";
			break;
		case "TASK_FAILED":
			sta = "KILLED";
			break;
		case "TASK_KILLED":
			sta = "KILLED";
			break;
		case "TASK_LOST":
			sta = "KILLED";
			break;
		}

		return sta;
	}

	/**获得容器运行时的names
	 * 
	 * 去mesos slave上获得docker Container的信息
	 * @param dockerName 
	 * @return
	 */
	private String handleName(String dockerName,String host,String taskId) {
		String url = ParamUtil
				.fillStringByArgs(
						EventConstants.MESOS_SLAVE_SERVER,
						new String[] {
								host,
								ConfUtil.getProperty(EventConstants.MESOS_SLAVE_PORT) });
		String mesosRes = HttpUtil.sendMessageToEndPoint(url, null);
		StateResp resp = GsonUtil.gsonToObject(mesosRes, StateResp.class);
		
		String container = "";
		for(FrameWork framework :resp.getFrameworks()){
			for(Executor executor:framework.getExecutors()){
				if(executor.getId().equals(taskId)){
					container = executor.getContainer();
					break;
				}
			}
		}
		
		String dockerNameEnd = "mesos-"+dockerName+"."+container;
		LOG.debug("-----dockerNameEnd--{}--------------",dockerNameEnd);
		return dockerNameEnd;
	}

	/**太平洋标准时间 比东八区 滞后 8个小时
	 * @param timestamp
	 * @return
	 */
	private String convetTime(String timestamp) {
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Date dt = sdf.parse(timestamp);
			return sdf2.format(new Date(dt.getTime()+8*60*60*1000));
		} catch (ParseException e) {
			LOG.error("------"+e.getMessage(),e);
		}
		return sdf2.format(new Date());
	}

	private void printParams(HttpServletRequest req) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("--------Request Type:" + req.getMethod());
			String url_ = req.getServerName() + ":" + req.getServerPort()
					+ req.getRequestURI();
			String queryString = req.getQueryString();
			LOG.debug("--------URL:"
					+ (queryString == null ? url_ : url_ + queryString));
			LOG.debug("--------sessionid:" + req.getSession().getId());
			Cookie[] cookies = req.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					LOG.debug("--------Cookies   Name:" + cookie.getName()
							+ " Value:" + cookie.getValue() + " Domain:"
							+ cookie.getDomain());
				}
			}

			Enumeration<String> paraNames = req.getParameterNames();
			while (paraNames.hasMoreElements()) {
				String paraName = paraNames.nextElement();
				if (req.getParameter(paraName) != null)
					LOG.debug("--------Params    " + paraName + ":"
							+ req.getParameter(paraName));
			}

			Enumeration<String> attrNames = req.getAttributeNames();
			int count = 0;
			while (attrNames.hasMoreElements()) {
				String attrName = attrNames.nextElement();
				// if(req.getAttribute(attrName)!=null)
				// LOG.debug("--------Attrs    " + attrName + ":" +
				// req.getAttribute(attrName));
				count++;
			}
			LOG.debug("--------Attrs  size  " + count);
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						req.getInputStream()));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				LOG.debug("--------input stream  " + sb);
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			}

		}

	}

}
