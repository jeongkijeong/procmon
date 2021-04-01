package com.mlog.procmon.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.common.CommonStr;
import com.mlog.procmon.common.Constant;
import com.mlog.procmon.common.Utils;
import com.mlog.procmon.context.ContextManager;

import sun.misc.Signal;
import sun.misc.SignalHandler;


public class ProcessMain implements SignalHandler, CommonStr {
	private static final Logger logger = LoggerFactory.getLogger(ProcessMain.class);

	public static void main(String[] args) throws Exception {
		int retv = -1;

		String proerties = "./conf/server.properties";
		String logConfig = "./conf/logback.xml";

		if (args != null && args.length == 2) {
			proerties = args[0];
			logConfig = args[1];
		}

		retv = Utils.loadProperties(proerties);
		if (retv < 0) {
			return;
		}

		retv = Utils.loadLogConfigs(logConfig);
		if (retv < 0) {
			return;
		}

		ProcessMain processMain = new ProcessMain();
		delegateHandler("TERM", processMain);
		delegateHandler("INT" , processMain);

		processMain.startProcess();

		while (Constant.RUN) {
			Thread.sleep(3000);
		}
	}

	/**
	 * start main process.
	 * */
	private void startProcess() {
		ContextManager contextManager = ContextManager.getInstance();
		contextManager.startManager();
	}

	/**
	 * close main process.
	 * */
	private void closeProcess() {
		ContextManager contextManager = ContextManager.getInstance();
		contextManager.closeManager();

		Constant.RUN = false;
		System.exit(0);
	}

	@Override
	public void handle(Signal signal) {
		logger.info("Received SIG NAME[" + signal.getName() + "] / NUMB[" + signal.getNumber() + "]");

		String SIGName = signal.getName();
		if (SIGName == null || SIGName.length() == 0) {
			return;
		}

		// end of process
		switch (SIGName) {
		case "TERM":
		case "INT":
			logger.info("close " + getClass().getName());
			closeProcess();

			break;
		default:
			break;
		}
	}

	public static void delegateHandler(String SIGName, SignalHandler SIGHandler) {
		Signal SIG = null;

		try {
			SIG = new Signal(SIGName);
			SIGHandler = Signal.handle(SIG, SIGHandler);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
