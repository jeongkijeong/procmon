package com.mlog.procmon.context;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mlog.procmon.common.CommonStr;
import com.mlog.procmon.common.Constant;

public abstract class DataHandler implements Runnable, CommonStr {
	private ArrayBlockingQueue<Object> queue = null;
	private Boolean isRun = null;
	
	public DataHandler() {
		super();
		queue = new ArrayBlockingQueue<Object>(100);
		this.isRun = Constant.RUN;
	}

	@Override
	public void run() {
		try {
			while (isRun) {
				Object object = queue.poll(3, TimeUnit.SECONDS);
				if (object == null) {
					continue;
				}

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

	public abstract void handler(Object object);
}
