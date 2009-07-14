/**
 * 
 */
package stockprocessor.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import stockprocessor.manager.DefaultSourceManager;
import stockprocessor.manager.SourceHandler;
import stockprocessor.manager.SourceManager;

/**
 * @author anti
 */
public class SourceBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(SourceBeanFactoryPostProcessor.class);

	/*
	 * (non-Javadoc)
	 * @seeorg.springframework.beans.factory.config.BeanFactoryPostProcessor#
	 * postProcessBeanFactory
	 * (org.springframework.beans.factory.config.ConfigurableListableBeanFactory
	 * )
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException
	{
		log.info("Configuring DefaultSourceManager");
		int manager = 0;
		int handler = 0;

		Map<String, SourceManager> beansOfTypeSourceManager = configurableListableBeanFactory.getBeansOfType(SourceManager.class);
		for (Entry<String, SourceManager> entry : beansOfTypeSourceManager.entrySet())
		{
			log.info("loaded source manager: " + entry.getKey());
			DefaultSourceManager.INSTANCE.registerSourceManager(entry.getValue());
			manager++;
		}

		Map<String, SourceHandler> beansOfTypeSourceHandler = configurableListableBeanFactory.getBeansOfType(SourceHandler.class);
		for (Entry<String, SourceHandler> entry : beansOfTypeSourceHandler.entrySet())
		{
			log.info("loaded source handler: " + entry.getKey());
			DefaultSourceManager.INSTANCE.registerInstanceHandler(entry.getValue());
			handler++;
		}

		log.info("Configured DefaultSourceManager to use " + manager + " manager and " + handler + " handler");
	}
}
