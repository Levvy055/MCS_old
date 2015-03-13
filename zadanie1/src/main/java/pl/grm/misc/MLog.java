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
		MLog.logger = loggerR;
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
	
	public static void print(String info, Map<Float, ValuePositions> map, boolean sorted) {
		List<Float> listIn = new ArrayList<Float>(map.keySet());
		if (sorted) {
			Collections.sort(listIn);
		}
		ArrayList<String> listOut = new ArrayList<String>();
		for (Float v : listIn) {
			ValuePositions vP = map.get(v);
			listOut.add("Value: " + v + " ");
			listOut.addAll(vP.toStringFullList());
			listOut.add("\r\n");
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
		info("======================" + info + "======================\n");
		String str = "\n";
		int i = 1;
		for (Object obj : list) {
			String strT = obj.toString();
			str += strT;
			System.out.println("Logged: (" + strT + ") " + i + " of " + list.size());
			i++;
		}
		info(str);
	}
	
	public static void info(String msg) {
		int msgL = msg.length();
		int iM = msgL / 100;
		iM = iM == 0 ? 1 : iM;
		float rL = (float) msgL / 100;
		float riM = rL - iM;
		iM = riM > 0 ? iM + 1 : iM;
		String[] msgs = new String[iM];
		int i = 0;
		do {
			int iC = i * 100;
			int iN = iC + 100;
			if (iN >= msgL) {
				iN = msgL;
			}
			iN = iN > msgL ? msgL - 1 : iN;
			msgs[i] = msg.substring(iC, iN);
			if (msgL > iN && msg.substring(iN, msgL - 1).length() < 20) {
				msgs[i] += msg.substring(iN, msgL - 1);
				i++;
			}
			i++;
		}
		while (i < iM);
		String stringO = "";
		for (String string : msgs) {
			if (string != null && string != "")
				stringO += string + "\r\n";
		}
		logger.info(stringO);
	}
}
