/**
 * 
 */
package stockprocessor.data;

import java.util.Date;

/**
 * @author anti
 */
public class CandleShareData extends ShareData<Candle>
{
	public CandleShareData(String name, final int open, final int close, final int min, final int max, final long periode, long volume,
			final Date time)
	{
		this(name, new Candle(open, close, min, max), periode, volume, time);
	}

	public CandleShareData(String name, final Candle candle, final long periode, long volume, final Date time)
	{
		super(name, candle, volume, time);
	}
}
