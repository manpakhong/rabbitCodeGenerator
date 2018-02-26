package hk.ebsl.mfms.notification;

import hk.ebsl.mfms.notification.object.InformEscalatorPostObj;
import hk.ebsl.mfms.notification.object.InformEscalatorReceivedObj;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface InformEscalator {

	@POST("Escalation")
	Call<InformEscalatorReceivedObj> createTask(@Body InformEscalatorPostObj post);
	
	@PUT("Escalation")
	Call<InformEscalatorReceivedObj> updateTask(@Body InformEscalatorPostObj post);
	
	@POST("TEST")
	Call<InformEscalatorReceivedObj> test();
	
}
