package com.mlog.procmon.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.cli.CliManager;
import com.mlog.procmon.common.CommonStr;
import com.mlog.procmon.common.Constant;
import com.mlog.procmon.main.ProcessManager;
import com.mlog.procmon.status.rx.ProcessStatusRxManager;

public class ContextManager implements CommonStr {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<ProcessManager> managerList = null;
	
	public static ContextManager instance = null;

	public static ContextManager getInstance() {
		if (instance == null) {
			instance = new ContextManager();
		}

		return instance;
	}

	public ContextManager() {
		super();

		managerList = new ArrayList<ProcessManager>();

		managerList.add(ProcessStatusRxManager.getInstance());
		managerList.add(CliManager.getInstance());
	}

	/**
	 * 컨텍스트 매니저 시작.
	 */
	public int startManager() {
		logger.debug(this.getClass().getSimpleName() + " start");

		Constant.RUN = true;
		try {
			for (ProcessManager manager : managerList) {
				manager.start();
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		logger.debug(this.getClass().getSimpleName() + " start completed");

		return -1;
	}

	/**
	 * 컨텍스트 매니저 종료.
	 */
	public int closeManager() {
		logger.debug(this.getClass().getSimpleName() + " close");

		Constant.RUN = false;

		for (ProcessManager manager : managerList) {
			manager.close();
		}
		
		logger.debug(this.getClass().getSimpleName() + " close completed");
		
		return -1;
	}
}
