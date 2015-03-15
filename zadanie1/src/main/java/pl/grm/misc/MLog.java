package pl.grm.misc;

import java.io.*;
import java.util.logging.*;

public class MLog {
	private Logger			logger;
	private static MLog		mLog	= new MLog("z1.log");
	public static boolean	ON		= false;
	
	private MLog(String fileName) {
		try {
			this.logger = setupLogger(fileName);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private static Logger setupLogger(String fileName) throws IllegalArgumentException {
		Logger logger = Logger.getLogger(fileName);
		try {
			FileHandler fileHandler = new FileHandler(fileName, 1048476, 1, true);
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return logger;
	}
	
	public static void info(String msg) {
		if (ON) {
			new Thread(() -> {
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
				try {
					mLog.infoNS(msg);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	public synchronized void infoNS(String msg) {
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
		mLog.logger.info(stringO);
	}
}
