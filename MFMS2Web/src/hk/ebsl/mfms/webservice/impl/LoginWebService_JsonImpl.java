package hk.ebsl.mfms.webservice.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.LoginWebService_Json;

public class LoginWebService_JsonImpl extends WebServiceUtil implements LoginWebService_Json {

	@Context
	private HttpHeaders headers;

	
	@Override
	public Response checkLogin() {
		// TODO Auto-generated method stub
		
		
		
		return null;
	}

}
