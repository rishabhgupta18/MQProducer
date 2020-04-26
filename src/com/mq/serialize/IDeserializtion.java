package com.mq.serialize;

public interface IDeserializtion {
	public Object deserialize(String data, Class<? extends Object> c) throws Exception;
}
