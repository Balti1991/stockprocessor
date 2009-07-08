import java.lang.reflect.InvocationTargetException;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.CoreAnnotated;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.meta.CoreMetaData2;
import com.tictactec.ta.lib.meta.PriceHolder;

/**
 * 
 */

/**
 * @author anti
 */
public class TestTA
{
	static double[] nullArray =
		{//
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //
				0};

	static double[] inArray =
		{//
		1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				0};

	static int inSize = inArray.length;

	static double[] inArrayH =
		{//
		1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				0};

	static double[] inArrayL =
		{//
		1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				1, 2, 1, 1, 1, 2, 2, 1, 4, 3, //
				0};

	private static transient final Class<CoreAnnotated> coreClass = CoreAnnotated.class;

	/**
	 * @param args
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		System.out.println("inSize: " + inSize);
		TestTA testTA = new TestTA();

		// testTA.SMA1();

		testTA.stoch1();
		System.out.println();

		testTA.stoch2();
		System.out.println();

		testTA.SMA1();
		System.out.println();

		testTA.SMA2();
		System.out.println();

		PriceHolder priceHolder = new PriceHolder(inArray, inArray, inArray, inArray, inArray, inArray);
	}

	private CoreMetaData2 getCoreMetaData(String function)
	{
		// get instance
		CoreMetaData2 instance = null;
		try
		{
			instance = CoreMetaData2.getInstance(function);
			System.out.println("getFuncInfo: " + instance.getFuncInfo());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// opt params
		System.out.println("opt inputs (" + instance.getFuncInfo().nbOptInput() + "):");
		for (int i = 0; i < instance.getFuncInfo().nbOptInput(); i++)
		{
			System.out.println(instance.getOptInputParameterInfo(i));
		}

		// inputs
		System.out.println("inputs (" + instance.getFuncInfo().nbInput() + "):");
		for (int i = 0; i < instance.getFuncInfo().nbInput(); i++)
		{
			System.out.println("getInputParameterInfo: " + instance.getInputParameterInfo(i));
		}

		// outputs
		System.out.println("outputs (" + instance.getFuncInfo().nbOutput() + "):");
		for (int i = 0; i < instance.getFuncInfo().nbOutput(); i++)
		{
			System.out.println(instance.getOutputParameterInfo(i));
		}

		try
		{
			// get lookback
			int lookback = instance.getLookback();
			System.out.println("lookback: " + lookback);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return instance;
	}

	private void SMA1() throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		// get instance
		CoreMetaData2 instance = getCoreMetaData("SMA");

		// get lookback
		int lookback = instance.getLookback();

		// set input
		instance.setInputParamReal(0, inArray);

		// set output
		double[] outArray = new double[inSize - lookback];
		instance.setOutputParamReal(0, outArray);

		// prepare out parameters
		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;
		MInteger outNbElement = new MInteger();
		outNbElement.value = outArray.length;

		// call function
		instance.callFunc(lookback, inSize - 1, outBegIdx, outNbElement);

		sysOutArray(outArray, outNbElement.value);
		System.out.println("outBegIdx: " + outBegIdx.value + " outNbElement: " + outNbElement.value);
	}

	private void SMA2()
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;
		MInteger outNbElement = new MInteger();
		outNbElement.value = inSize - 0;
		double[] outReal = new double[inSize - 0];

		int optInTimePeriod = Integer.MIN_VALUE;

		core.movingAverage(0, inSize - 1, inArray, optInTimePeriod, MAType.Sma, outBegIdx, outNbElement, outReal);

		sysOutArray(outReal, outNbElement.value);
		System.out.println("outBegIdx=" + outBegIdx.value + " outNbElement" + outNbElement.value);
	}

	private void stoch1() throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		// get instance
		CoreMetaData2 instance = getCoreMetaData("STOCH");

		// set input
		instance.setInputParamPrice(0, nullArray, inArrayH, inArrayL, inArray, nullArray, nullArray);

		// set opt inputs - FIX!
		instance.setOptInputParamMAType(2, MAType.Sma);
		instance.setOptInputParamMAType(4, MAType.Sma);

		// set outputs
		double[] outSlowK = new double[inSize - 0];
		instance.setOutputParamReal(0, outSlowK);
		double[] outSlowD = new double[inSize - 0];
		instance.setOutputParamReal(1, outSlowD);

		// prepare out parameters
		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;
		MInteger outNBElement = new MInteger();
		outNBElement.value = outSlowK.length;

		// call function
		instance.callFunc(0, inSize - 1, outBegIdx, outNBElement);

		sysOutArray(outSlowK, outNBElement.value);
		System.out.println();
		sysOutArray(outSlowD, outNBElement.value);
		System.out.println("outBegIdx: " + outBegIdx.value + " outNBElement: " + outNBElement.value);
	}

	private void stoch2()
	{
		Core core = new Core();

		MInteger outBegIdx = new MInteger();
		outBegIdx.value = 0;

		MInteger outNBElement = new MInteger();
		outNBElement.value = inSize - 0;

		int optInFastK_Period = Integer.MIN_VALUE, optInSlowK_Period = Integer.MIN_VALUE, optInSlowD_Period = Integer.MIN_VALUE;

		double[] outSlowK = new double[inSize - 0], outSlowD = new double[inSize - 0];

		core.stoch(0, inSize - 1, //
				inArrayH, inArrayL, inArray, //
				optInFastK_Period, optInSlowK_Period, MAType.Sma, optInSlowD_Period, MAType.Sma, //
				outBegIdx, outNBElement, outSlowK, outSlowD);

		sysOutArray(outSlowK, outNBElement.value);
		System.out.println();
		sysOutArray(outSlowD, outNBElement.value);
		System.out.println("outBegIdx=" + outBegIdx.value + " outNbElement" + outNBElement.value);
	}

	/**
	 * @param outArray
	 */
	private static void sysOutArray(double[] outArray, int count)
	{
		for (int i = 0; i < count; i++)
		{
			System.out.println(i + " [" + outArray[i] + "]");
		}
	}
}
