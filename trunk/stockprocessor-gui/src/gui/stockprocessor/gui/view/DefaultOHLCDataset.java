/**
 * 
 */
package stockprocessor.gui.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.util.PublicCloneable;

/**
 * A simple implementation of the {@link OHLCDataset} interface. This
 * implementation supports only one series.
 */
public class DefaultOHLCDataset extends AbstractXYDataset implements OHLCDataset, PublicCloneable
{

	/** The series key. */
	private final Comparable key;

	/** Storage for the data items. */
	private ArrayList<OHLCDataItem> data;

	/**
	 * Creates a new dataset.
	 * 
	 * @param key the series key.
	 */
	public DefaultOHLCDataset(Comparable key)
	{
		this.key = key;
	}

	/**
	 * Returns the series key.
	 * 
	 * @param series the series index (ignored).
	 * @return The series key.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesKey(int)
	 */
	@Override
	public Comparable getSeriesKey(int series)
	{
		return this.key;
	}

	/**
	 * Returns the x-value for a data item.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The x-value.
	 */
	public Number getX(int series, int item)
	{
		return new Long(this.data.get(item).getDate().getTime());
	}

	/**
	 * Returns the x-value for a data item as a date.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The x-value as a date.
	 */
	public Date getXDate(int series, int item)
	{
		return this.data.get(item).getDate();
	}

	/**
	 * Returns the y-value.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The y value.
	 */
	public Number getY(int series, int item)
	{
		return getClose(series, item);
	}

	/**
	 * Returns the high value.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The high value.
	 */
	public Number getHigh(int series, int item)
	{
		return this.data.get(item).getHigh();
	}

	/**
	 * Returns the high-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series the series (zero-based index).
	 * @param item the item (zero-based index).
	 * @return The high-value.
	 */
	public double getHighValue(int series, int item)
	{
		double result = Double.NaN;
		Number high = getHigh(series, item);
		if (high != null)
		{
			result = high.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the low value.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The low value.
	 */
	public Number getLow(int series, int item)
	{
		return this.data.get(item).getLow();
	}

	/**
	 * Returns the low-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series the series (zero-based index).
	 * @param item the item (zero-based index).
	 * @return The low-value.
	 */
	public double getLowValue(int series, int item)
	{
		double result = Double.NaN;
		Number low = getLow(series, item);
		if (low != null)
		{
			result = low.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the open value.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The open value.
	 */
	public Number getOpen(int series, int item)
	{
		return this.data.get(item).getOpen();
	}

	/**
	 * Returns the open-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series the series (zero-based index).
	 * @param item the item (zero-based index).
	 * @return The open-value.
	 */
	public double getOpenValue(int series, int item)
	{
		double result = Double.NaN;
		Number open = getOpen(series, item);
		if (open != null)
		{
			result = open.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the close value.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The close value.
	 */
	public Number getClose(int series, int item)
	{
		return this.data.get(item).getClose();
	}

	/**
	 * Returns the close-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series the series (zero-based index).
	 * @param item the item (zero-based index).
	 * @return The close-value.
	 */
	public double getCloseValue(int series, int item)
	{
		double result = Double.NaN;
		Number close = getClose(series, item);
		if (close != null)
		{
			result = close.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the trading volume.
	 * 
	 * @param series the series index (ignored).
	 * @param item the item index (zero-based).
	 * @return The trading volume.
	 */
	public Number getVolume(int series, int item)
	{
		return this.data.get(item).getVolume();
	}

	/**
	 * Returns the volume-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series the series (zero-based index).
	 * @param item the item (zero-based index).
	 * @return The volume-value.
	 */
	public double getVolumeValue(int series, int item)
	{
		double result = Double.NaN;
		Number volume = getVolume(series, item);
		if (volume != null)
		{
			result = volume.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the series count.
	 * 
	 * @return 1.
	 */
	@Override
	public int getSeriesCount()
	{
		return 1;
	}

	/**
	 * Returns the item count for the specified series.
	 * 
	 * @param series the series index (ignored).
	 * @return The item count.
	 */
	public int getItemCount(int series)
	{
		return this.data.size();
	}

	/**
	 * Sorts the data into ascending order by date.
	 */
	public void sortDataByDate()
	{
		Collections.sort(this.data);
	}

	/**
	 * Tests this instance for equality with an arbitrary object.
	 * 
	 * @param obj the object (<code>null</code> permitted).
	 * @return A boolean.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof DefaultOHLCDataset))
		{
			return false;
		}
		DefaultOHLCDataset that = (DefaultOHLCDataset) obj;
		if (!this.key.equals(that.key))
		{
			return false;
		}
		if (!Arrays.equals(this.data.toArray(), that.data.toArray()))
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns an independent copy of this dataset.
	 * 
	 * @return A clone.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		DefaultOHLCDataset clone = (DefaultOHLCDataset) super.clone();
		// clone.data = new OHLCDataItem[this.data.length];
		// System.arraycopy(this.data, 0, clone.data, 0, this.data.length);
		return clone;
	}

}
