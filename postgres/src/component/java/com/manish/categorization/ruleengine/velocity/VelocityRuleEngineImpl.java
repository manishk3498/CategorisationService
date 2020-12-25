package com.manish.categorization.ruleengine.velocity;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityRuleEngineImpl {

	private static final Logger logger = LogManager.getLogger(VelocityRuleEngineImpl.class);
	private static VelocityRuleEngineImpl instance = new VelocityRuleEngineImpl();

	String classificationTemplate = "";
	String spendTemplate = "";
	String nonSpendTemplate = "";

	private VelocityRuleEngineImpl() {
		try {
			java.util.Properties props = new java.util.Properties();
			props.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
			props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			props.setProperty("classpath.resource.loader.class",
					ClasspathResourceLoader.class.getName());
			props.setProperty("class.resource.loader.cache", "true");
			Velocity.init(props);
		} catch (Exception e) {
			if (logger.isInfoEnabled())
				logger.info("Exception while creating VelocityRulEngineImpl ", e);
		}
	}

	public static VelocityRuleEngineImpl getInstance() {
		return instance;
	}

	public String execute(String ruleId, HashMap params) {

		VelocityContext context = new VelocityContext(params);
		String templateName = null;

		if (ruleId.equals("isSpendOrNonSpend"))
			templateName = "SpendNonSpendClassification.vm";
		else if (ruleId.equals("Non-Spend"))
			templateName = "NonSpendRules.vm";
		else
			templateName = "SpendRules.vm";
		Template template = Velocity.getTemplate(templateName);
		StringWriter writer = new StringWriter();
		boolean result = false;
		try {
			template.merge(context, writer);
			result = true;
		} catch (ParseErrorException e) {
			if (logger.isInfoEnabled())
				logger.info(
						"Exception while Evaluating VelocityRuleTemplates for SimpleDescription Derivation",
						e);
		} catch (MethodInvocationException e) {
			if (logger.isInfoEnabled())
				logger.info(
						"Exception while Evaluating VelocityRuleTemplates for SimpleDescription Derivation",
						e);
		} catch (ResourceNotFoundException e) {
			if (logger.isInfoEnabled())
				logger.info(
						"Exception while Evaluating VelocityRuleTemplates for SimpleDescription Derivation",
						e);
		}
		String strResult = null;
		if (result)
			strResult = writer.getBuffer().toString().trim();
		return strResult;
	}
}
