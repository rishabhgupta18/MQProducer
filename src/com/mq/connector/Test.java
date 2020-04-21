package com.mq.connector;

import com.mq.producer.Producer;
import com.mq.producer.ProducerConfig;
import com.mq.producer.ProducerRecord;
import com.mq.producer.Topic;
import com.mq.serialize.ISerializtion;
import com.mq.serialize.Serialize;

public class Test {

	public static void main(String[] args) throws Exception {
	
		String ipAddress = "";//enter ip provided by MQServer
		int port = 8080; // enter port provided by MQServer
		ProducerConfig pc = new ProducerConfig(ipAddress, port);
		ISerializtion s = new Serialize();
		pc.setSerialization(s);
		Producer p = new Producer(pc);
		ProducerRecord r = new ProducerRecord(new Topic("Topic " + 0));
		r.setData(s.serialize(r));
		p.send(r);

	}
}
