/**
 * 
 */
package stockprocessor.gui;


import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import stockprocessor.gui.view.ChartHolder;

/**
 * @author anti
 */
public class ChartPropertiesWindow extends JDialog
{
	private static final long serialVersionUID = -5592425877983709293L;

	private final ChartHolder chartHolder;

	private JTextField heigthtField;

	private JTextField widthField;

	private JTextField ratioField;

	public ChartPropertiesWindow(ChartHolder chartHolder)
	{
		this.chartHolder = chartHolder;
		setTitle("chart properties");
		setModal(true);

		initGui();

		pack();
	}

	protected void initGui()
	{
		// height
		JLabel heightLabel = new JLabel("Height");
		heigthtField = new JTextField("" + chartHolder.getChartHeight(), 5);
		add(heightLabel);
		add(heigthtField);

		// width
		JLabel widthLabel = new JLabel("Width");
		widthField = new JTextField("" + chartHolder.getChartWidth(), 5);
		add(widthLabel);
		add(widthField);

		// ratio
		JLabel ratioLabel = new JLabel("Ratio:");
		ratioField = new JTextField("" + chartHolder.getChartRatio(), 3);
		add(ratioLabel);
		add(ratioField);

		// OK
		JButton okButton = new JButton(new AbstractAction("OK")
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (saveResult())
					dispose();
			}
		});
		add(okButton);

		// cancel
		JButton cancelButton = new JButton(new AbstractAction("Cancel")
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		add(cancelButton);

		// set layout
		LayoutManager layout = new FlowLayout();
		setLayout(layout);

		// setup layout
	}

	public boolean saveResult()
	{
		// get result
		String heightText = heigthtField.getText();
		String widthText = widthField.getText();
		String ratioText = ratioField.getText();

		int height;
		int widt;
		int ratio;
		try
		{
			// parse
			height = DecimalFormat.getInstance().parse(heightText).intValue();
			widt = DecimalFormat.getInstance().parse(widthText).intValue();
			ratio = DecimalFormat.getInstance().parse(ratioText).intValue();
		}
		catch (ParseException e)
		{
			// send error message
			JDialog dialog = new JDialog(this);
			dialog.setTitle("Error");
			dialog.add(new JLabel("Wrong number [" + widthText + "," + heightText + "," + ratioText + "]"));
			dialog.pack();
			dialog.setVisible(true);

			// finish
			return false;
		}

		// save result
		chartHolder.setChartSize(widt, height);
		chartHolder.setChartRatio(ratio);

		// finish
		return true;
	}
}
