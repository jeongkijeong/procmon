package com.mlog.procmon.cli;

public interface State {
	public int STOP = 0;
	public int NEXT = 0;

	public int doAction(StateMachine machine);
}
