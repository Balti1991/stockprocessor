/**
 * 
 */
package stockprocessor.cli;

import stockprocessor.handler.StockAction;

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

	public StockAction t3(double[] inArray)
	{
		return movingAverage(MAType.T3, inArray);
	}

	public StockAction sma(double[] inArray)
	{
		return movingAverage(MAType.Sma, inArray);
	}

	public StockAction ema(double[] inArray)
	{
		return movingAverage(MAType.Ema, inArray);
	}

	protected StockAction movingAverage(MAType maType, double[] inArray)
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

		return StockBrokerHelper.instance.evaluateMovingAverage(result);
	}

	protected StockAction trix(double[] inArray, int optInTimePeriod)
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;

		MInteger outNbElement = new MInteger();
		outNbElement.value = inArray.length - 0;

		double[] outReal = new double[inArray.length - 0];

		// startIdx, endIdx, inReal, optInTimePeriod,
		// outBegIdx, outNBElement, outReal
		core.trix(0, inArray.length - 1,//
				inArray, //
				optInTimePeriod, //
				outBegIdx, outNbElement, outReal);

		if (outNbElement.value == 0)
			throw new RuntimeException("No result for TRIX");

		// process result
		double result = outReal[outNbElement.value - 1];

		return StockBrokerHelper.instance.evaluateTrix(result);
	}

	protected StockAction rsi(double[] inArray, int optInTimePeriod)
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;

		MInteger outNbElement = new MInteger();
		outNbElement.value = inArray.length - 0;

		double[] outReal = new double[inArray.length - 0];

		// startIdx, endIdx, inReal, optInTimePeriod,
		// outBegIdx, outNBElement, outReal
		core.rsi(0, inArray.length - 1,//
				inArray, //
				optInTimePeriod, //
				outBegIdx, outNbElement, outReal);

		if (outNbElement.value == 0)
			throw new RuntimeException("No result for TRIX");

		// process result
		double result = outReal[outNbElement.value - 1];

		return StockBrokerHelper.instance.evaluatRsi(result);
	}

	protected StockAction stoch(double[] inArray, double[] inHigh, double[] inLow, double[] inClose, //
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

		return StockBrokerHelper.instance.evaluateStochastic(resultK, resultD);
	}
}
