package hk.ebsl.mfms.websocket;


public class JsonEncoder{
	
}

//public class JsonEncoder implements Encoder.TextStream<JsonMsg> {
//
//	@Override
//	public void destroy() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void init(EndpointConfig arg0) {
//		// TODO Auto-generated method stub
//	
//		
//	}
//
//	@Override
//	public void encode(JsonMsg jsonObj, Writer writer) throws EncodeException,
//			IOException {
//		// TODO Auto-generated method stub
//	
//		
//		System.out.println("Json Encode");
//		ObjectMapper mapper = new ObjectMapper();
//		String rtn = mapper.writeValueAsString(jsonObj);
//		writer.write(rtn);
//	
//	}
//
//}