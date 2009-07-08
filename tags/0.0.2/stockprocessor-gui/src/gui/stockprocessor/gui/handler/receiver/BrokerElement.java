/**
 * 
 */
package stockprocessor.gui.handler.receiver;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;
import java.util.Map;

import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;

import stockprocessor.broker.StockBroker;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.StockAction;
import stockprocessor.handler.receiver.DataReceiver;

/**
 * @author anti
 */
public class BrokerElement<SD extends ShareData<?>> implements DataReceiver<SD>
{
	private Boolean longPosition = null;

	private final StockBroker<SD> stockBroker;

	private XYPlot plot = null;

	private IntervalMarker marker = null;

	/**
	 * @param instrument
	 * @param stockDataReceiver
	 */
	public BrokerElement(final StockBroker stockBroker)
	{
		this.stockBroker = stockBroker;
	}

	@Override
	public String getName()
	{
		return stockBroker.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, stockprocessor.data.StockData)
	 */
	@Override
	public void newDataArrivedNotification(String input, SD stockData)
	{
		// if not on chart skip data
		if (plot == null)
			return;

		StockAction stockAction = stockBroker.newDataArrivedNotification(input, stockData);

		switch (stockAction)
		{
		case BUY:
			if (longPosition == null)
			{
				// create position
				longPosition = true;
				createMarker(stockData, Color.GREEN);
			}
			else if (longPosition)
			{
				// move endpoint
				marker.setEndValue(stockData.getTimeStamp().getTime());
			}
			else
			{
				// close position
				longPosition = null;
			}

			break;
		case SELL:
			if (longPosition == null)
			{
				// create position
				longPosition = false;
				createMarker(stockData, Color.RED);
			}
			else if (!longPosition)
			{
				// move endpoint
				marker.setEndValue(stockData.getTimeStamp().getTime());
			}
			else
			{
				// close position
				longPosition = null;
			}

			break;

		default:
			// if in position
			if (longPosition != null)
			{
				// move endpoint
				marker.setEndValue(stockData.getTimeStamp().getTime());
			}
			break;
		}

		// return stockData;
	}

	private void createMarker(SD stockData, Paint paint)
	{
		// create new marker
		marker = new IntervalMarker(stockData.getTimeStamp().getTime(), stockData.getTimeStamp().getTime(), paint);
		getPlot().addDomainMarker(marker);
	}

	/**
	 * @param plot the plot to set
	 */
	public void setPlot(XYPlot plot)
	{
		this.plot = plot;

		// if removed from chart cancel last marker
		if (plot == null)
			marker = null;
	}

	/**
	 * @return the plot
	 */
	public XYPlot getPlot()
	{
		return plot;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataReceiver#getInputParameterInformations()
	 */
	@Override
	public List<ParameterInformation> getInputParameters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataReceiver#getOptionalParameterInformations
	 * ()
	 */
	@Override
	public List<ParameterInformation> getOptionalParameters()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.data.handler.DataReceiver#setOptionalParameterInformations
	 * (java.util.Map)
	 */
	@Override
	public void setOptionalParameters(Map<String, Object> optionalParameters)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.data.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
