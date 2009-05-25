/**
 * 
 */
package stockprocessor.gui;


import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import stockprocessor.gui.processor.Element;
import stockprocessor.gui.view.Chart;
import stockprocessor.gui.view.ChartHolder;

/**
 * @author anti
 */
public class RemoveChartOrElementWindow extends JDialog
{
	private static final long serialVersionUID = -105440941296039657L;

	private final ChartHolder chartHolder;

	private JButton removeChartButton;

	private JButton removeElementButton;

	private JList chartList;

	private JList elementList;

	private DefaultListModel elementModel;

	private DefaultListModel chartModel;

	public RemoveChartOrElementWindow(ChartHolder chartHolder)
	{
		this.chartHolder = chartHolder;
		setTitle("Remove a chart or an element of a chart");
		setModal(true);

		initGui();

		pack();
	}

	@SuppressWarnings("serial")
	protected void initGui()
	{
		// chart side
		JPanel chartPanel = new JPanel();
		chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Chart"), BorderFactory.createEmptyBorder(10, 10,
				10, 10)));
		chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
		add(chartPanel);

		chartList = new JList();
		chartModel = new DefaultListModel();
		chartList.setModel(chartModel);
		chartList.addListSelectionListener(new ListSelectionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see
			 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing
			 * .event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int selectionCount = chartList.getSelectedIndices().length;

				removeChartButton.setEnabled(selectionCount > 0);

				removeElementButton.setEnabled((selectionCount == 1) && removeElementButton.isEnabled());
				setUpElementList();
			}

		});
		chartPanel.add(chartList);
		setUpChartList();

		removeChartButton = new JButton(new AbstractAction("Remove chart")
		{
			/*
			 * (non-Javadoc)
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeChart();
			}
		});
		removeChartButton.setEnabled(false);
		chartPanel.add(removeChartButton);

		// element side
		JPanel elementPanel = new JPanel();
		elementPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Element"), BorderFactory.createEmptyBorder(10,
				10, 10, 10)));
		elementPanel.setLayout(new BoxLayout(elementPanel, BoxLayout.Y_AXIS));
		add(elementPanel);

		elementList = new JList();
		elementList.setEnabled(false);
		elementModel = new DefaultListModel();
		elementList.setModel(elementModel);
		chartList.addListSelectionListener(new ListSelectionListener()
		{
			/*
			 * (non-Javadoc)
			 * @see
			 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing
			 * .event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				removeElementButton.setEnabled(elementList.getSelectedIndices().length > 0);
			}

		});
		elementPanel.add(elementList);
		setUpElementList();

		removeElementButton = new JButton(new AbstractAction("Remove element")
		{
			/*
			 * (non-Javadoc)
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.
			 * ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				removeElement();
			}
		});
		removeElementButton.setEnabled(false);
		elementPanel.add(removeElementButton);

		// set layout
		LayoutManager layout = new FlowLayout();
		setLayout(layout);

		// setup layout
	}

	protected void setUpChartList()
	{
		for (Chart chart : chartHolder.getChartList())
		{
			chartModel.addElement(chart);
		}
	}

	protected void setUpElementList()
	{
		boolean isSingleSelected = chartList.getSelectedIndices().length == 1;
		elementModel.clear();

		if (isSingleSelected)
		{
			Chart chart = (Chart) chartList.getSelectedValue();
			for (Element element : chart.getElementList())
			{
				elementModel.addElement(element);
			}
		}

		elementList.setEnabled(isSingleSelected);
	}

	protected void removeChart()
	{
		// TODO
	}

	protected void removeElement()
	{
		// TODO
	}
}
