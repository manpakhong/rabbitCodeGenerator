package hk.ebsl.mfms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.ui.ModelMap;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationExport;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.LocationManager;


@Controller
public class ImportExcel {
	

	@Autowired
	private LocationManager locationManager;
	
	@RequestMapping(value = "/ImportExcel.do")
	public String ImportExcelData(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		System.out.println("******************************ImportExcel******************************");
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		
		List<LocationExport> locationList = readExcel();
		List<Location> saveResult = new ArrayList<Location>();
		for(LocationExport il : locationList){
			System.out.println(il);
			Location location = new Location();
			location.setSiteKey(2);
			location.setCode(il.getCode());
			location.setName(il.getName());
			location.setDesc("");
			location.setTagId(il.getTagID());
			if(location.getCode().contains("GSLCPH1")){
				location.setParentKey(2);
				location.setChain("1,2");
			} else if(location.getCode().contains("GSLCPH2")){
				location.setParentKey(3);
				location.setChain("1,3");
			}
			
			location.setLevelKey(3);
			location.setCreatedBy(2);
			location.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
			location.setDeleted("N");
			saveResult.add(location);
		}
		
		List<Location> result = locationManager.saveListOfLocation(saveResult);
		
		for(Location l : result){
			l.setChain(l.getChain() + "," + l.getKey());
		}
		
		locationManager.saveListOfLocation(result);
		
		
		
		
		return "main/home";
	}
	
	
	private List<LocationExport> readExcel(){
		List<String[]> list = new ArrayList<String[]>();
		List<LocationExport> locationList = new ArrayList<LocationExport>();
		
		try{
			Workbook book = WorkbookFactory.create(new FileInputStream("C:\\Users\\User1\\Desktop\\ak.xlsx"));
			
			Sheet sheet = book.getSheetAt(0);
			
			String str = "";
			int count = 0;
			for (Row row : sheet) {
//				System.out.println(row.getRowNum());
				for (Cell cell : row) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							str += cell.getRichStringCellValue().getString() + ",";
							break;
						case Cell.CELL_TYPE_NUMERIC:
							str += cell.getNumericCellValue() + ",";
							break;
						}
					
				}
				list.add(str.split(","));
				str="";
			}
			
			for(String[] as : list){
				LocationExport location = new LocationExport();
				System.out.println("Row"+count++ + as[4] + "-" + as[5] + "-" + as[6]);
				location.setCode(as[4]);
				location.setName(as[5]);
				location.setTagID(as[6]);
				locationList.add(location);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return locationList;
	}
}