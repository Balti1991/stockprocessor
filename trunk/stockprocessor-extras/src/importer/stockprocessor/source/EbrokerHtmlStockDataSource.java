/**
 * 
 */
package stockprocessor.source;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import org.apache.commons.lang.StringUtils;

import stockprocessor.data.StockData;
import stockprocessor.source.AbstractHtmlStockDataSource;
import stockprocessor.source.ImportTimer;

/**
 * @author anti
 */
public class EbrokerHtmlStockDataSource extends AbstractHtmlStockDataSource<StockData<Integer>>
{
	protected final static String url = "http://www.ebroker.hu/pls/ebrk/new_arfolyam_html_p.startup";

	protected final HashSet<String> instruments = new HashSet<String>();

	private final long timerStep = 1000 * 60;

	public EbrokerHtmlStockDataSource()
	{
		new ImportTimer(getName(), timerStep)
		{
			@Override
			protected void timeTick()
			{
				try
				{
					parse();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractHtmlStockDataSource#getParserCallback
	 * ()
	 */
	@Override
	protected ParserCallback getParserCallback()
	{
		return new HTMLEditorKit.ParserCallback()
		{
			private boolean relevant = false;

			private String text = "";

			private final List<String> rowData = new ArrayList<String>();

			/*
			 * (non-Javadoc)
			 * @see
			 * javax.swing.text.html.HTMLEditorKit.ParserCallback#handleStartTag
			 * (javax.swing.text.html.HTML.Tag,
			 * javax.swing.text.MutableAttributeSet, int)
			 */
			@Override
			public void handleStartTag(Tag t, MutableAttributeSet a, int pos)
			{
				if (t == Tag.TR)
				{
					relevant = true;
					rowData.clear();
				}
				else if (t == Tag.TD)
				{
					text = "";
				}

			}

			/*
			 * (non-Javadoc)
			 * @see
			 * javax.swing.text.html.HTMLEditorKit.ParserCallback#handleEndTag
			 * (javax.swing.text.html.HTML.Tag, int)
			 */
			@Override
			public void handleEndTag(Tag t, int pos)
			{
				if (t == Tag.TR)
				{
					relevant = false;

					// nbsp
					if (rowData.size() == 10 && rowData.get(0).charAt(0) != 160)
					{
						String price = rowData.get(1).replace((char) 160, ' ').replaceAll(" |\\.", "");
						String volume = rowData.get(2).replace((char) 160, ' ').replaceAll(" |\\.", "");

						if (!StringUtils.equals(price, "N/A"))
						{
							StockData<Integer> stockData = new StockData<Integer>(new Date(), Integer.parseInt(price), Long.parseLong(volume));

							EbrokerHtmlStockDataSource.this.publishNewStockData(rowData.get(0), stockData);

							// System.out.println("New data for [" +
							// rowData.get(0) + "]: " + stockData);
						}

						instruments.add(rowData.get(0));
					}
				}
				else if (t == Tag.TD)
				{
					rowData.add(text);
				}
			}

			/*
			 * (non-Javadoc)
			 * @see
			 * javax.swing.text.html.HTMLEditorKit.ParserCallback#handleText
			 * (char[], int)
			 */
			@Override
			public void handleText(char[] data, int pos)
			{
				if (relevant)
					text += new String(data);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.AbstractHtmlStockDataSource#getUrl()
	 */
	@Override
	protected URL getUrl() throws MalformedURLException
	{
		return new URL(url);
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getName()
	 */
	@Override
	public String getName()
	{
		return "www.ebroker.hu";
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getAvailableInstruments()
	 */
	@Override
	public String[] getAvailableInstruments()
	{
		return instruments.toArray(new String[instruments.size()]);
	}

	public static void main(String[] a)
	{
		try
		{
			new EbrokerHtmlStockDataSource().parse();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
