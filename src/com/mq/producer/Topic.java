package com.mq.producer;

import java.io.Serializable;

public final class Topic implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int id;
	private String key;

	public Topic(String key) {
		this.key = key;
		this.id = key.hashCode();
	}

	public int getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {

		boolean isEqual = false;
		if (obj instanceof Topic) {
			Topic t = (Topic) obj;
			isEqual = this.key.equals(t.key);
		}

		return isEqual;
	}

	@Override
	public String toString() {
		return key;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Topic(key);
	}

}
