package com.mq.producer;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.mq.connector.Sender;
import com.mq.logger.Logger;
import com.mq.monitor.Monitoring;
import com.mq.topic.Topic;

public class RecordAccumulator extends Monitoring {

	private Logger log = new Logger(RecordAccumulator.class);
	private Map<Topic, List<ProducerRecord>> recordBatcher;
	private final static int THRESHOLD = 50;
	private final static int TIME_LIMIT_SECONDS = 5;
	private ThreadPoolExecutor helperPool;
	private Sender sender;

	public RecordAccumulator(ProducerConfig prop) throws ConnectException {
		super(RecordAccumulator.class.getSimpleName(), (Predicate<Integer>) (Integer t) -> t >= TIME_LIMIT_SECONDS);
		log.info("Starting RecordAccumulator");
		this.recordBatcher = new HashMap<>(2 * THRESHOLD);
		start();
		this.sender = new Sender(prop);
		helperPool = new ThreadPoolExecutor(2, 4, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4 * THRESHOLD),
				new ThreadPoolExecutor.AbortPolicy());
		log.info("RecordAccumulator started");
	}

	public void send(ProducerRecord record) {

		if (recordBatcher.size() >= THRESHOLD) {
			operation();
		}
		List<ProducerRecord> records = recordBatcher.getOrDefault(record.getTopic(), new LinkedList<>());
		records.add(record);
		recordBatcher.put(record.getTopic(), records);
	}

	@Override
	public void operation() {
		if(!this.recordBatcher.isEmpty()) {
			Map<Topic, List<ProducerRecord>> rb = this.recordBatcher;
			send(rb);
			this.recordBatcher = new HashMap<>(2 * THRESHOLD);
		}
		reset();
	}
	
	private void send(Map<Topic, List<ProducerRecord>> records) {

		helperPool.execute(() -> {
			for (Map.Entry<Topic, List<ProducerRecord>> e : records.entrySet()) {
				this.sender.send(e.getValue());
			}
		});
	}

	
	public void close() {
		if(!isClosed()) {
			super.close();
			helperPool.shutdown();
			sender.close();
		}
	}

}
