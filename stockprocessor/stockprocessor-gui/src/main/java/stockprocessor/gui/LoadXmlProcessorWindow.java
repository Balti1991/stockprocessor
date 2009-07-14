/**
 * 
 */
package stockprocessor.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.xmlbeans.XmlException;

import stockprocessor.handler.processor.XmlProcessor;
import stockprocessor.handler.xml.ProcessorDocument;
import stockprocessor.manager.InstanceHandler;

/**
 * c:\Work\Workspace\stockprocessor\stockprocessor-xml\src\main\xsd\
 * minimal-sample.xml
 * 
 * @author anti
 */
public class LoadXmlProcessorWindow extends JFileChooser
{
	private int dialogResul = JFileChooser.CANCEL_OPTION;

	public LoadXmlProcessorWindow()
	{
		// common properties
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setMultiSelectionEnabled(false);
		// setCurrentDirectory(new File("~/XmlProcessors"));
		setCurrentDirectory(new File("c:/Work/Workspace/stockprocessor/stockprocessor-xml/src/main/xsd/"));

		// file filters
		addChoosableFileFilter(new FileNameExtensionFilter("XML files", "xml"));
		setAcceptAllFileFilterUsed(true);
	}

	public XmlProcessor<?, ?> getSelectedXmlProcessor()
	{
		// skip if unsuccessful selection
		if (dialogResul != JFileChooser.APPROVE_OPTION)
			return null;

		// create the processor
		File file = getSelectedFile();
		try
		{
			ProcessorDocument processorDocument = ProcessorDocument.Factory.parse(file);
			XmlProcessor<?, ?> xmlProcessor = new XmlProcessor(processorDocument.getProcessor());

			return xmlProcessor;
		}
		catch (XmlException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public InstanceHandler<XmlProcessor<?, ?>> getInstanceHandler()
	{
		// skip if unsuccessful selection
		if (dialogResul != JFileChooser.APPROVE_OPTION)
			return null;

		// create the processor
		final File file = getSelectedFile();
		try
		{
			final ProcessorDocument processorDocument = ProcessorDocument.Factory.parse(file);

			// test it
			XmlProcessor<?, ?> xmlProcessor = new XmlProcessor(processorDocument.getProcessor());

			return new InstanceHandler<XmlProcessor<?, ?>>()
			{

				@Override
				public String getName()
				{
					return file.getName();
				}

				@Override
				public XmlProcessor<?, ?> getInstance()
				{
					return new XmlProcessor(processorDocument.getProcessor());
				}
			};
		}
		catch (XmlException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JFileChooser#showDialog(java.awt.Component,
	 * java.lang.String)
	 */
	@Override
	public int showDialog(Component parent, String approveButtonText) throws HeadlessException
	{
		dialogResul = super.showDialog(parent, approveButtonText);
		return dialogResul;
	}
}
