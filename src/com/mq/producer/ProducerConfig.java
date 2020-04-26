package com.mq.producer;

public class ProducerConfig {

	private String ipaddress;
	private int port;
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
