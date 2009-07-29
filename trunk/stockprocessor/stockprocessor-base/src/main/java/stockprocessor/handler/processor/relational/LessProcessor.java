/**
 * 
 */
package stockprocessor.handler.processor.relational;

/**
 * @author anti
 */
public class LessProcessor extends AbstractRelationalProcessor<Number>
{
	public LessProcessor()
	{
		setName("Less");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * stockprocessor.handler.processor.relational.AbstractRelationalProcessor
	 * #calculate(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Boolean calculate(Number inputDataA, Number inputDataB)
	{
		return inputDataA.doubleValue() < inputDataB.doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * @see stockprocessor.handler.DataHandler#getDescription()
	 */
	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
