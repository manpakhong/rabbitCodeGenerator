package hk.ebsl.mfms.exception;

/**
 * 
 * Holds custom exception message
 *
 */
public class MFMSException extends Throwable {

	public MFMSException() {
		super();
	}
	
	public MFMSException(Throwable e) {
		super(e);
	}
	
	public MFMSException(String message) {
		super(message);
	}
	
	public MFMSException(String message, Throwable e) {
		super(message, e);
	}
}
