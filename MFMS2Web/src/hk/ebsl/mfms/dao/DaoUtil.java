package hk.ebsl.mfms.dao;

public class DaoUtil {

	public static String escape(String str) {
		return str.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
		
	}
}
