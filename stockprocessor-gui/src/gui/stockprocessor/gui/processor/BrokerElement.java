/**
 * 
 */
package stockprocessor.gui.processor;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;

import stockprocessor.broker.StockBroker;
import stockprocessor.data.StockData;
import stockprocessor.processor.StockAction;
import stockprocessor.processor.StockDataReceiver;

/**
 * @author anti
 */
public class BrokerElement<SD extends StockData<?>> implements StockDataReceiver<SD>
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
	public StockData<?> newDataArrivedNotification(String instrument, SD stockData)
	{
		// if not on chart skip data
		if (plot == null)
			return null;

		StockAction stockAction = stockBroker.newDataArrivedNotification(instrument, stockData);

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
				marker.setEndValue(stockData.getTime().getTime());
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
				marker.setEndValue(stockData.getTime().getTime());
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
				marker.setEndValue(stockData.getTime().getTime());
			}
			break;
		}

		return stockData;
	}

	private void createMarker(SD stockData, Paint paint)
	{
		// create new marker
		marker = new IntervalMarker(stockData.getTime().getTime(), stockData.getTime().getTime(), paint);
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
}
