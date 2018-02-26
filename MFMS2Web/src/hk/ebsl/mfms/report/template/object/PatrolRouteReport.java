package hk.ebsl.mfms.report.template.object;

public class PatrolRouteReport {

	private Integer index;

	// Master
	private String RouteCode;

	private String RouteNameEN;

	private Integer TotalLocNum;

	// Detail
	private Integer SeqNum;
	
	private String DisplayLocationCode;

	private String DisplayLocationNameEN;

	private String Min;

	private String Max;

	// Summary
	private int TotalRouteNum;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getRouteCode() {
		return RouteCode;
	}

	public void setRouteCode(String routeCode) {
		RouteCode = routeCode;
	}

	public String getRouteNameEN() {
		return RouteNameEN;
	}

	public void setRouteNameEN(String routeNameEN) {
		RouteNameEN = routeNameEN;
	}

	public Integer getTotalLocNum() {
		return TotalLocNum;
	}

	public void setTotalLocNum(Integer totalLocNum) {
		TotalLocNum = totalLocNum;
	}

	public Integer getSeqNum() {
		return SeqNum;
	}

	public void setSeqNum(Integer seqNum) {
		SeqNum = seqNum;
	}

	public String getDisplayLocationCode() {
		return DisplayLocationCode;
	}

	public void setDisplayLocationCode(String displayLocationCode) {
		DisplayLocationCode = displayLocationCode;
	}

	public String getDisplayLocationNameEN() {
		return DisplayLocationNameEN;
	}

	public void setDisplayLocationNameEN(String displayLocationNameEN) {
		DisplayLocationNameEN = displayLocationNameEN;
	}

	public String getMin() {
		return Min;
	}

	public void setMin(String min) {
		Min = min;
	}

	public String getMax() {
		return Max;
	}

	public void setMax(String max) {
		Max = max;
	}

	public int getTotalRouteNum() {
		return TotalRouteNum;
	}

	public void setTotalRouteNum(int totalRouteNum) {
		TotalRouteNum = totalRouteNum;
	}

}
