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

import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.handler.processor.ProcessorManager;
import stockprocessor.handler.receiver.DataReceiver;

import com.tictactec.ta.lib.CoreAnnotated;
import com.tictactec.ta.lib.RetCode;
import com.tictactec.ta.lib.meta.CoreMetaData2;
import com.tictactec.ta.lib.meta.annotation.FuncInfo;
import com.tictactec.ta.lib.meta.annotation.InputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.InputParameterType;
import com.tictactec.ta.lib.meta.annotation.OptInputParameterInfo;

/**
 * @author anti
 */
public class TAProcessorManager extends ProcessorManager
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
	public List<String> getAvailableProcessors()
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

	/**
	 * @return
	 */
	@Deprecated
	public List<InputParameterInfo> getInputParameterInfo(String name)
	{
		CoreMetaData2 instance = getCoreMetaData(name);

		FuncInfo funcInfo = instance.getFuncInfo();

		List<InputParameterInfo> result = new ArrayList<InputParameterInfo>();
		for (int i = 0; i < funcInfo.nbInput(); i++)
		{
			result.add(instance.getInputParameterInfo(i));

			log.debug("nbOptInput: " + i);
			log.debug("getOptInputParameterInfo: " + instance.getInputParameterInfo(i));
		}

		return result;
	}

	/**
	 * @return
	 */
	@Deprecated
	public List<OptInputParameterInfo> getOptInputParameterInfo(String name)
	{
		CoreMetaData2 instance = getCoreMetaData(name);

		FuncInfo funcInfo = instance.getFuncInfo();

		List<OptInputParameterInfo> result = new ArrayList<OptInputParameterInfo>();
		for (int i = 0; i < funcInfo.nbOptInput(); i++)
		{
			result.add(instance.getOptInputParameterInfo(i));

			log.debug("nbOptInput: " + i);
			log.debug("getOptInputParameterInfo: " + instance.getOptInputParameterInfo(i));
			log.debug("getOptInputIntegerList: " + instance.getOptInputIntegerList(i));
			log.debug("getOptInputIntegerRange: " + instance.getOptInputIntegerRange(i));
			log.debug("getOptInputRealList: " + instance.getOptInputRealList(i));
			log.debug("getOptInputRealRange: " + instance.getOptInputRealRange(i));
		}

		return result;
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

		// get parameters
		List<Object> optInputParameters = getOptionalInputParameters(coreMetaData);

		// String instrument = (String) instrumentComboBox.getSelectedItem();

		// create element
		TaProcessor element = new TaProcessor(coreMetaData, optInputParameters);

		// // get source
		// StockDataSource stockDataSource = sourceManager.getInstance((String)
		// sourceComboBox.getSelectedItem());

		if (coreMetaData.getInputParameterInfo(0).type() == InputParameterType.TA_Input_Price)
		{
			// candle
			CandleCollectorStockDataProcessor candleDataCollector = new CandleCollectorStockDataProcessor();
			candleDataCollector.registerDataReceiver("", (DataReceiver) element); // FIXME
			return candleDataCollector;
		}

		return element;
	}

	private List<Object> getOptionalInputParameters(CoreMetaData2 coreMetaData)
	{
		FuncInfo funcInfo = coreMetaData.getFuncInfo();

		List<Object> optInputParameters = new ArrayList<Object>();

		for (int i = 0; i < funcInfo.nbOptInput(); i++)
		{
			OptInputParameterInfo info = coreMetaData.getOptInputParameterInfo(i);
			// Component component = parametersPanel.getComponent(i * 2 + 1);
			//
			// switch (info.type())
			// {
			// case TA_OptInput_IntegerList:
			// optInputParameters.add(((JComboBox)
			// component).getSelectedItem());
			// break;
			// case TA_OptInput_IntegerRange:
			// optInputParameters.add(((JSpinner) component).getValue());
			// break;
			// case TA_OptInput_RealList:
			// optInputParameters.add(((JComboBox)
			// component).getSelectedItem());
			// break;
			// case TA_OptInput_RealRange:
			// optInputParameters.add(((JSpinner) component).getValue());
			// break;
			//
			// default:
			// throw new IllegalArgumentException(); // TODO: message
			// }
		}

		return optInputParameters;
	}
}
