/**
 * 
 */
package stockprocessor.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author anti
 */
public class ChartHolder extends JPanel
{
	private static final Log log = LogFactory.getLog(ChartHolder.class);

	private boolean autoChartWidth;

	private boolean autoChartHeight;

	private int chartWidth;

	private int chartHeight;

	private int chartRatio;

	private final List<Chart> chartList = new ArrayList<Chart>();

	private JPanel panel;

	private JScrollPane scrollPane;

	// private List

	public ChartHolder()
	{
		autoChartWidth = true;
		autoChartHeight = true;

		chartWidth = 800;
		chartHeight = 350;

		chartRatio = 3;

		initGui();
		setPreferredSize(new Dimension(200, 400));
	}

	/***************************************************************************
	 * functionalities
	 **************************************************************************/

	/**
	 * @return the chartList
	 */
	public List<Chart> getChartList()
	{
		return chartList;
	}

	/**
	 * @param chart
	 */
	public void addChart(Chart chart)
	{
		chart.setPreferredSize(new Dimension(chartWidth, chartHeight));

		chartList.add(chart);
		panel.add(chart);

		log.debug("Added new chart [" + chart.getName() + "] new chart list size: [" + chartList.size() + "]");

		validate();
	}

	/**
	 * @param chart
	 */
	public Chart getChart(int index)
	{
		return chartList.get(index);
	}

	/***************************************************************************
	 * visualization
	 **************************************************************************/

	protected void initGui()
	{
		setLayout(new BorderLayout());

		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);

		panel = new JPanel(true);
		panel.setLayout(new GridLayout(0, 1, 10, 10));

		scrollPane.setViewportView(panel);
	}

	/**
	 * @return the autoChartVidth
	 */
	public boolean isAutoChartVidth()
	{
		return autoChartWidth;
	}

	/**
	 * @param autoChartWidth the autoChartVidth to set
	 */
	public void setAutoChartWidth(boolean autoChartWidth)
	{
		this.autoChartWidth = autoChartWidth;
	}

	/**
	 * @return the autoChartHeight
	 */
	public boolean isAutoChartHeight()
	{
		return autoChartHeight;
	}

	/**
	 * @param autoChartHeight the autoChartHeight to set
	 */
	public void setAutoChartHeight(boolean autoChartHeight)
	{
		this.autoChartHeight = autoChartHeight;
	}

	/**
	 * @return the chartWidth
	 */
	public int getChartWidth()
	{
		return chartWidth;
	}

	/**
	 * @return the chartHeight
	 */
	public int getChartHeight()
	{
		return chartHeight;
	}

	/**
	 * @param chartWidth the chartWidth to set
	 * @param chartHeight the chartHeight to set
	 */
	public void setChartSize(int chartWidth, int chartHeight)
	{
		this.chartWidth = chartWidth;
		this.chartHeight = chartHeight;

		for (Chart chart : getChartList())
		{
			chart.setPreferredSize(new Dimension(chartWidth, chartHeight));
		}
	}

	/**
	 * @return the chartRatio
	 */
	public int getChartRatio()
	{
		return chartRatio;
	}

	/**
	 * @param chartRatio the chartRatio to set
	 */
	public void setChartRatio(int chartRatio)
	{
		this.chartRatio = chartRatio;
	}
}
