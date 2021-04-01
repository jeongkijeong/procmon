package com.mlog.procmon.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.common.CommonStr;

public class StateStep1 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static State state;
	
	private StateContext context;
	public static State instance() {
		if (state == null) {
			state = new StateStep1();
		}

		return state;
	}

	public StateStep1() {
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
		case -1:
			// close program.
			machine.setState(null);
			next = STOP;
			context.clearScreen();

			break;
		case 2:
			machine.setState(StateStep2.instance());
			next = NEXT;

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
						logger.debug("{} / {}", c, (char) c);

						switch (c) {
						case 49: // select(1)
						case 79: // select(O)
							handle(c);
							context.selectOne();

							break;
						case 50: // select all(2)
						case 65: // select all(A)
							handle(c);
							context.selectAll();

							break;
						case 27: // cancel(3)
							context.cancel();

							break;
						case 51: // cancel(3)
						case 67: // cancel(C)
							handle(c);
							context.cancel();

							break;
						case 52: // next(4)
						case 78: // next(N)
							handle(c);
							if (context.next()) {
								return 2;
							}

							break;
						case 10: // enter
							context.select();
							
							break;
						case 53: // back(5)
						case 66: // back(B)

							return -1;
						case 54: // exit(6)
						case 69: // exit(E)
							handle(c);

							return -1;
						default:
							break;
						}
					} else {
						if (cnt < 3) {
							continue;
						}

						int arr[] = new int[cnt];
						for (int i = 0; i < cnt; i++) {
							arr[i] = System.in.read();
						}

						if (arr[0] != 27 || arr[1] != 91) {
							continue;
						}

						int c = arr[2];
						switch (c) {
						case 65: // up
							context.upDown(c);

							break;
						case 66: // down
							context.upDown(c);

							break;
						default:
							break;
						}
					}
				}

				Thread.sleep(10);
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
		return CommonStr.STATE_1;
	}
}
