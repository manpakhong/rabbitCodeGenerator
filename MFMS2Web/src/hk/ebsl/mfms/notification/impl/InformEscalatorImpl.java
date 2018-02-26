package hk.ebsl.mfms.notification.impl;

import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import hk.ebsl.mfms.notification.InformEscalator;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj;
import hk.ebsl.mfms.notification.object.InformEscalatorReceivedObj;
import hk.ebsl.mfms.utility.NotificationUtil;

public class InformEscalatorImpl implements
		Callback<InformEscalatorReceivedObj> {

	public final static Logger logger = Logger
			.getLogger(InformEscalatorImpl.class);

	private Retrofit retrofit = null;
	private InformEscalator inform = null;
	private int retryCounter = 0;
	
	
	public InformEscalatorImpl() {

		retrofit = new Retrofit.Builder()
				.baseUrl(NotificationUtil.notificationBaseUrl)
				.addConverterFactory(JacksonConverterFactory.create()).build();
		inform = retrofit.create(InformEscalator.class);

	}

	public void create(InformEscalatorPostObj obj) {
		Call<InformEscalatorReceivedObj> call = inform.createTask(obj);
		call.enqueue(this);
	}

	public void update(InformEscalatorPostObj obj) {
		Call<InformEscalatorReceivedObj> call = inform.updateTask(obj);
		call.enqueue(this);
	}
	
	public void test(){
		Call<InformEscalatorReceivedObj> call = inform.test();
		call.enqueue(this);
	}

	@Override
	public void onFailure(Call<InformEscalatorReceivedObj> call, Throwable t) {
		// TODO Auto-generated method stub

		logger.debug("onFailure");
		
		t.printStackTrace();
		
		if(t instanceof SocketTimeoutException){
		
			if(retryCounter < 5){
				retryCounter++;
				call.clone().enqueue(this);
				
			}
		}
		

	}

	@Override
	public void onResponse(Call<InformEscalatorReceivedObj> call,
			Response<InformEscalatorReceivedObj> response) {
		// TODO Auto-generated method stub
		logger.debug("onResponse");
		
		if (response.isSuccessful()) {
			
			logger.debug("success");
			logger.debug(response.message());
		} else {
			logger.debug("error");
			logger.debug(response.errorBody());
		}

	}

}
