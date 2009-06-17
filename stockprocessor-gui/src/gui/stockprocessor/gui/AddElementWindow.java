/**
 * 
 */
package stockprocessor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.broker.StockBroker;
import stockprocessor.data.StockData;
import stockprocessor.gui.panel.DataSourcePanel;
import stockprocessor.gui.panel.ProcessorParametersPanel;
import stockprocessor.gui.processor.BrokerElement;
import stockprocessor.gui.processor.ChartElement;
import stockprocessor.gui.view.Chart;
import stockprocessor.gui.view.ChartHolder;
import stockprocessor.processor.ProcessorManager;
import stockprocessor.processor.StockAction;
import stockprocessor.processor.StockDataProcessor;
import stockprocessor.processor.StockDataReceiver;
import stockprocessor.source.StockDataSource;
import stockprocessor.stock.source.SourceManager;

/**
 * @author anti
 */
public class AddElementWindow extends JDialog
{
	private static final long serialVersionUID = -9015932812783363070L;

	private static final Log log = LogFactory.getLog(AddElementWindow.class);

	private final ChartHolder chartHolder;

	private final ProcessorManager processorManager;

	private final SourceManager sourceManager;

	private JList elementsList;

	// private JPanel parametersPanel;

	private ProcessorParametersPanel parametersPanel;

	private DataSourcePanel sourcePanel;

	private JPanel targetPanel;

	private JRadioButton sizeBigRadioButton;

	private JRadioButton sizeSmallRadioButton;

	private JButton resetButton;

	private JButton addButton;

	private JComboBox destinationChartComboBox;

	private JTextField descriptionTextField;

	public AddElementWindow(ChartHolder chartHolder, ProcessorManager processorManager, SourceManager sourceManager)
	{
		this.chartHolder = chartHolder;
		this.processorManager = processorManager;
		this.sourceManager = sourceManager;

		setTitle("Add a new chart element");
		setModal(true);

		initGui();

		pack();
	}

	/**
	 * 
	 */
	protected void initGui()
	{
		// main content
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPane.setOneTouchExpandable(true);
		mainPane.setContinuousLayout(true);
		mainPane.setDividerLocation(0.25);
		add(mainPane);

		// //////////////////////////////////////////////////////////////////////////////////
		// available elements
		// //////////////////////////////////////////////////////////////////////////////////
		JPanel elementsPanel = new JPanel();
		elementsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), "Elements"));
		elementsPanel.setLayout(new BorderLayout());
		mainPane.add(elementsPanel);

		elementsList = new JList();
		elementsList.setListData(getAvailableProcessors());
		elementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		elementsList.addListSelectionListener(new ListSelectionListener()
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
				if (!e.getValueIsAdjusting())
				{
					updateButtons();
					updateProperties();
				}
			}
		});
		elementsPanel.add(new JScrollPane(elementsList));

		// //////////////////////////////////////////////////////////////////////////////////
		// data area
		// //////////////////////////////////////////////////////////////////////////////////
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		mainPane.add(dataPanel);

		descriptionTextField = new JTextField();

		// parameters
		parametersPanel = new ProcessorParametersPanel();
		dataPanel.add(parametersPanel);

		// sources
		sourcePanel = new DataSourcePanel(sourceManager);
		sourcePanel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateButtons();
			}
		});
		dataPanel.add(sourcePanel);

		// //////////////////////////////////////////////////////////////////////////////////
		// target
		// //////////////////////////////////////////////////////////////////////////////////
		targetPanel = new JPanel();
		targetPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Target"), BorderFactory.createEmptyBorder(10, 10,
				10, 10)));
		targetPanel.setLayout(new GridLayout(4, 2, 2, 2));
		dataPanel.add(targetPanel);

		// //////////////////////////////////////////////////////////////////////////////////
		// destination
		// //////////////////////////////////////////////////////////////////////////////////
		// destination chart
		JLabel destinationChartLabel = new JLabel("Destination chart");
		targetPanel.add(destinationChartLabel);

		destinationChartComboBox = new JComboBox(getChartListNames());
		destinationChartComboBox.setSelectedIndex(-1);
		destinationChartComboBox.addActionListener(new ActionListener()
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
				updateButtons();
			}
		});
		destinationChartComboBox.setEditable(true);
		targetPanel.add(destinationChartComboBox);

		// size buttons
		JPanel sizePanel = new JPanel();
		targetPanel.add(sizePanel);

		sizeBigRadioButton = new JRadioButton("Big chart");
		sizeBigRadioButton.setEnabled(false);
		sizePanel.add(sizeBigRadioButton);

		sizeSmallRadioButton = new JRadioButton("Small chart");
		sizeSmallRadioButton.setEnabled(false);
		sizePanel.add(sizeSmallRadioButton);

		ButtonGroup sizeButtonGroup = new ButtonGroup();
		sizeButtonGroup.add(sizeBigRadioButton);
		sizeButtonGroup.add(sizeSmallRadioButton);
		sizeBigRadioButton.setSelected(true);

		// //////////////////////////////////////////////////////////////////////////////////
		// buttons
		// //////////////////////////////////////////////////////////////////////////////////
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		dataPanel.add(buttonPanel);

		resetButton = new JButton(new AbstractAction("Reset values")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// reset function
				updateButtons();
			}
		});
		resetButton.setEnabled(false);
		buttonPanel.add(resetButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		addButton = new JButton(new AbstractAction("OK")
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
				addElement2Chart();
				dispose();
			}
		});
		addButton.setEnabled(false);
		buttonPanel.add(addButton);

		updateButtons();
		updateProperties();
	}

	/**
	 * @return
	 */
	protected Object[] getAvailableProcessors()
	{
		Object[] processors = processorManager.getAvailableProcessors().toArray();
		Arrays.sort(processors);

		return processors;
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

	/**
	 * 
	 */
	protected void updateButtons()
	{
		boolean isElementSelected = elementsList.getSelectedValue() != null;
		boolean isNewChart = isSelectedNewChart();

		// form buttons
		resetButton.setEnabled(isElementSelected);
		addButton.setEnabled(isElementSelected && sourcePanel.isSelected());

		// destination buttons
		sizeBigRadioButton.setEnabled(isNewChart);
		sizeSmallRadioButton.setEnabled(isNewChart);
	}

	private void updateProperties()
	{
		String elementSelectedValue = (String) elementsList.getSelectedValue();
		boolean isElementSelected = elementSelectedValue != null;

		if (isElementSelected)
		{
			// create new properties (if required)
			StockDataProcessor<?> stockDataProcessor = processorManager.getInstance(elementSelectedValue);

			descriptionTextField.setText(stockDataProcessor.getDescription());
			parametersPanel.setStockDataProcessor(stockDataProcessor);
			sourcePanel.setStockDataProcessor(stockDataProcessor);
		}
		else
		{
			// clear
			descriptionTextField.setText("");
			parametersPanel.setStockDataProcessor(null);
			sourcePanel.setStockDataProcessor(null);
		}

		// layout
		validate();
	}

	/**
	 * 
	 */
	private void addElement2Chart()
	{
		Chart chart;

		// get chart
		if (isSelectedNewChart())
		{
			// create new
			String name = (String) destinationChartComboBox.getSelectedItem();
			if (StringUtils.isEmpty(name))
			{
				name = "new chart";
			}

			boolean normalSize = sizeBigRadioButton.isSelected();

			// create
			chart = new Chart(name, normalSize);

			// store
			chartHolder.addChart(chart);
		}
		else
		{
			// get selected
			chart = chartHolder.getChart(destinationChartComboBox.getSelectedIndex());
		}
		log.debug("Useing chart [" + chart + "]");

		// create processor
		StockDataProcessor<?> stockDataProcessor = processorManager.getInstance((String) elementsList.getSelectedValue());

		// apply the values
		stockDataProcessor.setOptionalInputParameterInformations(parametersPanel.getParameters());

		String instrument = sourcePanel.getInstrument();

		// create element
		ChartElement element = new ChartElement(instrument, (StockDataReceiver<StockData<?>>) stockDataProcessor);

		StockDataSource stockDataSource = sourcePanel.getStockDataSource();
		stockDataSource.registerDataReceiver(instrument, element);

		sourceManager.registerElement(element);

		chart.addElement(element);

		// FIXME
		BrokerElement<StockData<?>> brokerElement = new BrokerElement<StockData<?>>(new StockBroker<StockData<?>>()
		{
			@Override
			public String getName()
			{
				return "test";
			}

			@Override
			public StockAction newDataArrivedNotification(String instrument, StockData<?> stockData)
			{
				double random = Math.random();

				if (random > 0.88)
				{
					log.debug("BUY");
					return StockAction.BUY;
				}
				if (random < 0.11)
				{
					log.debug("SELL");
					return StockAction.SELL;
				}

				log.debug("NOP");
				return StockAction.NOP;
			}
		});

		chart.registerBrokerElement(brokerElement);
		stockDataSource.registerDataReceiver(instrument, brokerElement);
	}

	/**
	 * @return
	 */
	private boolean isSelectedNewChart()
	{
		Object selectedItem = destinationChartComboBox.getSelectedItem();

		boolean isNewChart = destinationChartComboBox.getSelectedIndex() == -1 || !(selectedItem != null && selectedItem.toString().length() > 0);
		return isNewChart;
	}
}
