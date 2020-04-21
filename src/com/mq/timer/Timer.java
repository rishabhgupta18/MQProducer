package com.mq.timer;

import java.util.function.Predicate;

import com.mq.logger.Logger;

public class Timer extends Thread {

	class TimerCounter {
		private int counter;
		private int initValue;
		private boolean resetFlag;
		private boolean stop;

		public TimerCounter() {
			this(0);
		}

		public TimerCounter(int initialValue) {
			this.initValue = initialValue;
			this.counter = initialValue;
			this.resetFlag = false;
			this.stop = false;
		}

		public void reset() {
			counter = initValue;
			resetFlag = false;
		}
	}

	public static class TimerCallerThread {
		private Thread callerThread;
		private Predicate<Integer> notificationCondition;
		private boolean notificationFlag;

		public TimerCallerThread(Thread callerThread, Predicate<Integer> notificationCondition) {
			this.callerThread = callerThread;
			this.notificationCondition = notificationCondition;
			this.notificationFlag = false;
		}

		public boolean notified() {
			return notificationFlag;
		}

		public boolean isNotificationFlag() {
			return notificationFlag;
		}

		public void setNotificationFlag(boolean notificationFlag) {
			this.notificationFlag = notificationFlag;
		}
		
		
	}

	private TimerCallerThread ct;
	private TimerCounter tc;
	private String ownerName;
	private Logger log;

	public Timer(String ownerName) {
		this(ownerName, 0);
	}

	public Timer(String ownerName, int initialValue) {
		this.ownerName = ownerName;
		log = new Logger(ownerName);
		tc = new TimerCounter(initialValue);
	}

	public Timer(String ownerName, int initialValue, TimerCallerThread callerThread) {
		this.ownerName = ownerName;
		log = new Logger(ownerName);
		tc = new TimerCounter(initialValue);
		this.ct = callerThread;
	}
	
	

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public TimerCallerThread getCallerThread() {
		return ct;
	}

	public void setCallerThread(TimerCallerThread callerThread) {
		this.ct = callerThread;
	}

	public int getTime() {
		return tc.counter;
	}

	public void close() {
		tc.stop = true;
	}

	public void reset() {
		tc.resetFlag = true;
	}

	@Override
	public void run() {

		log.info("started the timer");

		while (!tc.stop) {

			synchronized (tc) {
				while (!tc.stop && tc.resetFlag) {
					tc.reset();
				}

				while (!tc.stop && !tc.resetFlag) {
					tc.counter++;
					if (ct != null && !ct.notificationFlag && ct.notificationCondition.test(tc.counter)) {
						synchronized (ct.callerThread) {
							ct.notificationFlag = true;
							ct.callerThread.notify();
						}
					}
					try {
						Thread.sleep(950);
					} catch (InterruptedException e) {
					}
				}
			}

		}
		log.info("stopped the timer");

	}

}
