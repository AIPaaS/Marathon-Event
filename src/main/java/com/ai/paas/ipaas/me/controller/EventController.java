package com.ai.paas.ipaas.me.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.paas.ipaas.me.service.IEventSv;

@Controller
@RequestMapping(value = "/mevent")
public class EventController {
	private static final transient Logger LOG = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private IEventSv iEventSv;

	@ResponseBody
	@RequestMapping(value = "/change")
	public String change(HttpServletRequest request) {
		iEventSv.sendMsg(request);
		return null;
	}

	
}
