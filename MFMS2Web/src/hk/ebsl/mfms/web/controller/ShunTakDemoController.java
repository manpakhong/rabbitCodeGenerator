package hk.ebsl.mfms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.LocationManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ShunTakDemoController {

	@Autowired
	private LocationManager locationManager;

	@RequestMapping(value = "/STDemo.do")
	public String serveSTDemoPage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "stDemo/stdemo";
	}

	@RequestMapping(value = "ImportLocation.do")
	public @ResponseBody String importLocation(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		int level1Key = 0;
		int level2Key = 0;
		int level3Key = 0;
		int level4Key = 0;

		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader("C:\\Users\\Ricky\\Desktop\\Shun Tak_Trial_configuration 20171110 (1).csv"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();

				System.out.println(line);
				if(line == null)
					break;
				
				String[] parts = line.split(",");

				
				
				// level 1
				if (!parts[1].equals("")) {
					level1Key = locationManager.saveLocation(getLocation(1, 0, parts[1], parts[2]));
					Location loc = locationManager.getLocationByKey(level1Key);
					loc.setChain(level1Key + "");
					locationManager.saveLocation(loc);
				}

				// level 2
				if (!parts[3].equals("")) {
					level2Key = locationManager.saveLocation(getLocation(2, level1Key, parts[3], parts[4]));
					Location loc = locationManager.getLocationByKey(level2Key);
					loc.setChain(level1Key + "," + level2Key);
					locationManager.saveLocation(loc);
				}

				// level 3
				if (!parts[5].equals("")) {
					level3Key = locationManager.saveLocation(getLocation(3, level2Key, parts[5], parts[6]));
					Location loc = locationManager.getLocationByKey(level3Key);
					loc.setChain(level1Key + "," + level2Key + "," + level3Key);
					locationManager.saveLocation(loc);
				}

				if (parts.length > 7) {

					// level 4
					if (!parts[7].equals("")) {
						level4Key = locationManager.saveLocation(getLocation(4, level3Key, parts[7], parts[8]));
						Location loc = locationManager.getLocationByKey(level4Key);
						loc.setChain(level1Key + "," + level2Key + "," + level3Key + "," + level4Key);
						locationManager.saveLocation(loc);
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "Done";
	}

	private Location getLocation(int level, int parentKey, String code, String name) {

		int siteKey = 3;
		int createBy = 2;

		Calendar cal = Calendar.getInstance();

		Location loc = new Location();
		loc.setSiteKey(siteKey);
		loc.setCreatedBy(createBy);
		loc.setCreateDateTime(new Timestamp(cal.getTimeInMillis()));
		loc.setLastModifyBy(createBy);
		loc.setLastModifyDateTime(new Timestamp(cal.getTimeInMillis()));

		loc.setLevelKey(level);
		loc.setCode(code.trim());
		loc.setName(name.trim());
		loc.setDesc("");
		loc.setChain("");
		loc.setParentKey(parentKey);
		loc.setDeleted("N");

		return loc;
	}
	
	@RequestMapping(value = "ExportLocationTest.do")
	public @ResponseBody String exportLocation(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		PrintWriter out= null;
		
		try {
			List<Location> locationList = locationManager.getLocationsBySiteKey(3);

			out = new PrintWriter("C:\\Users\\Ricky\\Desktop\\locations.txt");
			
			out.println("Location Level , Location Code , Location Name");
			for (Location loc : locationList) {

				out.println(loc.getLevelKey() + ", " + loc.getCode() + ", " + loc.getName());
			}

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}

		return "done";
	}
	
}