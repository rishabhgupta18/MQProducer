package com.mq.connector;

import java.net.ConnectException;
import java.util.Scanner;

import com.mq.logger.Logger;
import com.mq.producer.Producer;
import com.mq.producer.ProducerConfig;
import com.mq.producer.ProducerRecord;
import com.mq.serialize.ISerializtion;
import com.mq.serialize.Serialize;
import com.mq.topic.Topic;

public class Test {

	private static Logger log = new Logger(Test.class);

	public static void main(String[] args) {

		String ipAddress = "192.168.1.18";// enter ip provided by MQServer
		int port = 55681; // enter port provided by MQServer
		ProducerConfig pc = new ProducerConfig(ipAddress, port);
		ISerializtion s = new Serialize();
		Producer p = null;
		Scanner scan = null;
		try {
			p = new Producer(pc);
			scan = new Scanner(System.in);
			boolean exit = false;
			while (!exit) {

				log.info("Enter message ['exit' for closing] :");
				String input = scan.nextLine();
				if (input.equals("exit")) {
					log.info("Shutdown in process");
					exit = true;
				} else {
					ProducerRecord r = new ProducerRecord(new Topic("Test_t1"));
					r.setData(s.serialize(input));
					p.send(r);
				}
			}

		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (p != null) {
				p.close();
			}
			if (scan != null) {
				scan.close();
			}
		}

	}
}
