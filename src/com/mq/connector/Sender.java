package com.mq.connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mq.producer.ProducerConfig;
import com.mq.producer.ProducerRecord;
import com.mq.serialize.ISerializtion;

public class Sender {

	private Socket socket;
	private ProducerConfig prop;
	private ThreadPoolExecutor pool;

	public Sender(ProducerConfig prop) throws ConnectException {
		this.prop = prop;
		connect();
		this.pool = new ThreadPoolExecutor(5, 8, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
				new ThreadPoolExecutor.AbortPolicy());
	}

	private void connect() throws ConnectException {

		try {

			if (prop.getIpaddress() == null)
				throw new RuntimeException(
						"Unable to connect to MQ Server. Please check IP " + "[" + prop.getIpaddress() + "]");
			try {
				this.socket = new Socket(prop.getIpaddress(), prop.getPort());
			} catch (IOException e) {
				throw new ConnectException(
						"Unable to connect to MQ Server. Please check IP " + "[" + prop.getIpaddress() + "]");
			}

		} catch (ConnectException e) {
			System.out.println("connected to MQ Server ...");
		}

	}

	public void send(List<ProducerRecord> records) {

		pool.execute(() -> {
			PrintWriter out = null;
			ISerializtion serialize = prop.getSerialization();
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				out.println(serialize.serialize(records));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error occurred while transferring to Server ::: " + e.getMessage());
			} finally {
				if (out != null)
					out.close();
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
