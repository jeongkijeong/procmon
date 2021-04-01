package com.mlog.procmon.main;

import com.mlog.procmon.common.CommonStr;

public interface ProcessManager extends CommonStr {
	public void start();

	public void close();
	
	public void address(Object object);
}
