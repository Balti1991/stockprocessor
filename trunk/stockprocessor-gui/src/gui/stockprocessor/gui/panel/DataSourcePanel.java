/**
 * 
 */
package stockprocessor.gui.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.processor.StockDataProcessor;
import stockprocessor.source.StockDataSource;
import stockprocessor.stock.source.SourceManager;

/**
 * @author anti
 */
public class DataSourcePanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(DataSourcePanel.class);

	private final SourceManager sourceManager;

	private List<ParameterInformation> parameterInformations = null;

	private final JComboBox parameterComboBox;

	private final JComboBox sourceComboBox;

	private final JComboBox instrumentComboBox;

	/**
	 * 
	 */
	public DataSourcePanel(final SourceManager sourceManager)
	{
		this.sourceManager = sourceManager;

		setBorder(BorderFactory
				.createCompoundBorder(BorderFactory.createTitledBorder("Data Source"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		setLayout(new GridLayout(0, 2));

		// parameter input
		JLabel parameterLabel = new JLabel("Input parameter");
		add(parameterLabel);

		parameterComboBox = new JComboBox();
		parameterComboBox.setSelectedIndex(-1);
		parameterComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO
			}
		});
		add(parameterComboBox);

		// source input
		JLabel sourceLabel = new JLabel("Source");
		add(sourceLabel);

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
				// updateButtons();
			}
		});
		add(sourceComboBox);

		// source instrument
		JLabel instrumentLabel = new JLabel("Instrument");
		add(instrumentLabel);

		instrumentComboBox = new JComboBox();
		instrumentComboBox.setEnabled(false);
		add(instrumentComboBox);
	}

	public void setStockDataProcessor(StockDataProcessor<?> stockDataProcessor)
	{
		// clear properties
		// TODO clear map & disable

		// finish if no processor
		if (stockDataProcessor == null)
			return;

		parameterInformations = stockDataProcessor.getInputParameterInformations();

		// int nbOptInput = coreMetaData.getFuncInfo().nbOptInput();
		List<ParameterInformation> parameterInformations = stockDataProcessor.getOptionalInputParameterInformations();
		log.debug("Input-ParameterInfoList size:" + parameterInformations.size());

		for (int i = 0; i < parameterInformations.size(); i++)
		{
			// TODO
		}
	}

	public Map<String, Object> getParameters()
	{
		// create the collector map
		Map<String, Object> parameters = new HashMap<String, Object>();

		for (int i = 0; i < parameterInformations.size(); i++)
		{
			// TODO
		}

		return parameters;
	}

	public void addActionListener(ActionListener listener)
	{
		parameterComboBox.addActionListener(listener);
		sourceComboBox.addActionListener(listener);
		instrumentComboBox.addActionListener(listener);
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

	@Deprecated
	public StockDataSource<?> getStockDataSource()
	{
		if (!isSelected())
			return null;

		return sourceManager.getInstance((String) sourceComboBox.getSelectedItem());
	}

	@Deprecated
	public String getInstrument()
	{
		if (!isSelected())
			return null;

		return (String) instrumentComboBox.getSelectedItem();
	}

	public boolean isSelected()
	{
		// FIXME from map
		return instrumentComboBox.getSelectedIndex() > -1;
	}
}
