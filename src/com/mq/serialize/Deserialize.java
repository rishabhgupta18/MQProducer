package com.mq.serialize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialize implements IDeserializtion{

	public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {

		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

}
