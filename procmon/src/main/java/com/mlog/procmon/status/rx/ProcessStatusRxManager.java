package com.mlog.procmon.status.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medialog.meerkat.client.tcp.TcpClient;
import com.medialog.meerkat.handler.MeerKat;
import com.mlog.procmon.common.Constant;
import com.mlog.procmon.common.Utils;
import com.mlog.procmon.context.TimeHandler;
import com.mlog.procmon.main.ProcessManager;


public class ProcessStatusRxManager implements ProcessManager {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static ProcessStatusRxManager instance = null;

	private TcpClient meerkatClient = null;
	private MeerKat meerKat = null;

	public static ProcessStatusRxManager getInstance() {
		if (instance == null) {
			instance = new ProcessStatusRxManager();
		}

		return instance;
	}

	@Override
	public void start() {
		String host = Utils.getProperty(Constant.PROCMON_HOST);
		String port = Utils.getProperty(Constant.PROCMON_PORT);

		if (host == null || port == null) {
			close();
			return;
		}

		if (meerkatClient == null) {
			meerkatClient = new TcpClient(host, port, meerKat = new ProcessStatusRxHandler());

			Thread handlerThread = new Thread((TimeHandler) meerKat);
			handlerThread.start();

			Thread meerkatThread = new Thread(meerkatClient);
			meerkatThread.start();
		}

		logger.info("start {} ", getClass().getSimpleName());
	}

	@Override
	public void close() {
		if (meerkatClient != null) {
			meerkatClient.close();
		}

		if (meerKat != null) {
			((TimeHandler) meerKat).isRun(false);
		}
	}

	@Override
	public void address(Object object) {
		if (meerKat != null) {
			((TimeHandler) meerKat).put(object);
		}
	}
}
