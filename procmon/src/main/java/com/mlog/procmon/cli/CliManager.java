package com.mlog.procmon.cli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.main.ProcessManager;

public class CliManager implements ProcessManager{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static CliManager instance;
	
	private StateMachine machine;
	private String ttyConfig;
	
	public static CliManager getInstance() {
		if (instance == null) {
			instance = new CliManager();
		}

		return instance;
	}

	/**
	 * OCT CLI 대화형 시작.
	 */
	public void start() {
		logger.debug("{} start", this.getClass().getSimpleName());

		try {
			setTerminalToCBreak();
			machine = new StateMachine(new StateContext());
			machine.setState(StateStep1.instance());

			while (true) {
				if (machine.next() != State.NEXT) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				stty(ttyConfig.trim());
			} catch (Exception e) {
				logger.error("", e);
			}
			
			close();
		}
	}

	/**
	 */
	public void close() {
		try {
			if (machine != null) {
				machine.getContext().clearScreen();
			}
			
			stty(ttyConfig.trim());
		} catch (Exception e) {
			logger.error("", e);
		}
		
		System.exit(0);
		
		logger.debug("{} close", this.getClass().getSimpleName());
	}

	@Override
	public void address(Object object) {
		if (machine != null) {
			machine.getContext().setProcessStatusData(object.toString());
		}
	}
	
    private void setTerminalToCBreak() throws IOException, InterruptedException {
        ttyConfig = stty("-g");

        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");

        // disable character echoing
        stty("-echo");
    }
    

    /**
     *  Execute the stty command with the specified arguments
     *  against the current active terminal.
     */
	private String stty(final String args) throws IOException, InterruptedException {
		String cmd = "stty " + args + " < /dev/tty";

		return exec(new String[] { "sh", "-c", cmd });
	}

    /**
     *  Execute the specified command and return the output
     *  (both stdout and stderr).
     */
	private String exec(final String[] cmd) throws IOException, InterruptedException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		Process p = Runtime.getRuntime().exec(cmd);
		int c;
		InputStream in = p.getInputStream();

		while ((c = in.read()) != -1) {
			bout.write(c);
		}

		in = p.getErrorStream();

		while ((c = in.read()) != -1) {
			bout.write(c);
		}

		p.waitFor();

		String result = new String(bout.toByteArray());

		logger.info("exec : {}", result);

		return result;
	}

}
