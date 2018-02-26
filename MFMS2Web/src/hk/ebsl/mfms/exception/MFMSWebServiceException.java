package hk.ebsl.mfms.exception;

@SuppressWarnings("serial")
public class MFMSWebServiceException extends Exception {

	public MFMSWebServiceException(){
		super();
	}
	
	public MFMSWebServiceException(String msg){
		super(msg);
	}
	
}
