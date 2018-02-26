package hk.ebsl.mfms.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Pagination<T> {

//	@Autowired
//	private Properties propertyConfigurer;
	
//	@Autowired
//	private Properties privilegeMap;

	private List<T> list;
	private int rowsOnEachPage;
	private int listSize;
	private int pageSize;
	private int currentPage;

	Pagination(){
		
	}

	public void test(){
		System.out.println("rowsOnEachPage : "+rowsOnEachPage);
		System.out.println("pageSize : "+pageSize);
		System.out.println("listSize : "+listSize);
	}
	
	public void setList(List<T> list){
		this.list = list;
		this.listSize = list.size();
		this.currentPage = 1;
	}
	
	public void setList(List<T> list, Integer targetPage){
		this.list = list;
		this.listSize = list.size();
		if (targetPage > 0) {
			this.currentPage = targetPage;
		} else {
			this.currentPage = 1;
		}
	}
	
	
	public int getCurrentPage() {

		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getStartPage() {
		int startPage = 1;

		startPage = (int) ((Math.floor(currentPage / pageSize)) * pageSize + 1);
		if (currentPage % pageSize == 0) {
			startPage -= pageSize;
		}

		return startPage;
	}

	public double  getTotalPage() {

		return Math.ceil((double) list.size() / (double) rowsOnEachPage);
	}

	public int getRowsOnEachPage() {
		return rowsOnEachPage;
	}

	public List<T> getPageTableData() {

		List<T> returnList = new ArrayList<T>();

		for (int i = rowsOnEachPage * (currentPage - 1); i < rowsOnEachPage
				* currentPage; i++) {
			if (i < list.size())
				returnList.add(list.get(i));
		}

		return returnList;
	}

	public String getResultJson() {
		return listToJsonString(list);
	}

	private <T> String listToJsonString(List<T> json) {
		ObjectMapper mapper = new ObjectMapper();

		String rtn = "";

		try {
			rtn = mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtn;
	}

	public void setRowsOnEachPage(int rowsOnEachPage) {
		this.rowsOnEachPage = rowsOnEachPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
	

	
	
	
}
