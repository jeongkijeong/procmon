package com.mlog.procmon.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.procmon.common.CommonStr;
import com.mlog.procmon.common.Utils;
import com.mlog.procmon.status.rx.ProcessStatusRxManager;

public class StateContext implements CommonStr {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private List<HashMap<String, Object>> statusList = null;
	private Map<Integer, HashMap<String, Object>> selectMap = null;

	private boolean selectMode = false;
	private boolean selectAll = false;

	private int fullLineCnt = 0;
	private int headLineCnt = 0;
	private int bodyLineCnt = 0;

	private int index = 0;
	private int menu = -1;
	
//  private String format = "%4s %6s %6s %6s %8s  %-20s %-15s %-10s %-7s %5s";
//	private String format = "%4s %-16s %-6s %-13s %3s %8s %8s %8s %8s   %-8s";
//	private String format = "%4s %-16s %-6s %-13s %3s %8s %8s %8s   %-8s";
//	private String format = "%4s %-16s %-6s %-20s %3s %8s %8s %8s   %-8s";
	private String format = "%4s %-16s %-6s %-20s %3s %8s %8s %8s   %-5s %4s"; // 20201119 MMI_STAT 추가
	private String prompt = null;

	private Map<String, Object> currentmap = null;
	private String buffer = null;
	
	private State state = null;

	private String netwrokType = null;

	public StateContext() {
		System.out.print(getDataHead());
		moveToPrompt();
	}

	public void setProcessStatusData(String jsonStr) {
		if (jsonStr == null) {
			return;
		}

		if (selectMode == true) {
			buffer = jsonStr;
			return;
		} else {
			statusList = Utils.jsonStrToList(jsonStr);
		}

		String type = STATE_0;
		if (state != null) {
			type = state.toString();
		}

		if (STATE_0.equals(type)) {
			return;
		}

		draw();
	}

	public void draw() {
		clearScreen();

		try {
			fullLineCnt = 0;
			headLineCnt = 0;
			bodyLineCnt = 0;
			
			System.out.print(getDataHead());
			System.out.print(getDataBody());
			moveToPrompt();

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void moveToPrompt() {
		logger.debug("moveToPrompt");

		int cnt = fullLineCnt;
		System.out.print("\033[" + (cnt + 0) + "A"); // Up

		cnt = headLineCnt;
		System.out.print("\033[" + (cnt - 3) + "B"); // Down

		cnt = prompt.length();
		System.out.print("\033[" + (cnt - 1) + "C"); // Forward
	}

	public void clearScreen() {
		logger.debug("clearScreen");

		int cnt = prompt.length();
		System.out.print("\033[" + (cnt - 1) + "D"); // Back

		cnt = bodyLineCnt;
		System.out.print("\033[" + (cnt + 3) + "B"); // Down

		for (int i = 0; i < fullLineCnt; i++) {
			System.out.print("\033[1A" + "\033[K");
		}
	}

	private String getScreen() {
		StringBuffer buffer = new StringBuffer();

		String type = STATE_0;
		if (state != null) {
			type = state.toString();
		}

		switch (type) {
		case STATE_0:
			buffer.append("Welcome CATS process monitoring tool \n");
			buffer.append(" 1.").append("select ").append(underLine("C 코어망", 0, 1)).append("\n");
			buffer.append(" 2.").append("select ").append(underLine("B 기간망", 0, 1)).append("\n");
			buffer.append(" 3.").append("select ").append(underLine("T 전화망", 0, 1)).append("\n");
			buffer.append(" 4.").append("select ").append(underLine("A 전체망", 0, 1)).append("\n");
			buffer.append(" 5.").append(underLine("Exit ", 0, 1));
			buffer.append("\n\n");

			break;
		case STATE_1:
			buffer.append("Welcome CATS process monitoring tool \n");
			buffer.append(" 1.").append("select ").append(underLine("One ", 0, 1));
			buffer.append(" 2.").append("select ").append(underLine("All ", 0, 1));
			buffer.append(" 3.").append(underLine("Cancel ", 0,1));
			buffer.append(" 4.").append(underLine("Next "  , 0,1));
			buffer.append(" 5.").append(underLine("Back "  , 0,1));
			buffer.append(" 6.").append(underLine("Exit "  , 0,1));
			buffer.append("\n\n");

			break;
		case STATE_2:
			buffer.append("Welcome CATS process monitoring tool \n");
			buffer.append(" 1.").append(underLine("Start "  , 0, 1));
			buffer.append(" 2.").append(underLine("Close "  , 0, 1));
			buffer.append(" 3.").append(underLine("Restart ", 0, 1));
			buffer.append(" 4.").append(underLine("Back "   , 0, 1));
			buffer.append(" 5.").append(underLine("Exit "   , 0, 1));
			buffer.append(" 6.").append(underLine("Kill(9)" , 0, 1));
			buffer.append("\n\n");

			break;
		case STATE_3:
			buffer.append("Welcome CATS process monitoring tool \n");
			buffer.append(" 1.").append(underLine("Start "  , 0, 1));
			buffer.append(" 2.").append(underLine("Close "  , 0, 1));
			buffer.append(" 3.").append(underLine("Restart ", 0, 1));
			buffer.append(" 4.").append(underLine("Back "   , 0, 1));
			buffer.append(" 5.").append(underLine("Exit "   , 0, 1));
			buffer.append(" 6.").append(underLine("Kill(9)" , 0, 1));
			buffer.append("\n\n");

			break;
		default:
			break;
		}

		String screen = buffer.toString();
		return screen;
	}

	private String getPrompt() {
		
		String type = STATE_0;
		if (state != null) {
			type = state.toString();
		}

		switch (type) {
		case STATE_0:
		case STATE_1:
		case STATE_2:
			prompt = "choose a number or underlying letter : \n";
			
			break;
		case STATE_3:
			prompt = "Do you really want execute?(y/n) \n";
			
			break;
		default:
			break;
		}

		return prompt;
	}

	private String getDataHead() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Utils.setLine("-", 100));
		buffer.append(getScreen());
		buffer.append(getPrompt());
		buffer.append(Utils.setLine("-", 100));

		String string = null;

		String type = STATE_0;
		if (state != null) {
			type = state.toString();
		}

		if (STATE_0.equals(type)) {
			string = "\n";
		} else {
			string = String.format(format + "\n", "NO", "IP", "PID", "PROCESS", "CFG", "%CPU", "%MEM", "DEFUNCT",
					"STAT", "MMI_STAT");
		}

		buffer.append(string);
		string = buffer.toString();
		
		headLineCnt = Utils.getCharCount(string, '\n');
		fullLineCnt += headLineCnt;
		
		return string;
	}

	private String getDataBody() {
		String type = STATE_0;
		if (state != null) {
			type = state.toString();
		}

		if (STATE_0.equals(type)) {
			return "";
		}

		if (statusList == null || statusList.size() == 0) {
			return "";
		}

		String string = null;

		StringBuffer buffer = new StringBuffer();
		int no = 1;

		for (Map<String, Object> status : statusList) {
			if (status == null) {
				continue;
			}

			currentmap = status;
			String procCode = getValue(PROC_CODE);

			if (netwrokType != null) {
				switch (netwrokType) {
				case "C":
					if ("IMMI".equals(procCode) || "BMMI".equals(procCode) || "TMMI".equals(procCode)) {
						continue;
					}
					
					if ("IEGN".equals(procCode) || "BEGN".equals(procCode) || "TEGN".equals(procCode)) {
						continue;
					}

					break;
				case "B":
					if ("IMMI".equals(procCode) || "CMMI".equals(procCode) || "TMMI".equals(procCode)) {
						continue;
					}

					if ("IEGN".equals(procCode) || "CEGN".equals(procCode) || "TEGN".equals(procCode)) {
						continue;
					}

					break;
				case "T":
					if ("IMMI".equals(procCode) || "BMMI".equals(procCode) || "CMMI".equals(procCode)) {
						continue;
					}
					
					if ("IEGN".equals(procCode) || "BEGN".equals(procCode) || "CEGN".equals(procCode)) {
						continue;
					}

					break;
				default:
					break;
				}
			}

			String procStat = getValue(PROC_STAT);
			if (procStat == null || procStat.length() == 0) {
				procStat = "n/a";
			}

			String mmiStat = getValue(MMI_STAT);
			if (mmiStat == null || mmiStat.length() == 0) {
				mmiStat = "n/a";
			}

			String temp = String.format(format, no++, getValue(HOST_IP), getValue(PID_INFO), getValue(PROC_NAME),
					getValue(PROC_INDX), getValue(CPU_INFO), getValue(MEM_INFO), getValue(DEFUNCT_CNT), procStat,
					mmiStat);

			temp = temp.replace("ok"  ,  "\033[32m" + "ok"   + "\033[0m");
			temp = temp.replace("down",  "\033[31m" + "down" + "\033[0m");
			temp = temp.replace("n/a" ,  "\033[33m" + "n/a"  + "\033[0m");

			Boolean flag = (Boolean) status.get(SELECT);

			// text, background change
			if (flag != null && flag == true) {
				temp = "\033[7m" + temp + "\033[0m";
			}

			buffer.append(temp).append("\n");
		}

		bodyLineCnt = no - 1;

		string = buffer.toString();
		fullLineCnt += bodyLineCnt;

		return string;
	}

	private String getValue(String key) {
		String value = "";

		if (currentmap.get(key) == null) {
			return value;
		}

		value = currentmap.get(key).toString();

		return value;
	}

	public void selectOne() {
		if (selectMode || statusList == null || statusList.size() == 0) {
			return;
		} else {
			selectMode = true;
		}		
		
		Map<String, Object> map = statusList.get(index);
		map.put(SELECT, true);

		draw();
	}

	public void selectAll() {
		if (selectMode || statusList == null || statusList.size() == 0) {
			return;
		} else {
			selectMode = true;
			selectAll = true;
		}

		if (selectMap == null) {
			selectMap = new HashMap<Integer, HashMap<String, Object>>();
		}

		int i = 0;
		for (HashMap<String, Object> map : statusList) {
			selectMap.put(i++, map);
			map.put(SELECT, true);
		}

		draw();
	}

	public void cancel() {
		if (!selectMode || statusList == null || statusList.size() == 0) {
			return;
		}
		
		selectMode = false;
		selectAll = false;

		if (buffer != null) {
			statusList = Utils.jsonStrToList(buffer);
		} else {
			for (Map<String, Object> map : statusList) {
				map.put(SELECT, false);
			}
		}

		buffer = null;
		index = 0;

		if (selectMap != null) {
			selectMap.clear();
		}
		
		menu = -1;

		draw();
	}

	public void execute() {
		if (menu == -1) {
			return;
		}

		String type = null;
		switch (menu) {
		case 51:
			type = PROCESS_RESTART;

			break;
		case 49:
			type = PROCESS_START;

			break;
		case 50:
			type = PROCESS_CLOSE;

			break;
		case 54:
			type = PROCESS_KILL9;

			break;
		default:
			break;
		}

		List<HashMap<String, Object>> selectList = new ArrayList<HashMap<String, Object>>();
		if (type != null) {
			for (Integer i : selectMap.keySet()) {
				logger.debug("{} / {}", type, i);
				selectList.add(statusList.get(i));
			}
		}

		Map<String, Object> object = new HashMap<String, Object>();
		object.put(PROC_LIST, selectList);
		object.put(DATA_TYPE, type);

		ProcessStatusRxManager.getInstance().address(object);
		
		cancel();
	}

	public boolean next() {
		try {
			if (selectMap != null && selectMap.size() > 0) {
				return true;
			}

		} catch (Exception e) {
			logger.error("", e);
		}

		return true;
	}

	public void select() {
		if (selectAll == true) {
			return;
		}
		
		if (!selectMode || statusList == null || statusList.size() == 0) {
			return;
		}

		if (selectMap == null) {
			selectMap = new HashMap<Integer, HashMap<String, Object>>();
		}

		HashMap<String, Object> item = statusList.get(index);
		if (selectMap.get(index) == null) {
			selectMap.put(index, item);

			item.put(SELECT, true);
		} else {
			selectMap.remove(index);

			item.put(SELECT, false);
		}

		draw();
	}

	public void upDown(int type) {
		if (!selectMode || statusList == null || statusList.size() == 0) {
			return;
		}

		int size = statusList.size();
		int curr = -1;
		int last = index;

		if (selectMap == null || selectMap.get(last) == null) {
			Map<String, Object> lastItem = statusList.get(last);
			lastItem.put(SELECT, false);
		}

		if (type == 65) {
			curr = index - 1;

			if (curr < 0) {
				curr = size - 1;
			}
		} else {
			curr = index + 1;

			if (curr > size - 1) {
				curr = 0;
			}
		}
		
		index = curr;

		Map<String, Object> currItem = statusList.get(curr);
		currItem.put(SELECT, true);

		draw();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setMenu(int menu) {
		this.menu = menu;

		logger.debug("{}", this.menu);
	}

	private String underLine(String data, int stt, int end) {
		String src = data.substring(stt, end);
		String trg = data.replace(src, "\033[4m" + src + "\033[0m");

		return trg;
	}

	public String getNetwrokType() {
		return netwrokType;
	}

	public void setNetwrokType(String netwrokType) {
		this.netwrokType = netwrokType;
	}
}
