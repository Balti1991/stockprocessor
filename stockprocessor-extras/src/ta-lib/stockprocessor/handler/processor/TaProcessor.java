/**
 * 
 */
package stockprocessor.handler.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.Candle;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultListParameterInformation;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.DefaultRangeParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.meta.CoreMetaData2;
import com.tictactec.ta.lib.meta.annotation.FuncInfo;
import com.tictactec.ta.lib.meta.annotation.InputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.IntegerList;
import com.tictactec.ta.lib.meta.annotation.IntegerRange;
import com.tictactec.ta.lib.meta.annotation.OptInputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.OutputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.RealList;
import com.tictactec.ta.lib.meta.annotation.RealRange;

/**
 * @author anti
 */
public class TaProcessor extends AbstractDataProcessor<ShareData<?>, ShareData<?>>
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(TaProcessor.class);

	private final CoreMetaData2 coreMetaData;

	private List<Object> optInputParameters;

	private int lookback;

	private int[] dataCount;

	private ShareData<?>[][] lookbackQueue = null;

	public TaProcessor(CoreMetaData2 coreMetaData)
	{
		this.coreMetaData = coreMetaData;

		// set lookback
		updateQueue();
	}

	private void updateQueue()
	{
		// set lookback
		try
		{
			lookback = coreMetaData.getLookback();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			// throw new RuntimeException(e);
			lookback = 0;
		}

		FuncInfo funcInfo = coreMetaData.getFuncInfo();

		int nbInput = funcInfo.nbInput();

		if (lookbackQueue == null || lookbackQueue.length != nbInput + 1)
		{
			dataCount = new int[nbInput];
			// create main if not exists or different
			lookbackQueue = new ShareData[nbInput + 1][];
		}

		for (int i = 0; i < nbInput; i++)
		{
			ShareData<?>[] lookbackInput = lookbackQueue[i];
			// create it if not exists or different
			if (lookbackInput == null || lookbackInput.length != lookback + 1)
			{
				// clear counter
				dataCount[i] = 0;

				lookbackQueue[i] = new ShareData[lookback + 1];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return coreMetaData.getFuncInfo().hint();
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		return coreMetaData.getFuncInfo().name();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, hu.bogar.anti.stock.data.StockData)
	 */
	@Override
	public void newDataArrivedNotification(String input, ShareData<?> stockData)
	{
		FuncInfo funcInfo = getCoreMetaData().getFuncInfo();

		for (int i = 0; i < funcInfo.nbInput(); i++)
		{
			// search for named input
			if (StringUtils.equals(input, getCoreMetaData().getInputParameterInfo(i).paramName()))
			{
				List<Object> result = callFunc(i, stockData);

				// if got result
				if (result != null)
					// split & publish
					for (int j = 0; j < funcInfo.nbOutput(); j++)
					{
						OutputParameterInfo opinfo = getCoreMetaData().getOutputParameterInfo(j);

						// TODO: calculate sum volume
						ShareData<?> data;
						switch (opinfo.type())
						{
						case TA_Output_Integer:
						{
							int[] value = (int[]) result.get(j);
							data = new ShareData<Integer>(opinfo.paramName(), value[0], stockData.getVolume(), stockData.getTimeStamp());
							break;
						}
						case TA_Output_Real:
						{
							double[] value = (double[]) result.get(j);
							data = new ShareData<Double>(opinfo.paramName(), value[0], stockData.getVolume(), stockData.getTimeStamp());
							break;
						}

						default:
							throw new IllegalArgumentException();
							// TODO: message
						}

						publishNewData(opinfo.paramName(), data);
					}
			}
		}
	}

	public List<Object> callFunc(int input, ShareData<?> stockData)
	{
		FuncInfo funcInfo = getCoreMetaData().getFuncInfo();

		// set opt input
		setOptionalInputParameters(funcInfo);

		updateQueue();

		int lookbackQueueLength = getLookbackQueue(input).length;

		// roll the queue
		if (lookbackQueueLength > 1)
			for (int j = 0; j < lookbackQueueLength - 1; j++)
			{
				getLookbackQueue(input)[j] = getLookbackQueue(input)[j + 1];
			}

		// add new value
		getLookbackQueue(input)[lookbackQueueLength - 1] = stockData;

		// check lookback queue size
		if (dataCount[input] <= lookback)
		{
			// count data and dont process it
			dataCount[input]++;

			return null;
		}

		// check all queues
		for (int i = 0; i < dataCount.length; i++)
		{
			// if any queues size is less...
			if (dataCount[i] <= lookback)
				return null;
		}

		// set input
		setupInputParameters(funcInfo);

		// set output
		List<Object> outputParameters = setOutputParameters(funcInfo);

		// calculate
		MInteger outNbElement = new MInteger();
		MInteger outBegIdx = new MInteger();
		try
		{
			coreMetaData.callFunc(0, lookbackQueueLength - 1, outBegIdx, outNbElement);

			// log.debug("Got result [" + outputParameters + "] for data [" +
			// stockData + "]");
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}

		// // store
		// storeData(stockData, outputParameters);

		return outputParameters;
	}

	/***************************************************************************
	 * getters
	 **************************************************************************/

	/**
	 * @return the coreMetaData
	 */
	protected CoreMetaData2 getCoreMetaData()
	{
		return coreMetaData;
	}

	public int getLookback()
	{
		return lookback;
	}

	/**
	 * @return the lookbackQueue
	 */
	protected ShareData<?>[] getLookbackQueue(int i)
	{
		return lookbackQueue[i];
	}

	/***************************************************************************
	 * helpers
	 **************************************************************************/

	/**
	 * @param funcInfo
	 * @param outputParameters
	 */
	private List<Object> setOutputParameters(FuncInfo funcInfo)
	{
		List<Object> outputParameters = new ArrayList<Object>();

		for (int i = 0; i < funcInfo.nbOutput(); i++)
		{
			Object array;

			OutputParameterInfo opinfo = coreMetaData.getOutputParameterInfo(i);
			switch (opinfo.type())
			{
			case TA_Output_Integer:
				array = new int[]
					{0};
				coreMetaData.setOutputParamInteger(i, array);
				break;
			case TA_Output_Real:
				array = new double[]
					{0.0};
				coreMetaData.setOutputParamReal(i, array);
				break;

			default:
				throw new IllegalArgumentException(); // TODO: message
			}

			outputParameters.add(array);
		}

		return outputParameters;
	}

	/**
	 * @param funcInfo
	 */
	private void setOptionalInputParameters(FuncInfo funcInfo)
	{
		// if no changes in default values, create the list
		if (optInputParameters == null)
			createDefaultValueList();

		// number of optional parameters
		int nbOptInput = funcInfo.nbOptInput();

		for (int i = 0; i < nbOptInput; i++)
		{
			OptInputParameterInfo info = getCoreMetaData().getOptInputParameterInfo(i);

			switch (info.type())
			{
			case TA_OptInput_IntegerList:
				coreMetaData.setOptInputParamInteger(i, (String) optInputParameters.get(i));
				break;
			case TA_OptInput_IntegerRange:
				coreMetaData.setOptInputParamInteger(i, (Integer) optInputParameters.get(i));
				break;
			case TA_OptInput_RealList:
				coreMetaData.setOptInputParamReal(i, (String) optInputParameters.get(i));
				break;
			case TA_OptInput_RealRange:
				coreMetaData.setOptInputParamReal(i, (Double) optInputParameters.get(i));
				break;

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}
	}

	/**
	 * @param funcInfo
	 */
	protected void setupInputParameters(FuncInfo funcInfo)
	{
		for (int i = 0; i < funcInfo.nbInput(); i++)
		{
			InputParameterInfo info = getCoreMetaData().getInputParameterInfo(i);
			switch (info.type())
			{
			case TA_Input_Price:
				int length = getLookbackQueue(i).length;

				double[] open = new double[length];
				double[] high = new double[length];
				double[] low = new double[length];
				double[] close = new double[length];
				double[] volume = new double[length];
				double[] interest = new double[length];

				for (int j = 0; j < length; j++)
				{
					Candle candle = (Candle) getLookbackQueue(i)[j].getValue();

					open[j] = candle.getOpen();
					high[j] = candle.getMax();
					low[j] = candle.getMin();
					close[j] = candle.getClose();

					volume[j] = getLookbackQueue(i)[j].getVolume();
					interest[j] = 0; // FIXME
				}

				getCoreMetaData().setInputParamPrice(i, open, high, low, close, volume, interest);
				break;
			case TA_Input_Real:
			{
				double[] ds = new double[getLookbackQueue(i).length];

				for (int j = 0; j < getLookbackQueue(i).length; j++)
				{
					Object value = getLookbackQueue(i)[j].getValue();

					if (value instanceof Integer)
					{
						ds[j] = (Integer) value;
					}
					else if (value instanceof Candle)
					{
						ds[j] = ((Candle) value).getClose();
					}
					else
					{
						System.err.println("Unknown value type: [" + value + "]");
					}
				}

				getCoreMetaData().setInputParamReal(i, ds);
				break;
			}
			case TA_Input_Integer:
			{
				int[] ds = new int[getLookbackQueue(i).length];

				for (int j = 0; j < getLookbackQueue(i).length; j++)
				{
					ds[j] = (Integer) getLookbackQueue(i)[j].getValue();
				}

				getCoreMetaData().setInputParamInteger(i, ds);
				break;
			}

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.AbstractDataProcessor#createInputParameters()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		for (int i = 0; i < coreMetaData.getFuncInfo().nbInput(); i++)
		{
			InputParameterInfo info = getCoreMetaData().getInputParameterInfo(i);
			switch (info.type())
			{
			case TA_Input_Price:
				list.add(new DefaultParameterInformation(info.paramName(), ParameterType.STOCK_DATA_CANDLE));
				break;
			case TA_Input_Real:
			case TA_Input_Integer:
				list.add(new DefaultParameterInformation(info.paramName(), ParameterType.STOCK_DATA_NUMBER));
				break;

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.AbstractDataProcessor#createOptionalParameters()
	 */
	@Override
	protected List<ParameterInformation> createOptionalParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		int nbOptInput = coreMetaData.getFuncInfo().nbOptInput();

		for (int i = 0; i < nbOptInput; i++)
		{
			OptInputParameterInfo info = getCoreMetaData().getOptInputParameterInfo(i);

			// create information
			switch (info.type())
			{
			case TA_OptInput_IntegerList:
			{
				IntegerList optInputIntegerList = getCoreMetaData().getOptInputIntegerList(i);

				int[] value = optInputIntegerList.value();
				Integer[] valueList = new Integer[value.length];
				for (int j = 0; j < value.length; j++)
				{
					valueList[i] = value[i];
				}

				int defaultValue = optInputIntegerList.defaultValue();
				list.add(new DefaultListParameterInformation<Integer>(info.displayName(), defaultValue, valueList, optInputIntegerList.string()));

				break;
			}
			case TA_OptInput_RealList:
			{
				RealList optInputRealList = getCoreMetaData().getOptInputRealList(i);

				double[] value = optInputRealList.value();
				Double[] valueList = new Double[value.length];
				for (int j = 0; j < value.length; j++)
				{
					valueList[i] = value[i];
				}

				double defaultValue = optInputRealList.defaultValue();
				list.add(new DefaultListParameterInformation<Double>(info.displayName(), defaultValue, valueList, optInputRealList.string()));

				break;
			}
			case TA_OptInput_IntegerRange:
			{
				IntegerRange optInputIntegerRange = getCoreMetaData().getOptInputIntegerRange(i);

				int defaultValue = optInputIntegerRange.defaultValue();
				list.add(new DefaultRangeParameterInformation<Integer>(info.displayName(), defaultValue, optInputIntegerRange.suggested_start(),
						optInputIntegerRange.suggested_end(), optInputIntegerRange.suggested_increment(), 1));

				break;
			}
			case TA_OptInput_RealRange:
			{
				RealRange optInputRealRange = getCoreMetaData().getOptInputRealRange(i);

				double defaultValue = optInputRealRange.defaultValue();
				list.add(new DefaultRangeParameterInformation<Double>(info.displayName(), defaultValue, optInputRealRange.suggested_start(),
						optInputRealRange.suggested_end(), optInputRealRange.suggested_increment(), 1d));
				log.info("precision " + optInputRealRange.precision());

				break;
			}

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.AbstractDataProcessor#createOutputParameters()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		for (int i = 0; i < coreMetaData.getFuncInfo().nbOutput(); i++)
		{
			OutputParameterInfo info = getCoreMetaData().getOutputParameterInfo(i);
			switch (info.type())
			{
			case TA_Output_Integer:
			case TA_Output_Real:
				list.add(new DefaultParameterInformation(info.paramName(), ParameterType.STOCK_DATA_NUMBER));
				break;

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.receiver.DataReceiver#setOptionalParameterInformations
	 * (java.util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// reset default values
		createDefaultValueList();

		// number of optional parameters
		int nbOptInput = getCoreMetaData().getFuncInfo().nbOptInput();

		for (int i = 0; i < nbOptInput; i++)
		{
			OptInputParameterInfo info = getCoreMetaData().getOptInputParameterInfo(i);
			String displayName = info.displayName();

			if (optionalParameters.containsKey(displayName))
			{
				// override the default value
				Object value = optionalParameters.get(displayName);

				log.debug("Useing new value: [" + value + "] to parameter: [" + info + "]");

				switch (info.type())
				{
				case TA_OptInput_IntegerList:
					optInputParameters.set(i, new String("" + value));
					break;
				case TA_OptInput_IntegerRange:
					optInputParameters.set(i, ((Double) value).intValue());
					break;
				case TA_OptInput_RealList:
					optInputParameters.set(i, new String("" + value));
					break;
				case TA_OptInput_RealRange:
					optInputParameters.set(i, value);
					break;

				default:
					throw new IllegalArgumentException(); // TODO: message
				}
			}
		}
	}

	private void createDefaultValueList()
	{
		// create default values list
		optInputParameters = new ArrayList<Object>();

		int nbOptInput = coreMetaData.getFuncInfo().nbOptInput();

		for (int i = 0; i < nbOptInput; i++)
		{
			OptInputParameterInfo info = getCoreMetaData().getOptInputParameterInfo(i);

			// create information
			switch (info.type())
			{
			case TA_OptInput_IntegerList:
			{
				IntegerList optInputIntegerList = getCoreMetaData().getOptInputIntegerList(i);

				int defaultValue = optInputIntegerList.defaultValue();
				optInputParameters.add(defaultValue);
				break;
			}
			case TA_OptInput_RealList:
			{
				RealList optInputRealList = getCoreMetaData().getOptInputRealList(i);

				double defaultValue = optInputRealList.defaultValue();
				optInputParameters.add(defaultValue);
				break;
			}
			case TA_OptInput_IntegerRange:
			{
				IntegerRange optInputIntegerRange = getCoreMetaData().getOptInputIntegerRange(i);

				int defaultValue = optInputIntegerRange.defaultValue();
				optInputParameters.add(defaultValue);
				break;
			}
			case TA_OptInput_RealRange:
			{
				RealRange optInputRealRange = getCoreMetaData().getOptInputRealRange(i);

				double defaultValue = optInputRealRange.defaultValue();
				optInputParameters.add(defaultValue);
				break;
			}

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}
	}
}
