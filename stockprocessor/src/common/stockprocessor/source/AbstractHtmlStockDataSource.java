/**
 * 
 */
package stockprocessor.source;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.ChangedCharSetException;
import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import stockprocessor.data.StockData;

/**
 * @author anti
 */
public abstract class AbstractHtmlStockDataSource<SD extends StockData<?>> extends AbstractStockDataSource<SD>
{
	public void parse() throws IOException
	{
		Reader reader = null;
		InputStream in = null;
		InputStreamReader isr = null;

		try
		{
			// Open a connection on the URL we want to render first.
			// URLConnection connection = getUrl().openConnection();
			// in = connection.getInputStream();
			in = getUrl().openStream();
			isr = new InputStreamReader(in);
			reader = new BufferedReader(isr);

			// prepare
			Parser parser = new ParserDelegator();
			ParserCallback callback = getParserCallback();

			// Load it (true means to ignore character set)
			try
			{
				parser.parse(reader, callback, false);
			}
			catch (ChangedCharSetException e)
			{
				final String spec = e.getCharSetSpec();

				final Pattern p = Pattern.compile("charset=\"?(.+)\"?.*;?", Pattern.CASE_INSENSITIVE);
				final Matcher m = p.matcher(spec);

				final String charset = m.find() ? m.group(1) : "ISO-8859-1"; // NOTRANS

				isr = new InputStreamReader(in, charset);
				reader = new BufferedReader(isr);

				// System.out.println("Charset changed to : [" + charset + "]");
				parser.parse(reader, callback, false);
			}
		}
		finally
		{
			// close everything
			if (isr != null)
			{
				try
				{
					isr.close();
				}
				catch (IOException ignoredException)
				{
					// NOP
				}
			}

			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException ignoredException)
				{
					// NOP
				}
			}

			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException ignoredException)
				{
					// NOP
				}
			}
		}
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	protected abstract URL getUrl() throws MalformedURLException;

	/**
	 * @return
	 */
	protected abstract ParserCallback getParserCallback();
}
