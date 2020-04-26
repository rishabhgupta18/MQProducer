package com.mq.serialize;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Deserialize implements IDeserializtion {

	public Object deserialize(String data, Class<? extends Object> c) throws IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(data, c);

	}

}
