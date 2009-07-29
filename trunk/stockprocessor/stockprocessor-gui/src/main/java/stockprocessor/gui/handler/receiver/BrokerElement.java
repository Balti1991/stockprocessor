/**
 * 
 */
package stockprocessor.gui.handler.receiver;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;

import stockprocessor.broker.StockAction;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;

/**
 * @author anti
 */
public class BrokerElement extends BaseElement<ShareData<StockAction>>
{
	private Boolean longPosition = null;

	private XYPlot plot = null;

	private IntervalMarker marker = null;

	/**
	 * @param instrument
	 * @param shareDataReceiver
	 */
	public BrokerElement(String instrument)
	{
		super(instrument);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.processor.StockDataReceiver#newDataArrivedNotification
	 * (java.lang.String, stockprocessor.data.StockData)
	 */
	@Override
	public void newDataArrivedNotification(String input, ShareData<StockAction> shareData)
	{
		// if not on chart skip data
		if (plot == null)
			return;

		switch (shareData.getValue())
		{
		case BUY:
			if (longPosition == null)
			{
				// create position
				longPosition = true;
				createMarker(shareData, new Color(0, 255, 0, 150));
			}
			else if (longPosition)
			{
				// move endpoint
				marker.setEndValue(shareData.getTimeStamp().getTime());
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
				createMarker(shareData, new Color(255, 0, 0, 150));
			}
			else if (!longPosition)
			{
				// move endpoint
				marker.setEndValue(shareData.getTimeStamp().getTime());
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
				marker.setEndValue(shareData.getTimeStamp().getTime());
			}
			break;
		}

		// return shareData;
	}

	private void createMarker(ShareData<StockAction> shareData, Paint paint)
	{
		// create new marker
		long time = shareData.getTimeStamp().getTime();
		marker = new IntervalMarker(time, time, paint);
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
	 * stockprocessor.handler.receiver.AbstractDataReceiver#createInputParameters
	 * ()
	 */
	@Override
	protected List<ParameterInformation> createInputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		ParameterInformation parameterInformation = new DefaultParameterInformation("Input", ParameterType.STOCK_ACTION);
		list.add(parameterInformation);

		return list;
	}
}
