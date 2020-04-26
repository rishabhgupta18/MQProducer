package com.mq.connector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mq.logger.Logger;
import com.mq.producer.ProducerConfig;
import com.mq.producer.ProducerRecord;
import com.mq.serialize.ISerializtion;
import com.mq.serialize.Serialize;

public class Sender {

	private Socket socket;
	private ProducerConfig prop;
	private ThreadPoolExecutor pool;
	private Logger log;

	public Sender(ProducerConfig prop) throws ConnectException {
		this.prop = prop;
		log = new Logger(Sender.class);
		connect();
		this.pool = new ThreadPoolExecutor(5, 8, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
				new ThreadPoolExecutor.AbortPolicy());
	}

	private void connect() throws ConnectException {

		int retryAttempts = prop.getRetryAttempts();
		while (retryAttempts > 0) {
			log.info("Connecting to MQ Server");
			if (prop.getIpaddress() == null)
				throw new RuntimeException(
						"Unable to connect to MQ Server. IP is null " + "[" + prop.getIpaddress() + "]");
			try {
				log.info("Connecting to IP["+prop.getIpaddress()+"], port["+prop.getPort()+"]");
				socket = new Socket();
				socket.connect(new InetSocketAddress(prop.getIpaddress(), prop.getPort()), 5000);
				return;
			} catch (Exception e) {

			}

			log.error("Retry Attempt will be made after 3 seconds");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			retryAttempts--;
		}
		throw new ConnectException(
				"Unable to connect to MQ Server. Please check IP " + "[" + prop.getIpaddress() + "]");
	}

	public void send(List<ProducerRecord> records) {

		pool.execute(() -> {
			DataOutputStream out = null;
			ISerializtion serialize = new Serialize();
			try {
				out = new DataOutputStream(this.socket.getOutputStream());
				out.writeUTF(serialize.serialize(records));
				out.flush();
			} catch (Exception e) {
				log.error("Error occurred while transferring to Server ::: " + e.getMessage());
			} finally {
				if (out != null)
					try {
						out.close();
					} catch (IOException e) {
						log.error("Exception while closing the output stream");
					}
			}
			// retry
			int retryAttempts = prop.getRetryAttempts();
			while (retryAttempts > 0) {

				retryAttempts--;
			}

		});

	}

	public InetAddress getSocketAddress() {
		return this.socket.getInetAddress();
	}

	public int getPort() {
		return this.socket.getLocalPort();
	}

	public void close() {
		this.pool.shutdown();
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
			}
	}

}
