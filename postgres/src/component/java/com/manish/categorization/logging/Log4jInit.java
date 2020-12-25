
package com.manish.categorization.logging;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.manish.categorization.sdp.config.CaasConfigBean;

@Component
public class Log4jInit {

	@Autowired
	CaasConfigBean caasConfigBean;

	private static String LOG4J_XML_FILE_NAME = "/log4j2.xml";

	@PostConstruct
	public void init() {

		System.out.println("-------------Printing Values in Log4Init--------------");
		System.out.println("logfile " + caasConfigBean.getCaas().getLogFile());
		System.out.println("logFileNamePrefix " + System.getProperty("logPrefix"));
//		System.out.println("logFileNamePrefix "+environment.getProperty("logfile.name.prefix"));
		System.out.println("instance " + caasConfigBean.getCaas().getInstance());
		System.out.println("maxSize " + caasConfigBean.getCaas().getMaxSize());
		System.out.println("maxBackup " + caasConfigBean.getCaas().getMaxBackup());

		System.setProperty("ServiceLogMaxFileSize", caasConfigBean.getCaas().getMaxSize());
		System.setProperty("ServiceLogMaxBackupIndex",
				caasConfigBean.getCaas().getMaxBackup());
		System.setProperty("instance", caasConfigBean.getCaas().getInstance());
		System.setProperty("ServiceLogFileName", getLogFileName(
				caasConfigBean.getCaas().getLogFile(), System.getProperty("logPrefix")));
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		System.out.println("ServiceLogMaxFileSize : " + System.getProperty("ServiceLogMaxFileSize"));
		System.out.println("ServiceLogMaxBackupIndex : " + System.getProperty("ServiceLogMaxBackupIndex"));
		System.out.println("ServiceLogFileName : " + System.getProperty("ServiceLogFileName"));
		System.out.println("Instance : "
				+ System.getProperty("instance", caasConfigBean.getCaas().getInstance()));
		LoggerContext logCtx = (LoggerContext) LogManager.getContext(false);
		URI uri = null;
		try {
			uri = Log4jInit.class.getResource(LOG4J_XML_FILE_NAME).toURI();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logCtx.setConfigLocation(uri);
		logCtx.updateLoggers();
		System.out.println("Log File " + System.getProperty("ServiceLogFileName"));

	}

	private static String getLogFileName(String location, String prefix) {
		StringBuffer sb = new StringBuffer();
		String logSuffice = System.getProperty("logPrefix");
		if (logSuffice != null) {
			sb.append(location).append("_").append(logSuffice).append(".log");
		} else {
			sb.append(location).append("_").append("caas_devcaas").append(".log");
		}
		System.out.println("File Location " + sb.toString());
		return sb.toString();
	}

}