package com.mlog.procmon.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.common.CommonStr;

public class StateStep2 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static State state;
	
	private StateContext context;

	public static State instance() {
		if (state == null) {
			state = new StateStep2();
		}

		return state;
	}

	public StateStep2() {
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
		case 3:
			machine.setState(StateStep3.instance());
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
						logger.debug("StateStep2 {} / {}", c, (char) c);
						
						switch (c) {
						case 49: // start(1)
						case 83: // start(S)
							handle(c);
							context.setMenu(49);

							return 3;
						case 50: // exit(2)
						case 67: // exit(C)
							handle(c);
							context.setMenu(50);

							return 3;
						case 51: // restart(2)
						case 82: // restart(R)
							handle(c);
							context.setMenu(51);
							
							return 3;
						case 52: // back(4)
						case 66: // back(B)
							handle(c);
							// context.cancel();

							return 1;
						case 53: // exit(5)
						case 69: // exit(E)
							return -1;
						case 54: // force exit(6)
						case 75: // force exit(K)
							handle(c);
							context.setMenu(54);

							return 3;
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
		System.out.print("\033[1D"); // move the cursor backward N columns
		System.out.print("\033[K" ); // erase to end of line
	}

	@Override
	public String toString() {
		return CommonStr.STATE_2;
	}

}
