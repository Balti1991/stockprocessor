/**
 * 
 */
package stockprocessor.cli;

import stockprocessor.processor.Action;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;

/**
 * @author anti
 */
public class CliHelper
{
	public static CliHelper instance = new CliHelper();

	protected CliHelper()
	{
	}

	public Action sma(double[] inArray)
	{
		return movingAverage(MAType.Sma, inArray);
	}

	public Action ema(double[] inArray)
	{
		return movingAverage(MAType.Ema, inArray);
	}

	protected Action movingAverage(MAType maType, double[] inArray)
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;

		MInteger outNbElement = new MInteger();
		outNbElement.value = inArray.length - 0;

		double[] outReal = new double[inArray.length - 0];

		core.movingAverage(0, inArray.length - 1, //
				inArray, //
				Integer.MIN_VALUE, maType, //
				outBegIdx, outNbElement, outReal);

		if (outNbElement.value == 0)
			throw new RuntimeException("No result for MA");

		// process result
		double result = outReal[outNbElement.value - 1];

		return evaulateMovingAverage(result);
	}

	/**
	 * @param result
	 * @return
	 */
	protected Action evaulateMovingAverage(double result)
	{
		if (result > 0)
			return Action.BUY;
		else if (result < 0)
			return Action.SELL;
		else
			return Action.NOP;
	}

	protected Action stoch(double[] inArray, double[] inHigh, double[] inLow, double[] inClose, //
			int optInFastK_Period, int optInSlowK_Period, MAType optInSlowK_MAType, int optInSlowD_Period, MAType optInSlowD_MAType)
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;

		MInteger outNBElement = new MInteger();
		outNBElement.value = inArray.length - 0;

		int optInTimePeriod = Integer.MIN_VALUE;

		double[] outSlowK = new double[inArray.length - 0];
		double[] outSlowD = new double[inArray.length - 0];

		core.stoch(0, inArray.length - 1,//
				inHigh, inLow, inClose,//
				optInFastK_Period, //
				optInSlowK_Period, optInSlowK_MAType,//
				optInSlowD_Period, optInSlowD_MAType,//
				outBegIdx, outNBElement, outSlowK, outSlowD);

		if (outNBElement.value == 0)
			throw new RuntimeException("No result for SMA");

		// process result
		double resultK = outSlowK[outNBElement.value - 1];
		double resultD = outSlowD[outNBElement.value - 1];

		return evaulateStock(resultK, resultD);
	}

	/**
	 * @param resultK
	 * @param resultD
	 * @return
	 */
	protected Action evaulateStock(double resultK, double resultD)
	{
		if (resultK > resultD)
			return Action.BUY;
		else if (resultK < resultD)
			return Action.SELL;
		else
			return Action.NOP;
	}
}
