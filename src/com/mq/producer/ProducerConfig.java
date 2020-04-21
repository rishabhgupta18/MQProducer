package com.mq.producer;

import com.mq.serialize.ISerializtion;

public class ProducerConfig {

	private String ipaddress;
	private int port;
	private ISerializtion serialization;
	private int retryAttempts;
	private int producerMaxIdleTimeInSeconds;

	public ProducerConfig(String ipaddress, int port) {
		this.ipaddress = ipaddress;
		this.port = port;
		this.retryAttempts = 3;
		this.producerMaxIdleTimeInSeconds = 120;
		
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ISerializtion getSerialization() {
		return serialization;
	}

	public void setSerialization(ISerializtion serialization) {
		this.serialization = serialization;
	}

	public int getRetryAttempts() {
		return retryAttempts;
	}

	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	public int getProducerMaxIdleTimeInSeconds() {
		return producerMaxIdleTimeInSeconds;
	}

	public void setProducerMaxIdleTimeInSeconds(int producerMaxIdleTimeInSeconds) {
		this.producerMaxIdleTimeInSeconds = producerMaxIdleTimeInSeconds;
	}
	
	

}
