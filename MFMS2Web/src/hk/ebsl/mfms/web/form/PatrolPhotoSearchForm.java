package hk.ebsl.mfms.web.form;


import java.util.Map;

public class PatrolPhotoSearchForm {
	private int siteKey;
	private int routeDefKey;
	private String routeCode;
	private String routeName;
	private Map<Integer,String> routeMap;
	
	public int getSiteKey() {
		return siteKey;
	}
	public void setSiteKey(int siteKey) {
		this.siteKey = siteKey;
	}
	public int getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public String getRouteCode() {
		return routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public Map<Integer, String> getRouteMap() {
		return routeMap;
	}
	public void setRouteMap(Map<Integer, String> routeMap) {
		this.routeMap = routeMap;
	}
	@Override
	public String toString() {
		return "PatrolPhotoSearchForm [siteKey=" + siteKey + ", routeDefKey=" + routeDefKey + ", routeCode=" + routeCode
				+ ", routeName=" + routeName + ", routeMap=" + routeMap + "]";
	}
	
}
