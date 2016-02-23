package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ai.paas.ipaas.me.constant.EventConstants;
import com.ai.paas.ipaas.me.model.StateResp;
import com.ai.paas.ipaas.me.util.ConfUtil;
import com.ai.paas.ipaas.me.util.GsonUtil;
import com.ai.paas.ipaas.me.util.HttpUtil;
import com.ai.paas.ipaas.me.util.ParamUtil;

public class MesosRes {
	public static void main(String[] args) {
		
//		testResp();
		
		System.out.println(convetTime("2016-02-22T09:34:14.646Z"));
	}
	
	
	
	private static void testResp() {
		String url = ParamUtil
				.fillStringByArgs(
						EventConstants.MESOS_SLAVE_SERVER,
						new String[] {
								"10.1.235.199",
								ConfUtil.getProperty(EventConstants.MESOS_SLAVE_PORT) });
		String mesosRes = HttpUtil.sendMessageToEndPoint(url, null);
		StateResp resp = GsonUtil.gsonToObject(mesosRes, StateResp.class);
		String container = resp.getFrameworks().get(0).getExecutors().get(0)
				.getContainer();
		String dockerNameEnd = "mesos-"
				+ "20160107-105655-3320512778-5050-26940-S2" + "-" + container;
		System.out.println("-----dockerNameEnd--{}--------------"
				+ dockerNameEnd);		
	}



	private static String convetTime(String timestamp) {
		SimpleDateFormat sdf2 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Date dt = sdf.parse(timestamp);
			return sdf2.format(new Date(dt.getTime()+8*60*60*1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf2.format(new Date());
	}

}
