package hk.ebsl.mfms.utility.writer;

public class RowObject 
{
	private Integer index;
	private Object object;
	private String[] input;
	private boolean isError = false;
	private int numOfSetProperty = 0;
	
	public RowObject()
	{
	}
	
	public RowObject(Integer index, Object object, String[] input, int numOfSetProperty)
	{
		this.index = index;
		this.object = object;
		this.input = input;
		this.numOfSetProperty = numOfSetProperty;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String[] getInput() {
		return input;
	}
	public void setInput(String[] input) {
		this.input = input;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public int getNumOfSetProperty() {
		return numOfSetProperty;
	}
	public void setNumOfSetProperty(int numOfSetProperty) {
		this.numOfSetProperty = numOfSetProperty;
	}
}
