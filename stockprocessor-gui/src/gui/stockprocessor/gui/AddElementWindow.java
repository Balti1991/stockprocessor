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
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.StockData;
import stockprocessor.data.information.ListParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.RangeParameterInformation;
import stockprocessor.gui.processor.BaseElement;
import stockprocessor.gui.view.Chart;
import stockprocessor.gui.view.ChartHolder;
import stockprocessor.processor.ProcessorManager;
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

	private Dimension preferredSize;

	private JPanel parametersPanel;

	private JPanel targetPanel;

	private JRadioButton sizeBigRadioButton;

	private JRadioButton sizeSmallRadioButton;

	private JButton resetButton;

	private JButton addButton;

	private JComboBox destinationChartComboBox;

	private JComboBox sourceComboBox;

	private JTextField descriptionTextField;

	private JComboBox instrumentComboBox;

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
		parametersPanel = new JPanel();
		parametersPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Parameters"), BorderFactory.createEmptyBorder(
				10, 10, 10, 10)));
		parametersPanel.setLayout(new GridLayout(0, 2));
		dataPanel.add(parametersPanel);

		// //////////////////////////////////////////////////////////////////////////////////
		// target
		// //////////////////////////////////////////////////////////////////////////////////
		targetPanel = new JPanel();
		targetPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Target"), BorderFactory.createEmptyBorder(10, 10,
				10, 10)));
		targetPanel.setLayout(new GridLayout(4, 2, 2, 2));
		dataPanel.add(targetPanel);

		// source input
		JLabel sourceLabel = new JLabel("Source");
		targetPanel.add(sourceLabel);

		sourceComboBox = new JComboBox(getAvailableSources());
		sourceComboBox.setSelectedIndex(-1);
		sourceComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean selected = sourceComboBox.getSelectedIndex() > -1;

				instrumentComboBox.removeAllItems();

				if (selected)
				{
					// get source
					StockDataSource stockDataSource = sourceManager.getInstance((String) sourceComboBox.getSelectedItem());
					String[] availableInstruments = stockDataSource.getAvailableInstruments();
					for (String instrument : availableInstruments)
					{
						instrumentComboBox.addItem(instrument);
					}
					instrumentComboBox.setSelectedIndex(-1);
				}

				instrumentComboBox.setEnabled(selected);
				updateButtons();
			}
		});
		targetPanel.add(sourceComboBox);

		// source instrument
		JLabel instrumentLabel = new JLabel("Instrument");
		targetPanel.add(instrumentLabel);

		instrumentComboBox = new JComboBox();
		instrumentComboBox.setEnabled(false);
		instrumentComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateButtons();
			}
		});
		targetPanel.add(instrumentComboBox);

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
	private Object[] getAvailableSources()
	{
		Object[] sources = sourceManager.getAvailableSources().toArray();
		Arrays.sort(sources);

		return sources;
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
		addButton.setEnabled(isElementSelected && instrumentComboBox.getSelectedIndex() > -1);

		// destination buttons
		sizeBigRadioButton.setEnabled(isNewChart);
		sizeSmallRadioButton.setEnabled(isNewChart);
	}

	private void updateProperties()
	{
		String elementSelectedValue = (String) elementsList.getSelectedValue();
		boolean isElementSelected = elementSelectedValue != null;

		// clear properties
		parametersPanel.removeAll();
		int rowCounter = 0;

		// create new properties (if required)
		if (isElementSelected)
		{
			StockDataProcessor<?> stockDataProcessor = processorManager.getInstance(elementSelectedValue);

			// processorManager.getInputParameterInfo(elementSelectedValue); //
			// FIXME
			//
			// CoreMetaData2 coreMetaData =
			// processorManager.getCoreMetaData(elementSelectedValue);
			//
			// descriptionTextField.setText(coreMetaData.getFuncInfo().hint());

			descriptionTextField.setText(stockDataProcessor.getDescription());
			List<ParameterInformation> optionalInputParameterInformations = stockDataProcessor.getOptionalInputParameterInformations();

			// int nbOptInput = coreMetaData.getFuncInfo().nbOptInput();
			List<ParameterInformation> parameterInformations = stockDataProcessor.getOptionalInputParameterInformations();
			log.debug("ParameterInfoList size:" + parameterInformations.size());

			for (int i = 0; i < parameterInformations.size(); i++)
			{
				// OptInputParameterInfo optInputParameterInfo =
				// coreMetaData.getOptInputParameterInfo(i);
				ParameterInformation parameterInformation = parameterInformations.get(i);

				log.debug(parameterInformation);

				parametersPanel.add(new JLabel(parameterInformation.getDisplayName()));

				switch (parameterInformation.type())
				{
				// INTEGER_LIST
				case LIST:
				{
					// IntegerList optInputIntegerList
					// coreMetaData.getOptInputIntegerList(i);
					ListParameterInformation listParameterInformation = (ListParameterInformation) parameterInformation;

					JComboBox comboBox = new JComboBox(listParameterInformation.getStringValues());
					comboBox.setSelectedItem("" + parameterInformation.getDefaultValue()); // FIXME
					comboBox.setPreferredSize(preferredSize);

					parametersPanel.add(comboBox);
					break;
				}
					// case REAL_LIST:
					// {
					// // RealList optInputRealList =
					// // coreMetaData.getOptInputRealList(i);
					//
					// JComboBox comboBox = new
					// JComboBox(parameterInformation.string());
					// //
					// comboBox.setSelectedIndex(optInputRealList.defaultValue());
					// // FIXME ez hogy?
					// comboBox.setPreferredSize(preferredSize);
					//
					// parametersPanel.add(comboBox);
					// break;
					// }

					// INTEGER_RANGE
				case RANGE:
				{
					// IntegerRange optInputIntegerRange =
					// coreMetaData.getOptInputIntegerRange(i);

					RangeParameterInformation rangeParameterInformation = (RangeParameterInformation) parameterInformation;

					double defaultValue = rangeParameterInformation.getDefaultValue().doubleValue();
					double increment = rangeParameterInformation.getIncrement().doubleValue();

					double suggested_start = rangeParameterInformation.getStart().doubleValue();
					double suggested_end = rangeParameterInformation.getEnd().doubleValue();

					// corrections
					suggested_start = (suggested_start < defaultValue) ? suggested_start : defaultValue;
					suggested_end = (defaultValue < suggested_end) ? suggested_end : defaultValue;

					SpinnerNumberModel spinnerNumberModel = //
					new SpinnerNumberModel(defaultValue, suggested_start, suggested_end, increment);

					JSpinner spinner = new JSpinner(spinnerNumberModel);
					spinner.setPreferredSize(preferredSize);

					parametersPanel.add(spinner);
					break;
				}
					// case REAL_RANGE:
					// {
					// // RealRange optInputRealRange =
					// // coreMetaData.getOptInputRealRange(i);
					//
					// double defaultValue =
					// parameterInformation.getDefaultValue();
					// double suggested_start = parameterInformation.getStart();
					// double suggested_end = parameterInformation.getEnd();
					//
					// // corrections
					// suggested_start = (suggested_start < defaultValue) ?
					// suggested_start : defaultValue;
					// suggested_end = (defaultValue < suggested_end) ?
					// suggested_end : defaultValue;
					//
					// SpinnerNumberModel spinnerNumberModel = //
					// new SpinnerNumberModel(defaultValue, suggested_start,
					// suggested_end, parameterInformation.getIncrement());
					//
					// JSpinner spinner = new JSpinner(spinnerNumberModel);
					// spinner.setPreferredSize(preferredSize);
					//
					// parametersPanel.add(spinner);
					// break;
					// }

				default:
					break;
				}

				rowCounter++;
			}
		}

		// visual extra
		for (int i = rowCounter; i < 8; i++) // FIXME auto max row count
		{
			parametersPanel.add(new JLabel(" "));
			JTextField textField = new JTextField();
			textField.setEnabled(false);
			parametersPanel.add(textField);

			if (preferredSize == null)
				preferredSize = textField.getPreferredSize();
			else
				textField.setPreferredSize(preferredSize);

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

		String instrument = (String) instrumentComboBox.getSelectedItem();

		// create element
		BaseElement element = new BaseElement(instrument, (StockDataReceiver<StockData<?>>) stockDataProcessor);

		StockDataSource stockDataSource = sourceManager.getInstance((String) sourceComboBox.getSelectedItem());
		stockDataSource.registerDataReceiver(instrument, element);

		sourceManager.registerElement(element);

		chart.addElement(element);
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
