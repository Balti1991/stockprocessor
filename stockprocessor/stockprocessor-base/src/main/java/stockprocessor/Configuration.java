/**
 * 
 */
package stockprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author anti
 */
public class Configuration
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(Configuration.class);

	public static final Configuration INSTANCE = new Configuration();

	private boolean initDone = false;

	private Configuration()
	{
	}

	public void initialize()
	{
		if (initDone)
			return;

		ApplicationContext ctx;

		// sources
		ctx = new ClassPathXmlApplicationContext(new String[]
			{"classpath*:config/source-*.xml"});

		// processors
		ctx = new ClassPathXmlApplicationContext(new String[]
			{"classpath*:config/processor-*.xml"});

		initDone = true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Configuration stockProcessor = new Configuration();

		try
		{
			stockProcessor.initialize();
		}
		catch (BeansException e)
		{
			log.error("initialize() - Failed test1: " + e.getLocalizedMessage(), e); //$NON-NLS-1$
			System.exit(1);
		}
	}

}
