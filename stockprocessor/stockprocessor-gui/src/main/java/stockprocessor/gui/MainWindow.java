/**
 * 
 */
package stockprocessor.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import stockprocessor.Configuration;
import stockprocessor.gui.view.ChartHolder;
import stockprocessor.handler.InstanceHandler;
import stockprocessor.manager.DefaultProcessorManager;
import stockprocessor.manager.XmlProcessorManager;

/**
 * @author anti
 */
public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1854063225675308962L;

	protected ChartHolder chartHolder;

	public MainWindow()
	{
		// initialize
		Configuration.INSTANCE.initialize();

		// prepare gui
		setTitle("Stock GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		chartHolder = new ChartHolder();

		createMenu();
		add(chartHolder);

		pack();
	}

	public static void main(String[] args)
	{
		new MainWindow().setVisible(true);
	}

	// //////////////
	// MENU CREATION
	// //////////////

	/**
	 * creates menu hierarchy for application
	 */
	protected void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuBar.add(createFileMenu());
		menuBar.add(createViewMenu());
		menuBar.add(createChartMenu());
	}

	/**
	 * creates the file menu
	 * 
	 * @return
	 */
	protected JMenu createFileMenu()
	{
		// file menu
		JMenu menu = new JMenu("File");
		@SuppressWarnings("serial")
		JMenuItem newObject = new JMenuItem(new AbstractAction("New")
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(newObject);

		@SuppressWarnings("serial")
		JMenuItem open = new JMenuItem(new AbstractAction("Load XML Processor")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				LoadXmlProcessorWindow loadXmlProcessorWindow = new LoadXmlProcessorWindow();
				loadXmlProcessorWindow.showOpenDialog(MainWindow.this);

				InstanceHandler instanceHandler = loadXmlProcessorWindow.getInstanceHandler();
				if (instanceHandler != null)
				{
					DefaultProcessorManager.INSTANCE.getProcessorManager(XmlProcessorManager.NAME).registerInstanceHandler(instanceHandler);
				}
			}
		});
		menu.add(open);

		@SuppressWarnings("serial")
		JMenuItem save = new JMenuItem(new AbstractAction("Save")
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(save);

		menu.addSeparator();

		@SuppressWarnings("serial")
		JMenuItem selectSource = new JMenuItem(new AbstractAction("Select source")
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(selectSource);

		menu.addSeparator();

		@SuppressWarnings("serial")
		JMenuItem exit = new JMenuItem(new AbstractAction("Exit")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menu.add(exit);

		return menu;
	}

	/**
	 * creates the file menu
	 * 
	 * @return
	 */
	protected JMenu createViewMenu()
	{
		JMenu menu = new JMenu("View");

		@SuppressWarnings("serial")
		JMenuItem newObject = new JMenuItem(new AbstractAction("Show parameters")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(newObject);

		menu.addSeparator();

		@SuppressWarnings("serial")
		JMenuItem zoomIn = new JMenuItem(new AbstractAction("Zoom In")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(zoomIn);

		@SuppressWarnings("serial")
		JMenuItem zoomOut = new JMenuItem(new AbstractAction("Zoom Out")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(zoomOut);

		@SuppressWarnings("serial")
		JMenuItem zoomFit = new JMenuItem(new AbstractAction("Show All")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		menu.add(zoomFit);

		return menu;
	}

	protected JMenu createChartMenu()
	{
		JMenu menu = new JMenu("Chart");

		@SuppressWarnings("serial")
		final JCheckBoxMenuItem autoWidth = new JCheckBoxMenuItem();
		autoWidth.setAction(new AbstractAction("Auto Width")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				chartHolder.setAutoChartWidth(autoWidth.isSelected());
				validate();
			}
		});
		menu.add(autoWidth);

		@SuppressWarnings("serial")
		final JCheckBoxMenuItem autoHeight = new JCheckBoxMenuItem();
		autoHeight.setAction(new AbstractAction("Auto Height")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				chartHolder.setAutoChartHeight(autoHeight.isSelected());
				validate();
			}
		});
		menu.add(autoHeight);

		@SuppressWarnings("serial")
		JMenuItem setSize = new JMenuItem(new AbstractAction("Set size")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ChartPropertiesWindow window = new ChartPropertiesWindow(chartHolder);
				window.setVisible(true);

				validate();
			}
		});
		menu.add(setSize);

		menu.addSeparator();

		@SuppressWarnings("serial")
		JMenuItem addElement = new JMenuItem(new AbstractAction("Add element")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				AddElementWindow window = new AddElementWindow(chartHolder);
				window.setVisible(true);

				validate();
			}
		});
		menu.add(addElement);

		@SuppressWarnings("serial")
		JMenuItem remove = new JMenuItem(new AbstractAction("Remove chart/element")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				RemoveChartOrElementWindow window = new RemoveChartOrElementWindow(chartHolder);
				window.setVisible(true);

				validate();
			}
		});
		menu.add(remove);

		return menu;
	}
}
