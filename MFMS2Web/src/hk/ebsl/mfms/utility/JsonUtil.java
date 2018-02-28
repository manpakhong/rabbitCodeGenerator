package hk.ebsl.mfms.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	
	 public static <T> String listToJsonString(List<T> json) {
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

	    public static <T> List<T> jsonStringToList(String jsonString, Class<T> clazz) {

	        ObjectMapper mapper = new ObjectMapper();
	        List<T> json = new ArrayList<T>();

	        if (!jsonString.equals(""))
	            try {
	                json = mapper.readValue(jsonString, mapper.getTypeFactory()
	                        .constructCollectionType(List.class, clazz));
	            } catch (JsonParseException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (JsonMappingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }

	        return json;
	    }

	    public static <T> String objectToJsonString(Object json) {

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

	    public static <T> Object jsonStringToObject(String jsonString, Class<T> clazz) {

	        ObjectMapper mapper = new ObjectMapper();
	        Object json = new Object();

	        if (!jsonString.equals("")) {
	            try {
	                json = mapper.readValue(jsonString, mapper.getTypeFactory()
	                        .constructType(clazz));
	            } catch (JsonParseException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (JsonMappingException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	        return json;
	    }
	
}
