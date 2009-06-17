/**
 * 
 */
package stockprocessor.gui.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.commons.lang.StringUtils;

import stockprocessor.gui.view.Chart;
import stockprocessor.gui.view.ChartHolder;

/**
 * @author anti
 */
public class ChartSelectorPanel extends JPanel
{
	private final ChartHolder chartHolder;

	private final boolean canCreateNew;

	private final JRadioButton sizeBigRadioButton;

	private final JRadioButton sizeSmallRadioButton;

	private final JComboBox destinationChartComboBox;

	public ChartSelectorPanel(ChartHolder chartHolder, boolean canCreateNew)
	{
		this.chartHolder = chartHolder;
		this.canCreateNew = canCreateNew;

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Chart"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		setLayout(new GridLayout(4, 2, 2, 2));

		// //////////////////////////////////////////////////////////////////////////////////
		// destination
		// //////////////////////////////////////////////////////////////////////////////////
		// destination chart
		JLabel destinationChartLabel = new JLabel("Destination chart");
		add(destinationChartLabel);

		destinationChartComboBox = new JComboBox(getChartListNames());
		destinationChartComboBox.setSelectedIndex(-1);
		destinationChartComboBox.setEditable(true);
		destinationChartComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean isNewChart = isSelectedNewChart();

				sizeBigRadioButton.setEnabled(isNewChart);
				sizeSmallRadioButton.setEnabled(isNewChart);
			}
		});
		add(destinationChartComboBox);

		// size buttons
		JPanel sizePanel = new JPanel();
		add(sizePanel);

		sizeBigRadioButton = new JRadioButton("Big chart");
		// sizeBigRadioButton.setEnabled(false);
		sizePanel.add(sizeBigRadioButton);

		sizeSmallRadioButton = new JRadioButton("Small chart");
		// sizeSmallRadioButton.setEnabled(false);
		sizePanel.add(sizeSmallRadioButton);

		ButtonGroup sizeButtonGroup = new ButtonGroup();
		sizeButtonGroup.add(sizeBigRadioButton);
		sizeButtonGroup.add(sizeSmallRadioButton);
		sizeBigRadioButton.setSelected(true);
	}

	public void addActionListener(ActionListener listener)
	{
		destinationChartComboBox.addActionListener(listener);
	}

	/**
	 * @return
	 */
	protected String[] getChartListNames()
	{
		List<Chart> chartList = chartHolder.getChartList();
		String[] chartListNames = new String[chartList.size()];
		for (int i = 0; i < chartList.size(); i++)
		{
			chartListNames[i] = chartList.get(i).getName();
		}

		return chartListNames;
	}

	private boolean isSelectedNewChart()
	{
		Object selectedItem = destinationChartComboBox.getSelectedItem();

		boolean isNewChart = destinationChartComboBox.getSelectedIndex() == -1 || !(selectedItem != null && selectedItem.toString().length() > 0);
		return isNewChart;
	}

	public Chart getChart()
	{
		// existing is selected
		if (!isSelectedNewChart())
		{
			return chartHolder.getChart(destinationChartComboBox.getSelectedIndex());
		}

		// if can not create new
		if (!canCreateNew)
		{
			return null;
		}

		// create new
		String name = (String) destinationChartComboBox.getSelectedItem();
		if (StringUtils.isEmpty(name))
		{
			name = "chart";
		}

		boolean isNormalSize = sizeBigRadioButton.isSelected();

		// create new
		Chart chart = new Chart(name, isNormalSize);

		// register the new chart
		chartHolder.addChart(chart);

		// return it
		return chart;
	}
}
