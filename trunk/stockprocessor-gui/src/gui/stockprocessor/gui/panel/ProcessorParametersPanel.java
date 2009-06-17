/**
 * 
 */
package stockprocessor.gui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.information.ListParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.RangeParameterInformation;
import stockprocessor.processor.StockDataProcessor;

/**
 * @author anti
 */
public class ProcessorParametersPanel extends JPanel
{
	private static final long serialVersionUID = 8139691941537287938L;

	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(ProcessorParametersPanel.class);

	private Dimension preferredSize;

	private List<ParameterInformation> parameterInformations = null;

	/**
	 * 
	 */
	public ProcessorParametersPanel()
	{
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Parameters"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		setLayout(new GridLayout(0, 2));
	}

	public void setStockDataProcessor(StockDataProcessor<?> stockDataProcessor)
	{
		// clear properties
		removeAll();

		// finish if no processor
		if (stockDataProcessor == null)
			return;

		parameterInformations = stockDataProcessor.getOptionalInputParameterInformations();
		log.debug("Optional-ParameterInfoList size:" + parameterInformations.size());

		for (int i = 0; i < parameterInformations.size(); i++)
		{
			// OptInputParameterInfo optInputParameterInfo =
			// coreMetaData.getOptInputParameterInfo(i);
			ParameterInformation parameterInformation = parameterInformations.get(i);

			log.debug(parameterInformation);

			add(new JLabel(parameterInformation.getDisplayName()));

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

				add(comboBox);
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
				// add(comboBox);
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

				add(spinner);
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
				// add(spinner);
				// break;
				// }

			default:
				break;
			}
		}

		// // visual extra
		// for (int i = rowCounter; i < 8; i++)
		// {
		// add(new JLabel(" "));
		// JTextField textField = new JTextField();
		// textField.setEnabled(false);
		// add(textField);
		//
		// if (preferredSize == null)
		// preferredSize = textField.getPreferredSize();
		// else
		// textField.setPreferredSize(preferredSize);
		// }

		// layout
		validate();
	}

	public Map<String, Object> getParameters()
	{
		// create the collector map
		Map<String, Object> parameters = new HashMap<String, Object>();

		for (int i = 0; i < parameterInformations.size(); i++)
		{
			ParameterInformation parameterInformation = parameterInformations.get(i);
			String displayName = parameterInformation.getDisplayName();
			Component component = getComponent(i * 2 + 1);

			log.debug(parameterInformation);

			switch (parameterInformation.type())
			{
			case LIST:
			{
				Object selectedItem = ((JComboBox) component).getSelectedItem();
				log.debug("Selected LIST value: [" + selectedItem + "]");

				parameters.put(displayName, selectedItem);
				break;
			}
			case RANGE:
			{
				Object value = ((JSpinner) component).getModel().getValue();
				log.debug("Selected RANGE value: [" + value + "]");

				parameters.put(displayName, value);
				break;
			}

			default:
				break;
			}
		}

		return parameters;
	}
}
