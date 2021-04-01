package com.mlog.procmon.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.common.CommonStr;

public class StateStep3 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static State state;
	
	private StateContext context;

	public static State instance() {
		if (state == null) {
			state = new StateStep3();
		}

		return state;
	}

	public StateStep3() {
		super();
	}

	@Override
	public int doAction(StateMachine machine) {
		int next = STOP;
		
		context = machine.getContext();
		if (context == null) {
			return next;
		}

		int retv = action();
		switch (retv) {
		case 1:
			machine.setState(StateStep1.instance());
			next = NEXT;

			break;
		case 2:
			machine.setState(StateStep2.instance());
			next = NEXT;

			break;
		case -1:
			machine.setState(null);
			next = STOP;
			context.clearScreen();

			break;
		default:
			next = STOP;

			break;
		}

		return next;
	}

	private int action() {
		context.draw();

		try {
			while (true) {
				int cnt = 0;

				if ((cnt = System.in.available()) != 0) {
					if (cnt == 1) {
						int c = System.in.read();
						logger.debug("StateStep3 {} / {}", c, (char) c);

						switch (c) {
						case  89: // yes(Y)
						case 121: // yes(y)
							handle(c);
							context.execute();

							return 1;
						case  78: // no(N)
						case 110: // no(n)
							handle(c);
							// context.cancel();

							return 2;
						case 52: // back(4)
						case 66: // back(B)
							handle(c);
							// context.cancel();

							return 2;
						case 53: // exit(5)
						case 69: // exit(E)
							return -1;
						default:
							break;
						}
					} else {
						int arr[] = new int[cnt];
						for (int i = 0; i < cnt; i++) {
							arr[i] = System.in.read();
						}
					}

					Thread.sleep(10);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return -1;
	}
	
	private void handle(int c) {
		try {
			System.out.print((char) c);
			Thread.sleep(50);
			clearInput();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private void clearInput() {
		System.out.print("\033[1D"); // Move the cursor backward N columns
		System.out.print("\033[K" ); // Erase to end of line
	}

	@Override
	public String toString() {
		return CommonStr.STATE_3;
	}

}
