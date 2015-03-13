package pl.grm.misc;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import pl.grm.geocompression.*;

public class MLog {
	private static Logger	logger;
	private static MLog		mLog	= new MLog("z1.log");
	
	public MLog(String fileName) {
		Logger loggerR = null;
		try {
			loggerR = setupLogger(fileName);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		this.logger = loggerR;
	}
	
	public static Logger setupLogger(String fileName) throws IllegalArgumentException {
		logger = Logger.getLogger(fileName);
		try {
			FileHandler fileHandler = new FileHandler(fileName, 1048476, 1, true);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		}
		catch (SecurityException e) {}
		catch (IOException e) {}
		logger.info("Logger is running ...");
		return logger;
	}
	
	public static void print(String info, HashMap<Float, ValuePositions> map, boolean sorted) {
		List<Float> listIn = new ArrayList<Float>(map.keySet());
		if (sorted) {
			Collections.sort(listIn);
		}
		ArrayList<String> listOut = new ArrayList<String>();
		for (Float v : listIn) {
			ValuePositions vP = map.get(v);
			listOut.add("");
			listOut.add("Value: " + v);
			listOut.addAll(vP.toStringFullList());
		}
		print(info, listOut, false);
	}
	
	public static void print(String info, Set set, boolean sorted) {
		List list = new ArrayList(set);
		print(info, list, sorted);
	}
	
	public static void print(String info, List list, boolean sorted) {
		if (sorted) {
			Collections.sort(list);
		}
		info("==========================\n" + info + "====================\n");
		String str = "\n";
		int i = 1;
		for (Object obj : list) {
			String strT = obj.toString();
			str += strT;
			System.out.println("Logged " + i + " of " + list.size());
			i++;
		}
		info(str);
	}
	
	public static void info(String msg) {
		int iM = msg.length() / 100;
		iM = iM == 0 ? 1 : iM;
		String[] msgs = new String[iM];
		int i = 0;
		do {
			int iN = i + 100;
			if (iN >= msg.length()) {
				iN = msg.length();
			}
			msgs[i] = msg.substring(i, iN);
			i += 100;
		}
		while (i < iM);
		for (String string : msgs) {
			if (string != null && string != "")
				logger.info(string);
		}
	}
}
