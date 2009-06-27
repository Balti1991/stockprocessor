/**
 * 
 */
package stockprocessor.data;

import java.util.Date;

/**
 * @author anti
 */
public class CandleStockData extends StockData<Candle>
{
	public CandleStockData(final int open, final int close, final int min, final int max, final Date time, final long periode, long volume)
	{
		this(new Candle(open, close, min, max), time, periode, volume);
	}

	public CandleStockData(final Candle candle, final Date time, final long periode, long volume)
	{
		super(time, candle, volume);
	}
}
