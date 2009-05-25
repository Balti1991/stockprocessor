/**
 * 
 */
package stockprocessor.processor;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import stockprocessor.data.StockData;
import stockprocessor.processor.StockDataProcessor;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.meta.CoreMetaData2;

/**
 * @author anti
 */
public class DefaultTAProcessor implements StockDataProcessor
{
	private final CoreMetaData2 coreMetaData;

	private int lookback;

	private final Object[] lookbackQueue;

	public DefaultTAProcessor(CoreMetaData2 coreMetaData)
	{
		this.coreMetaData = coreMetaData;

		try
		{
			lookback = coreMetaData.getLookback();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lookbackQueue = new Object[lookback];
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.processor.StockDataProcessor#getName()
	 */
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.processor.StockDataProcessor#newDataArrivedNotification
	 * (hu.bogar.anti.stock.data.StockData)
	 */
	@Override
	public StockData<?> newDataArrivedNotification(String instrument, StockData stockData)
	{
		// roll the queue
		if (lookbackQueue.length > 1)
			for (int i = 0; i < lookbackQueue.length - 1; i++)
			{
				lookbackQueue[i] = lookbackQueue[i + 1];
			}

		lookbackQueue[lookbackQueue.length] = 0; // FIXME

		MInteger outNbElement = new MInteger();
		MInteger outBegIdx = new MInteger();

		try
		{
			coreMetaData.callFunc(0, lookbackQueue.length, outBegIdx, outNbElement);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;// FIXME
	}

	/**
	 * @return the coreMetaData
	 */
	public CoreMetaData2 getCoreMetaData()
	{
		return coreMetaData;
	}

	/**
	 * @return the lookback
	 */
	public int getLookback()
	{
		return lookback;
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
	public List getInputParameterInformations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @seehu.bogar.anti.stock.processor.StockDataProcessor#
	 * getOptionalInputParameterInformations()
	 */
	@Override
	public List getOptionalInputParameterInformations()
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
	public List getOutputParameterInformations()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
