/**
 * 
 */
package stockprocessor.gui.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.XYPlot;

import stockprocessor.gui.handler.receiver.Element;

/**
 * @author anti
 */
public class Chart extends JComponent
{
	private static final long serialVersionUID = 8885447286423717151L;

	private static final Log log = LogFactory.getLog(Chart.class);

	private final String name;

	private final List<Element> elementList = new ArrayList<Element>();

	private final JFreeChart chart;

	private final boolean normalSize;

	private final XYPlot plot;

	private static final int PLOT_INDEX_INTEGER = 0;

	private static final int PLOT_INDEX_OHLC = 1;

	/**
	 * @param name
	 * @param normalSize
	 */
	public Chart(String name, boolean normalSize)
	{
		this.normalSize = normalSize;
		this.name = name;

		setDoubleBuffered(true);

		// create XY plot
		plot = new XYPlot();

		// TODO add bar renderer support
		// plot.setRenderer(PLOT_INDEX_INTEGER, new XYLineAndShapeRenderer(true,
		// false));
		// plot.setRenderer(PLOT_INDEX_OHLC, new CandlestickRenderer());

		// X axis
		ValueAxis timeAxis = new DateAxis("Time");
		timeAxis.setLowerMargin(0.02); // reduce the default margins
		timeAxis.setUpperMargin(0.02);
		timeAxis.setAutoTickUnitSelection(true);
		getPlot().setDomainAxis(timeAxis);

		// Y axis
		NumberAxis valueAxis = new NumberAxis("Value");
		valueAxis.setAutoRangeIncludesZero(false); // override default
		valueAxis.setAutoTickUnitSelection(true);
		getPlot().setRangeAxis(valueAxis);

		// set chart
		chart = new JFreeChart(name, getPlot());
		chart.setBorderVisible(true);
		chart.addChangeListener(new ChartChangeListener()
		{
			@Override
			public void chartChanged(ChartChangeEvent event)
			{
				// if dataset changed repaint chart
				if (event.getType() == ChartChangeEventType.DATASET_UPDATED)
					Chart.this.repaint();
			}
		});
	}

	/**
	 * @return the elementList
	 */
	public List<Element> getElementList()
	{
		return elementList;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return the size
	 */
	public boolean isNormalSize()
	{
		return normalSize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g)
	{
		Rectangle bounds = getBounds();
		// log.debug("Drawing chart [" + getName() + "] to [" + bounds + "]");

		chart.draw((Graphics2D) g, new Rectangle(bounds.width, bounds.height));
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

	/**
	 * @return the plot
	 */
	public XYPlot getPlot()
	{
		return plot;
	}
}
