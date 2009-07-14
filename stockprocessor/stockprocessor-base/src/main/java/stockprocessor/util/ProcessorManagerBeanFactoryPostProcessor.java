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

import stockprocessor.manager.DefaultProcessorManager;
import stockprocessor.manager.ProcessorManager;

/**
 * @author anti
 */
public class ProcessorManagerBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(ProcessorManagerBeanFactoryPostProcessor.class);

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
		Map<String, ProcessorManager> beansOfType = configurableListableBeanFactory.getBeansOfType(ProcessorManager.class);
		for (Entry<String, ProcessorManager> entry : beansOfType.entrySet())
		{
			log.info("loaded source manager: " + entry.getKey());
			DefaultProcessorManager.INSTANCE.registerProcessorManager(entry.getValue());
		}
	}
}
