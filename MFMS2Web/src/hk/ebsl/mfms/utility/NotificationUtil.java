package hk.ebsl.mfms.utility;

public class NotificationUtil {
	
	
	
	//public static String notificationBaseUrl = "http://10.8.8.211/MFMS.Escalation.API/api/";
	public static String notificationBaseUrl = "http://localhost/MFMS.Escalation.API/api/";
	
	public enum NotificationUserRole{
		
		RespsonsibleAccount("S"), NormalAccount("N");
		
		private final String text;
		
		NotificationUserRole(final String text){
			this.text = text;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return text;
		}
		
	}
	
	public enum NotificationType{
		
		System(0), GCM(1),APNS(2),EMAIL(99);
		
		private final int value;
		
		NotificationType(final int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
		
	}
	
}
