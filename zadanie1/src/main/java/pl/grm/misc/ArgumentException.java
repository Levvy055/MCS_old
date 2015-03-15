package pl.grm.misc;

public class ArgumentException extends Exception {
	private static final long	serialVersionUID	= 1L;
	private static String		args				= "Poprawne parametry:\n compress <nazwa pliku / -test <ID>\ngdzie ID = (1 - 4)\n decompress <nazwa pliku>\n -l aktywuje log";
	
	public ArgumentException(String string) {
		super(args + string);
	}
	
}
