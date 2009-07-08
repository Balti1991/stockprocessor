package stockprocessor.handler.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import stockprocessor.data.ShareData;
import stockprocessor.handler.receiver.DataReceiver;
import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 */

/**
 * @author anti
 */
public abstract class AbstractCsvStockDataSource<SD extends ShareData<?>> extends AbstractDataSource<SD>
{
	private static final Log log = LogFactory.getLog(AbstractCsvStockDataSource.class);

	protected Reader getFileRread(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		InputStream inputStream = new FileInputStream(file);

		return new InputStreamReader(inputStream);
	}

	protected Reader getFileRead(File file) throws FileNotFoundException
	{
		return new FileReader(file);
	}

	protected abstract String extractInstument(String[] nextLine);

	protected abstract SD processLine(String[] nextLine);

	/**
	 * @throws IOException
	 */
	protected void readFile(DataReceiver<SD> dataReceiver, String input, Reader reader, char separator) throws IOException
	{
		// prepare
		Reader bufferedReader = new BufferedReader(reader);
		CSVReader csvReader = new CSVReader(bufferedReader, separator);

		// read file
		String[] nextLine;

		try
		{
			// read
			while ((nextLine = csvReader.readNext()) != null)
			{
				String instrument = extractInstument(nextLine);
				SD stockData = processLine(nextLine);

				// skip empty & fake data
				if (stockData != null && stockData.getVolume() > 0)
				{
					try
					{
						dataReceiver.newDataArrivedNotification(input, stockData);
					}
					catch (RuntimeException e)
					{
						log.warn("error notifying processor [" + dataReceiver + "] with data [" + stockData + "]", e);
					}
				}
			}
		}
		finally
		{

			// close
			try
			{
				if (csvReader != null)
					csvReader.close();
			}
			catch (IOException e)
			{
				// NOP
			}

			try
			{
				if (bufferedReader != null)
					bufferedReader.close();
			}
			catch (IOException e)
			{
				// NOP
			}

			try
			{
				if (reader != null)
					reader.close();
			}
			catch (IOException e)
			{
				// NOP
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getName()
	 */
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractStockDataSource#registerDataProcessor
	 * (java.lang.String, hu.bogar.anti.stock.processor.StockDataProcessor)
	 */
	@Override
	public void registerDataReceiver(String instrument, DataReceiver<SD> dataReceiver, String input)
	{
		Reader reader = null;
		try
		{
			// read the csv file only once per processor
			reader = getReader(instrument);
			readFile(dataReceiver, input, reader, getSeparator());
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					// NOP
				}
		}
	}

	/*
	 * @param instrument
	 * @return
	 */
	protected abstract Reader getReader(String instrument);

	/**
	 * @return the separator character
	 */
	protected char getSeparator()
	{
		return ';';
	}
}
