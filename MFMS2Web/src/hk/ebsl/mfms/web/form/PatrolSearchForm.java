package hk.ebsl.mfms.web.form;

import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.model.Patrol_SearchTableRoute;

public class PatrolSearchForm {

	String routeCode;
	String routeName;
	String selectedLocationJson;
	String searchResultJson;

	private List<Patrol_SearchTableRoute> resultList = new ArrayList<Patrol_SearchTableRoute>();

	private Boolean canModify = false;

	private Boolean canRemove = false;

	private Boolean canGen = false;

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

	public String getSelectedLocationJson() {
		return selectedLocationJson;
	}

	public void setSelectedLocationJson(String selectedLocationJson) {
		this.selectedLocationJson = selectedLocationJson;
	}

	public String getSearchResultJson() {
		return searchResultJson;
	}

	public void setSearchResultJson(String searchResultJson) {
		this.searchResultJson = searchResultJson;
	}

	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}

	public Boolean getCanRemove() {
		return canRemove;
	}

	public void setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}

	public List<Patrol_SearchTableRoute> getResultList() {
		return resultList;
	}

	public void setResultList(List<Patrol_SearchTableRoute> resultList) {
		this.resultList = resultList;
	}

}
