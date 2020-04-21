package com.mq.producer;

import java.net.ConnectException;
import java.util.function.Predicate;

import com.mq.logger.Logger;
import com.mq.monitor.Monitoring;

public class Producer extends Monitoring {

	private Logger log = new Logger(Producer.class);
	private static final int WAITING_TIME = 2 * 60;
	private RecordAccumulator accumulator;

	public Producer(ProducerConfig prop) throws ConnectException {
		super(Producer.class.getSimpleName(), (Predicate<Integer>) (Integer t) -> t > WAITING_TIME);
		log.info("Starting Producer");
		start();
		this.accumulator = new RecordAccumulator(prop);
		log.info("Producer started");
	}

	public void send(ProducerRecord record) {
		if (isClosed() || isStopped()) {
			throw new RuntimeException("Producer : is not running ...");
		}
		accumulator.send(record);
		reset();
	}
	
	@Override
	public void operation() {
		stop();
		log.info("No new Request in " + getTimer().getTime() + " secound. Stopping producer");
	}

	@Override
	public void close() {
		if (!isClosed()) {
			super.close();
			log.info("stopping");
			accumulator.close();
			log.info("stopped");
		} else {
			log.info("Producer is not running");
		}

	}

}
