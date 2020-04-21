package com.mq.producer;

import java.io.Serializable;

public class ProducerRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Topic topic;
	private int customHashKey;
	private Integer partitionNumber;
	private byte[] data;

	public ProducerRecord(Topic topic) {
		this(topic, null);
	}

	public ProducerRecord(Topic topic, Integer partitionNumber) {
		this(topic, topic.getId(), partitionNumber);
	}

	public ProducerRecord(Topic topic, Integer customHashKey, Integer partitionNumber) {
		this.topic = topic;
		this.customHashKey = customHashKey;
		this.partitionNumber = partitionNumber;
	}
	
	public Integer getCustomHashKey() {
		return customHashKey;
	}

	public void setCustomHashKey(int customHashKey) {
		this.customHashKey = customHashKey;
	}

	public Integer getPartitionNumber() {
		return partitionNumber;
	}

	public void setPartitionNumber(int partitionNumber) {
		this.partitionNumber = partitionNumber;
	}

	public Topic getTopic() {
		return topic;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
