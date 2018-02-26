package hk.ebsl.mfms.utility;

public class Utility {

	
	public static String replaceHtmlEntities(String str) {

		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace("\"", "&quot;");
		str = str.replace("'", "&apos;");

		return str;
	}
	
	public static String removeQuote(String str) {

		str = str.replace("&quot;", "");
		str = str.replace("&apos;", "");

		return str;
	}
	
}
