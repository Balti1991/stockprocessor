/**
 * 
 */
package stockprocessor.gui.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.information.ParameterInformation;
import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.handler.source.DataSource;
import stockprocessor.handler.source.SourceManager;
import stockprocessor.util.Pair;

/**
 * @author anti
 */
public class DataSourcePanel extends JPanel
{
	private static final long serialVersionUID = 3284774977493459651L;

	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(DataSourcePanel.class);

	private final List<ParameterInformation> parameterInformations = new ArrayList<ParameterInformation>();

	private final Map<String, Pair<String, String>> parameterMap = new HashMap<String, Pair<String, String>>();

	private final JComboBox parameterComboBox;

	private final JComboBox sourceComboBox;

	private final JComboBox instrumentComboBox;

	/**
	 * 
	 */
	public DataSourcePanel(final SourceManager sourceManager)
	{
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
				boolean selected = parameterComboBox.getSelectedIndex() > -1;

				sourceComboBox.setSelectedIndex(-1);
				sourceComboBox.setEnabled(selected);

				String selectedParameter = (String) parameterComboBox.getSelectedItem();
				if (selected && parameterMap.containsKey(selectedParameter))
				{
					Pair<String, String> pair = parameterMap.get(selectedParameter);
					sourceComboBox.setSelectedItem(pair.getFirst());
					instrumentComboBox.setSelectedItem(pair.getSecond());
				}
			}
		});
		add(parameterComboBox);

		// source input
		JLabel sourceLabel = new JLabel("Source");
		add(sourceLabel);

		sourceComboBox = new JComboBox(getAvailableSources(sourceManager));
		sourceComboBox.setSelectedIndex(-1);
		sourceComboBox.setEnabled(false);
		sourceComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean selected = sourceComboBox.getSelectedIndex() > -1;

				// update map
				Pair<String, String> pair = new Pair<String, String>((String) sourceComboBox.getSelectedItem(), null);
				parameterMap.put((String) parameterComboBox.getSelectedItem(), pair);

				// update components
				instrumentComboBox.removeAllItems();

				if (selected)
				{
					// get source
					DataSource<?> stockDataSource = sourceManager.getInstance((String) sourceComboBox.getSelectedItem());
					for (ParameterInformation parameterInformation : stockDataSource.getOutputParameterInformations())
					{
						instrumentComboBox.addItem(parameterInformation.getDisplayName());
					}
					instrumentComboBox.setSelectedIndex(-1);
				}

				instrumentComboBox.setEnabled(selected);
			}
		});
		add(sourceComboBox);

		// source instrument
		JLabel instrumentLabel = new JLabel("Instrument");
		add(instrumentLabel);

		instrumentComboBox = new JComboBox();
		instrumentComboBox.setEnabled(false);
		instrumentComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// update map
				Pair<String, String> pair = parameterMap.get(parameterComboBox.getSelectedItem());
				pair.setSecond((String) instrumentComboBox.getSelectedItem());
			}
		});
		add(instrumentComboBox);
	}

	public void setStockDataProcessor(DataProcessor<?, ?> stockDataProcessor)
	{
		// clear properties
		// clear map & disable
		parameterMap.clear();
		parameterInformations.clear();
		parameterComboBox.removeAllItems();

		// finish if no processor
		if (stockDataProcessor == null)
			return;

		parameterInformations.addAll(stockDataProcessor.getInputParameterInformations());
		log.debug("Input ParameterInfoList size:" + parameterInformations.size());

		for (Iterator<ParameterInformation> iterator = parameterInformations.iterator(); iterator.hasNext();)
		{
			ParameterInformation parameterInformation = iterator.next();

			parameterComboBox.addItem(parameterInformation.getDisplayName());
		}
	}

	public Map<String, Object> getParameters()
	{
		// create the collector map
		Map<String, Object> parameters = new HashMap<String, Object>();

		for (Iterator<ParameterInformation> parameterIterater = parameterInformations.iterator(); parameterIterater.hasNext();)
		{
			ParameterInformation paramter = parameterIterater.next();

			String name = paramter.getDisplayName();
			if (parameterMap.containsKey(name))
			{
				Pair<String, String> pair = parameterMap.get(name);
				parameters.put(name, pair);
			}
			else
			{
				parameters.put(name, new Pair<String, String>(null, null));
			}
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
	private Object[] getAvailableSources(SourceManager sourceManager)
	{
		Object[] sources = sourceManager.getAvailableSources().toArray();
		Arrays.sort(sources);

		return sources;
	}

	@Deprecated
	public boolean isSelected()
	{
		// FIXME from map
		return instrumentComboBox.getSelectedIndex() > -1;
	}
}
