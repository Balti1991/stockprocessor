/**
 * 
 */
package stockprocessor.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.broker.RandomBroker;
import stockprocessor.broker.SimpleBrokerHouse;
import stockprocessor.data.ShareData;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.gui.handler.receiver.BrokerElement;
import stockprocessor.gui.handler.receiver.CandleElement;
import stockprocessor.gui.handler.receiver.Element;
import stockprocessor.gui.handler.receiver.TimeElement;
import stockprocessor.gui.panel.ChartSelectorPanel;
import stockprocessor.gui.panel.DataSourcePanel;
import stockprocessor.gui.panel.ProcessorLibraryPanel;
import stockprocessor.gui.panel.ProcessorParametersPanel;
import stockprocessor.gui.view.Chart;
import stockprocessor.gui.view.ChartHolder;
import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.handler.receiver.DataReceiver;
import stockprocessor.handler.source.DataSource;
import stockprocessor.manager.DefaultSourceManager;
import stockprocessor.util.Pair;

/**
 * @author anti
 */
public class AddElementWindow extends JDialog
{
	private static final long serialVersionUID = -9015932812783363070L;

	private static final Log log = LogFactory.getLog(AddElementWindow.class);

	private final ChartHolder chartHolder;

	private ProcessorLibraryPanel processorLibraryPanel;

	private ProcessorParametersPanel parametersPanel;

	private DataSourcePanel sourcePanel;

	private ChartSelectorPanel targetPanel;

	private JButton addButton;

	private JTextArea descriptionTextField;

	public AddElementWindow(ChartHolder chartHolder)
	{
		this.chartHolder = chartHolder;

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
		processorLibraryPanel = new ProcessorLibraryPanel();
		processorLibraryPanel.addActionListener(new ListSelectionListener()
		{
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
		mainPane.add(processorLibraryPanel);

		// //////////////////////////////////////////////////////////////////////////////////
		// data area
		// //////////////////////////////////////////////////////////////////////////////////
		JPanel dataPanel = new JPanel();
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		mainPane.add(dataPanel);

		descriptionTextField = new JTextArea();
		descriptionTextField.setEditable(false);
		dataPanel.add(descriptionTextField);

		// parameters
		parametersPanel = new ProcessorParametersPanel();
		dataPanel.add(parametersPanel);

		// sources
		sourcePanel = new DataSourcePanel();
		sourcePanel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateButtons();
			}
		});
		dataPanel.add(sourcePanel);

		// target
		targetPanel = new ChartSelectorPanel(chartHolder, true);
		targetPanel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateButtons();
			}
		});
		dataPanel.add(targetPanel);

		// //////////////////////////////////////////////////////////////////////////////////
		// buttons
		// //////////////////////////////////////////////////////////////////////////////////
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		dataPanel.add(buttonPanel);

		JButton resetButton;
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
	 * 
	 */
	protected void updateButtons()
	{
		boolean isElementSelected = processorLibraryPanel.getDataProcessor() != null;

		// form buttons
		// resetButton.setEnabled(isElementSelected);
		addButton.setEnabled(isElementSelected && sourcePanel.isSelected());
	}

	private void updateProperties()
	{
		DataProcessor<?, ?> stockDataProcessor = processorLibraryPanel.getDataProcessor();

		if (stockDataProcessor != null)
		{
			// create new properties (if required)
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
		repaint();
	}

	/**
	 * 
	 */
	private void addElement2Chart()
	{
		// create processor
		DataProcessor<?, ?> stockDataProcessor = processorLibraryPanel.getDataProcessor();

		// apply the parameters
		stockDataProcessor.setOptionalParameters(parametersPanel.getParameters());
		String name = stockDataProcessor.getName();

		Chart chart = targetPanel.getChart();
		log.debug("Useing chart [" + chart + "]");

		// create element
		Element element;
		for (ParameterInformation outputParameterInformation : stockDataProcessor.getOutputParameters())
		{
			switch (outputParameterInformation.getType())
			{
			// case STOCK_ACTION:
			// element = new BrokerElement<StockData<?>>(); TODO
			// break;
			case STOCK_DATA_NUMBER:
				element = new TimeElement(name + "(" + outputParameterInformation.getDisplayName() + ")");
				break;
			case STOCK_DATA_CANDLE:
				element = new CandleElement(name + "(" + outputParameterInformation.getDisplayName() + ")");
				break;

			default:
				element = null;
				log.warn("Unknown paramter type: [" + outputParameterInformation.getType() + "]");
				break;
			}

			// register element on processor
			@SuppressWarnings("unchecked")
			DataReceiver dataReceiver = (DataReceiver) element;
			stockDataProcessor.registerDataReceiver(outputParameterInformation.getDisplayName(), dataReceiver, "ChartInput");

			// register element
			element.setPlot(chart.getPlot());
			// chart.addElement(element);
		}

		// FIXME block start
		RandomBroker stockBroker = new RandomBroker();
		stockBroker.setBrokerHouse(new SimpleBrokerHouse());
		BrokerElement<ShareData<?>> brokerElement = new BrokerElement<ShareData<?>>(stockBroker);

		brokerElement.setPlot(chart.getPlot());
		// stockDataProcessor.registerDataReceiver(null, (DataReceiver)
		// brokerElement);
		// FIXME block end

		// register on sources
		Map<String, Pair<String, String>> inputParameters = sourcePanel.getParameters();
		for (Entry<String, Pair<String, String>> inputParameter : inputParameters.entrySet())
		{
			@SuppressWarnings("unchecked")
			Pair<String, String> pair = inputParameter.getValue();

			// register the processor
			@SuppressWarnings("unchecked")
			DataSource stockDataSource = DefaultSourceManager.INSTANCE.getInstance(pair.getFirst());
			stockDataSource.registerDataReceiver(pair.getSecond(), stockDataProcessor, inputParameter.getKey());
		}
	}
}
