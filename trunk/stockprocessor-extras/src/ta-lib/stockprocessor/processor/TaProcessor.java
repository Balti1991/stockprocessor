/**
 * 
 */
package stockprocessor.processor;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import stockprocessor.data.Candle;
import stockprocessor.data.StockData;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.processor.StockDataProcessor;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.meta.CoreMetaData2;
import com.tictactec.ta.lib.meta.annotation.FuncInfo;
import com.tictactec.ta.lib.meta.annotation.InputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.OptInputParameterInfo;
import com.tictactec.ta.lib.meta.annotation.OutputParameterInfo;

/**
 * @author anti
 */
public class TaProcessor implements StockDataProcessor<StockData<?>>
{
	private final CoreMetaData2 coreMetaData;

	private final List<Object> optInputParameters;

	private int lookback;

	private int dataCount = 0;

	private StockData<?>[] lookbackQueue = null;

	public TaProcessor(CoreMetaData2 coreMetaData, List<Object> optInputParameters)
	{
		this.coreMetaData = coreMetaData;
		this.optInputParameters = optInputParameters;

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

		// create it if not exists or different
		if (lookbackQueue == null || lookbackQueue.length != lookback + 1)
			lookbackQueue = new StockData[lookback + 1];
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getInputParameterInformations()
	{
		// TODO Auto-generated method stub
		return null;
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
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getOptionalInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOptionalInputParameterInformations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getOutputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getOutputParameterInformations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, hu.bogar.anti.stock.data.StockData)
	 */
	@Override
	public StockData<?> newDataArrivedNotification(String instrument, StockData<?> stockData)
	{
		callFunc(stockData);

		return null;
	}

	public List<Object> callFunc(StockData<?> stockData)
	{
		FuncInfo funcInfo = coreMetaData.getFuncInfo();

		// set opt input
		setOptionalInputParameters(coreMetaData.getFuncInfo());

		updateQueue();
		int lookbackQueueLength = getLookbackQueue().length;

		// roll the queue
		if (lookbackQueueLength > 1)
			for (int i = 0; i < lookbackQueueLength - 1; i++)
			{
				getLookbackQueue()[i] = getLookbackQueue()[i + 1];
			}

		// add new value
		getLookbackQueue()[lookbackQueueLength - 1] = stockData;

		// check lookback queue size
		if (dataCount <= lookback)
		{
			// count data and dont process it
			dataCount++;

			return null;
		}

		// set input
		setInputParameters(funcInfo);

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
	protected StockData<?>[] getLookbackQueue()
	{
		return lookbackQueue;
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
		if (optInputParameters == null || optInputParameters.size() != funcInfo.nbOptInput())
			throw new IllegalArgumentException(); // TODO: message

		for (int i = 0; i < funcInfo.nbOptInput(); i++)
		{
			OptInputParameterInfo info = coreMetaData.getOptInputParameterInfo(i);

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

	protected void setInputParameters(FuncInfo funcInfo)
	{
		for (int i = 0; i < funcInfo.nbInput(); i++)
		{
			InputParameterInfo info = getCoreMetaData().getInputParameterInfo(i);
			switch (info.type())
			{
			case TA_Input_Price:
				int length = getLookbackQueue().length;

				double[] open = new double[length];
				double[] high = new double[length];
				double[] low = new double[length];
				double[] close = new double[length];
				double[] volume = new double[length];
				double[] interest = new double[length];

				for (int j = 0; j < length; j++)
				{
					Candle candle = (Candle) getLookbackQueue()[j].getValue();

					open[j] = candle.getOpen();
					high[j] = candle.getMax();
					low[j] = candle.getMin();
					close[j] = candle.getClose();

					volume[j] = getLookbackQueue()[j].getVolume();
					interest[j] = 0; // FIXME
				}

				getCoreMetaData().setInputParamPrice(i, open, high, low, close, volume, interest);
				break;
			case TA_Input_Real:
			{
				double[] ds = new double[getLookbackQueue().length];

				for (int j = 0; j < getLookbackQueue().length; j++)
				{
					Object value = getLookbackQueue()[j].getValue();

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
				int[] ds = new int[getLookbackQueue().length];

				for (int j = 0; j < getLookbackQueue().length; j++)
				{
					ds[j] = (Integer) getLookbackQueue()[j].getValue();
				}

				getCoreMetaData().setInputParamInteger(i, ds);
				break;
			}

			default:
				throw new IllegalArgumentException(); // TODO: message
			}
		}
	}
}
