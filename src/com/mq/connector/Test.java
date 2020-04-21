package com.mq.connector;

import com.mq.producer.Producer;
import com.mq.producer.ProducerConfig;
import com.mq.producer.ProducerRecord;
import com.mq.producer.Topic;
import com.mq.serialize.ISerializtion;
import com.mq.serialize.Serialize;

public class Test {

	public static void main(String[] args) throws Exception {
		ProducerConfig pc = new ProducerConfig("192.168.1.18", 59570);
		ISerializtion s = new Serialize();
		pc.setSerialization(s);
		Producer p = new Producer(pc);
		ProducerRecord r = new ProducerRecord(new Topic("Topic " + 0));
		r.setData(s.serialize(r));
		p.send(r);

	}
}
