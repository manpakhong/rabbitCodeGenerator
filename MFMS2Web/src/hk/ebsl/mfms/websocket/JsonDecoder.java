package hk.ebsl.mfms.websocket;


public class JsonDecoder{
	
}

//public class JsonDecoder implements Decoder.TextStream<JsonMsg> {
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
//	}
//
//	@Override
//	public JsonMsg decode(Reader reader) throws DecodeException, IOException {
//		// TODO Auto-generated method stub
//
//		System.out.println("Json Decode");
//		
//		String jsonString = IOUtils.toString(reader);
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		JsonMsg jsonMsg = mapper.readValue(jsonString, JsonMsg.class);
//
//		return jsonMsg;
//	}
//
//}
