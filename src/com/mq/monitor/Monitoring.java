package com.mq.monitor;

import java.util.function.Predicate;

import com.mq.logger.Logger;
import com.mq.timer.Timer;
import com.mq.timer.Timer.TimerCallerThread;

public class Monitoring {

	private Logger log;
	private Thread monitor;
	private Timer timer;
	private volatile boolean stop;
	private volatile boolean stopped;

	public Monitoring(String className) {
		log = new Logger(className + " " + Monitoring.class.getSimpleName());
		this.timer = new Timer(log.getName());
	}

	public Monitoring(String className, Predicate<Integer> notificationCondition) {
		log = new Logger(className + " " + Monitoring.class.getSimpleName());
		register();
		this.timer = new Timer(log.getName(), 0, new TimerCallerThread(monitor, notificationCondition));
	}

	public boolean isClosed() {
		return stopped;
	}

	public boolean isStopped() {
		return stop;
	}

	public void stop() {
		stop = true;
	}

	public void start() {
		monitor.start();
	}

	public void reset() {
		timer.reset();
	}

	public void close() {
		if (!isClosed()) {
			stopped = true;
			timer.close();
			try {
				timer.join();
			} catch (InterruptedException e) {
			}
		}
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void operation() {
	}

	private void register() {

		monitor = new Thread(() -> {
			log.info("started monitoring");
			TimerCallerThread th = timer.getCallerThread();
			timer.start();
			synchronized (monitor) {
				while (!stop) {
					while (!stop && !th.notified()) {
						try {
							monitor.wait();
						} catch (InterruptedException e) {
						}
					}
					operation();
					th.setNotificationFlag(false);
				}
			}
			if (!isClosed()) {
				close();
			}
			log.info("stopped monitoring");
		});

	}
}
