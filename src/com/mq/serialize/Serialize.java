package com.mq.serialize;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Serialize implements ISerializtion {

	public String serialize(Object obj) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String data = mapper.writeValueAsString(obj);
		return data;
	}


}
