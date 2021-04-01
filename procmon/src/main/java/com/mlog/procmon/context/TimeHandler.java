package com.mlog.procmon.context;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mlog.procmon.common.CommonStr;
import com.mlog.procmon.common.Constant;

public abstract class TimeHandler implements Runnable, CommonStr {
	private ArrayBlockingQueue<Object> queue = null;
	private Boolean isRun = null;
	
	private int timeout = 30;
	
	public TimeHandler() {
		super();
		queue = new ArrayBlockingQueue<Object>(100);
		this.isRun = Constant.RUN;
	}

	@Override
	public void run() {
		try {
			while (isRun) {
				Object object = queue.poll(timeout, TimeUnit.SECONDS);
				handler(object);
			}
		} catch (Exception e) {
		}
	}

	public void put(Object object) {
		try {
			queue.put(object);
		} catch (Exception e) {
		}
	}
	
	public void isRun(Boolean run) {
		this.isRun = run;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public abstract void handler(Object object);
}
