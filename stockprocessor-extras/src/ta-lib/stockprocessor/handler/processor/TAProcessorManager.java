/**
 * 
 */
package stockprocessor.handler.processor;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.manager.AbstractManager;

import com.tictactec.ta.lib.CoreAnnotated;
import com.tictactec.ta.lib.RetCode;
import com.tictactec.ta.lib.meta.CoreMetaData2;
import com.tictactec.ta.lib.meta.annotation.FuncInfo;

/**
 * @author anti
 */
public class TAProcessorManager extends AbstractManager<DataProcessor<?, ?>>
{
	private static final Log log = LogFactory.getLog(TAProcessorManager.class);

	/**
	 * FROM: com.tictactec.ta.lib.meta.CoreMetaData
	 */
	private transient final Class<CoreAnnotated> coreClass = CoreAnnotated.class;

	/**
	 * FROM: com.tictactec.ta.lib.meta.CoreMetaData
	 */
	private transient final String LOOKBACK_SUFFIX = "Lookback";

	@Override
	public List<String> getAvailableInstances()
	{
		List<String> result = new ArrayList<String>();
		Method[] ms = coreClass.getDeclaredMethods();
		Map<String, Method> lookbackMap = getLookbackMethodMap();

		for (Method funcMethod : ms)
		{
			String fn = funcMethod.getName();
			if (funcMethod.getReturnType().equals(RetCode.class))
			// && !fn.startsWith(INT_PREFIX))
			{
				String lookbackName = fn + LOOKBACK_SUFFIX;
				Method lookbackMethod = lookbackMap.get(lookbackName);
				if (lookbackMethod != null)
				{
					FuncInfo info = getFuncInfo(funcMethod);
					String funcName = info.name();
					result.add(funcName);
				}
			}
		}

		return result;
	}

	/**
	 * FROM: com.tictactec.ta.lib.meta.CoreMetaData
	 * 
	 * @param method
	 * @return
	 * @throws IncompleteAnnotationException
	 */
	private FuncInfo getFuncInfo(Method method) throws IncompleteAnnotationException
	{
		FuncInfo annotation = method.getAnnotation(FuncInfo.class);
		if (annotation != null)
			return annotation;
		throw new IncompleteAnnotationException(FuncInfo.class, "Method " + method.getName());
	}

	/**
	 * FROM: com.tictactec.ta.lib.meta.CoreMetaData
	 * 
	 * @return
	 */
	private Map<String, Method> getLookbackMethodMap()
	{
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] ms = coreClass.getDeclaredMethods();
		for (Method m : ms)
		{
			if (m.getName().endsWith(LOOKBACK_SUFFIX))
			{
				map.put(m.getName(), m);
				// System.out.println("lookback="+m.getName());
			}
		}
		return map;
	}

	/**
	 * @param name
	 * @return
	 */
	public CoreMetaData2 getCoreMetaData(String name)
	{
		CoreMetaData2 instance;
		try
		{
			instance = CoreMetaData2.getInstance(name);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.ProcessorManager#getInstance(java.lang.
	 * String)
	 */
	@Override
	public DataProcessor<?, ?> getInstance(String stockDataProcessorName)
	{
		// add element
		CoreMetaData2 coreMetaData = getCoreMetaData(stockDataProcessorName);

		// create element
		TaProcessor element = new TaProcessor(coreMetaData);

		// if (coreMetaData.getInputParameterInfo(0).type() ==
		// InputParameterType.TA_Input_Price)
		// {
		// // candle
		// CandleConverter candleDataCollector = new CandleConverter();
		// candleDataCollector.registerDataReceiver("", (DataReceiver) element,
		// ""); // FIXME
		// return candleDataCollector;
		// }

		return element;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.manager.Manager#getName()
	 */
	@Override
	public String getName()
	{
		return "TA-lib Processors";
	}

}
