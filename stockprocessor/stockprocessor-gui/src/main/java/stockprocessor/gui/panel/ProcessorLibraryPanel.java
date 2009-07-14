/**
 * 
 */
package stockprocessor.gui.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import stockprocessor.handler.processor.DataProcessor;
import stockprocessor.manager.DefaultProcessorManager;

/**
 * @author anti
 */
public class ProcessorLibraryPanel extends JPanel
{
	private final JComboBox librarySelector;

	private final JList elementsList;

	public ProcessorLibraryPanel()
	{
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), "Elements"));
		setLayout(new BorderLayout());

		List<String> managers = DefaultProcessorManager.INSTANCE.getAvailableManagers();
		managers.add(0, "");

		librarySelector = new JComboBox(managers.toArray());
		librarySelector.setEditable(false);
		librarySelector.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateElements();
			}
		});
		add(librarySelector, BorderLayout.NORTH);

		elementsList = new JList();
		elementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(elementsList));

		updateElements();
	}

	/**
	 * 
	 */
	private void updateElements()
	{
		elementsList.setListData(getAvailableProcessors());
	}

	public void addActionListener(ListSelectionListener listener)
	{
		elementsList.addListSelectionListener(listener);
	}

	/**
	 * @return
	 */
	protected Object[] getAvailableProcessors()
	{
		String libraryName = (String) librarySelector.getSelectedItem();

		Object[] processors;
		if (libraryName == "")
		{
			processors = DefaultProcessorManager.INSTANCE.getAvailableInstances().toArray();
		}
		else
		{
			processors = DefaultProcessorManager.INSTANCE.getAvailableInstances(libraryName).toArray();
		}

		Arrays.sort(processors);

		return processors;
	}

	public DataProcessor<?, ?> getDataProcessor()
	{
		String elementSelectedValue = (String) elementsList.getSelectedValue();

		if (elementSelectedValue != null)
		{
			return DefaultProcessorManager.INSTANCE.getInstance(elementSelectedValue);
		}

		return null;
	}
}
