package com.mlog.procmon.cli;


public class StateMachine {
	private State state;
	private StateContext context;
	
	public StateMachine(StateContext context) {
		super();
		this.state = StateStep1.instance();
		this.context = context;
		
		this.context.setState(this.state);
	}

	public int next() {
		int retv = -1;
		if (state != null) {
			retv = state.doAction(this);
		}

		return retv;
	}

	public void setState(State state) {
		this.state = state;
		this.context.setState(state);
	}

	public StateContext getContext() {
		return context;
	}
}
