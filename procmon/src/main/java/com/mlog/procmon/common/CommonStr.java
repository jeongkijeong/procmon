package com.mlog.procmon.common;

public interface CommonStr {
	public final static int SUCCESS = +0;
	public final static int FAILURE = -1;

	public final String PROCESS_START = "start";
	public final String PROCESS_CLOSE = "close";
	public final String PROCESS_KILL9 = "shutdown";
	public final String PROCESS_RESTART = "restart";

	public final String DATA_TYPE = "TYPE";
	public final String PROC_NO   = "PROC_NO";
	public final String PROC_IP   = "PROC_IP";
	public final String PROC_LIST = "PROC_LIST";
	public final String WATCH_YN  = "WATCH_YN";

	public final String PROC_NAME = "PROC_NAME";
	public final String PROC_INDX = "PROC_INDX";
	public final String PROC_STAT = "PROC_STAT";
	public final String MMI_STAT  = "MMI_STAT";
	public final String PROC_CODE = "PROC_CODE";
	public final String PID_INFO  = "PID_INFO";
	public final String CPU_INFO  = "CPU_INFO";
	public final String MEM_INFO  = "MEM_INFO";
	public final String RSS_INFO  = "RSS_INFO";
	public final String HOST_IP   = "HOST_IP";
	public final String DEFUNCT_CNT = "DEFUNCT_CNT";

	public final String SELECT = "SELECT";

//	public final String STATE_0 = "0";
	public final String STATE_1 = "1";
	public final String STATE_2 = "2";
	public final String STATE_3 = "3";
}
