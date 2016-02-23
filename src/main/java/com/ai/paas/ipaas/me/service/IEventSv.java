package com.ai.paas.ipaas.me.service;

import javax.servlet.http.HttpServletRequest;

public interface IEventSv {
	boolean sendMsg(HttpServletRequest request);
}
