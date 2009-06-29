/**
 * 
 */
package stockprocessor.handler.source;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import stockprocessor.data.Candle;
import stockprocessor.data.CandleShareData;
import stockprocessor.data.information.DefaultParameterInformation;
import stockprocessor.data.information.ParameterInformation;
import stockprocessor.data.information.ParameterInformation.ParameterType;
import stockprocessor.handler.receiver.DataReceiver;

/**
 * http://portfolio.hu/reszveny/adatletoltes.tdp?typ=txt&rv=MTELEKOM
 * 
 * @author anti
 */
public class PortfolioStockDataSource extends AbstractCsvStockDataSource<CandleShareData>
{
	protected String[] availableInstruments = new String[]
		{"AAA", "ANY", "BIF", "BNPAGRI0111", "BOOK", "CSEPEL", "DANUBIUS", "ECONET", "EGIS", "EHEP", "ELMU", "EMASZ", "ETFBUXOTP", "EXTERNET",
				"FEVITAN", "FHB", "FORRAS/OE", "FORRAS/T", "FOTEX", "FREESOFT", "GENESIS", "GSPARK", "HUMET", "KARPOT", "KONZUM", "KPACK", "LINAMAR",
				"MOL", "MTELEKOM", "ORC", "OTP", "PANNERGY", "PANNUNION", "PFLAX", "PHYLAXIA", "PVALTO", "QUAESTOR", "RABA", "RFV", "RICHTER",
				"SYNERGON", "TVK", "TVNETWORK", "ZWACK",//
				"BUX0906", "BUX0907", "BUX0908", "BUX0909", "BUX0912", "BUX1003", "BUX1006", "BUX1012"};

	private static final String CACHE_PATH = "./cache/Porfolio";

	protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	protected final long periode = 60 * 60 * 24;

	private String instrument = null;

	private int counter;

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractCsvStockDataSource#extractInstument
	 * (java.lang.String[])
	 */
	@Override
	protected String extractInstument(String[] nextLine)
	{
		return instrument;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractCsvStockDataSource#processLine(java
	 * .lang.String[])
	 */
	@Override
	protected CandleShareData processLine(String[] nextLine)
	{
		CandleShareData stockData = null;

		if (counter == 0)
		{
			String line = nextLine[0];

			// Részvény: BUX1012 (2008-12-15 - 2009-05-06)
			// Részvény: MTELEKOM
			instrument = line.substring(10);

			counter++;
		}
		if (counter > 3 && nextLine.length == 6)
		{
			// Dátum Nyitó Záró Minimum Maximum Forgalom (db)
			try
			{
				Date date = simpleDateFormat.parse(nextLine[0]);

				int open = (int) Double.parseDouble(nextLine[1]);
				int close = (int) Double.parseDouble(nextLine[4]);
				int min = (int) Double.parseDouble(nextLine[3]);
				int max = (int) Double.parseDouble(nextLine[2]);

				Candle candle = new Candle(open, close, min, max);

				long volume = Long.parseLong(nextLine[5]);

				stockData = new CandleShareData(instrument, candle, periode, volume, date);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			counter++;
		}

		return stockData;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.StockDataSource#getName()
	 */
	@Override
	public String getName()
	{
		return "PortfolioStockData";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractCsvStockDataSource#getFileName(java
	 * .lang.String)
	 */
	@Override
	protected Reader getReader(String instrument)
	{
		// check cache directory
		File cacheDirectory = new File(CACHE_PATH);
		if (!cacheDirectory.exists())
			cacheDirectory.mkdirs();

		// check is it a directory
		if (!cacheDirectory.isDirectory())
		{
			System.out.println("Can't access cach, useing dirrect access");
			return getUrlReader(instrument);
		}

		// MTELEKOM-history.txt
		// get file
		File file = new File(CACHE_PATH + "/" + instrument + "-history.txt");

		try
		{
			// check file
			if (!file.exists())
			{ // download it...
				downloadInstrument(instrument);
			}

			Date lastModified = new Date(file.lastModified());
			if (!DateUtils.isSameDay(new Date(), lastModified))
				downloadInstrument(instrument);

			// return reader to file
			return getFileRead(file);
		}
		catch (IOException e)
		{
			System.out.println("Can't access cach, useing dirrect access");
			return getUrlReader(instrument);
		}
	}

	/**
	 * @param instrument
	 * @return
	 * @throws IOException
	 */
	protected boolean downloadInstrument(String instrument) throws IOException
	{
		URL url;
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;

		try
		{
			File file = new File(CACHE_PATH + "/" + instrument + "-history.txt");
			url = new URL("http://portfolio.hu/reszveny/adatletoltes.tdp?typ=txt&rv=" + instrument);

			inputStream = new BufferedInputStream(url.openStream());
			outputStream = new BufferedOutputStream(new FileOutputStream(file));

			// download
			int i;
			while ((i = inputStream.read()) != -1)
			{
				outputStream.write(i);
			}

			return true;
		}
		catch (Exception e)
		{
			System.err.println("downloadInstrument: " + e.getMessage());
			return false;
		}
		finally
		{
			if (inputStream != null)
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					// NOP
				}

			if (outputStream != null)
				try
				{
					outputStream.close();
				}
				catch (IOException e)
				{
					// NOP
				}
		}
	}

	protected Reader getUrlReader(String instrument)
	{
		// http://portfolio.hu/reszveny/adatletoltes.tdp?typ=txt&rv=MTELEKOM

		InputStream in;
		URL url;

		try
		{
			url = new URL("http://portfolio.hu/reszveny/adatletoltes.tdp?typ=txt&rv=" + instrument);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		try
		{
			in = url.openStream();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		InputStreamReader isr = new InputStreamReader(in);
		return isr;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.bogar.anti.stock.source.AbstractCsvStockDataSource#getSeparator()
	 */
	@Override
	protected char getSeparator()
	{
		return '\t';
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hu.bogar.anti.stock.source.AbstractCsvStockDataSource#readFile(hu.bogar
	 * .anti.stock.processor.StockDataProcessor, java.io.Reader, char)
	 */
	@Override
	protected void readFile(DataReceiver<CandleShareData> dataReceiver, String input, Reader reader, char separator) throws IOException
	{
		counter = 0;

		super.readFile(dataReceiver, input, reader, separator);
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.source.AbstractDataSource#createOutputParameters()
	 */
	@Override
	protected List<ParameterInformation> createOutputParameters()
	{
		List<ParameterInformation> list = new ArrayList<ParameterInformation>();

		for (String instrument : availableInstruments)
		{
			ParameterInformation parameterInformation = new DefaultParameterInformation(instrument, ParameterType.STOCK_DATA);
			list.add(parameterInformation);
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.data.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
