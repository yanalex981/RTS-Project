package rts.networking;

import java.io.File;

public class BadMapFileException extends Exception {
	private static final long serialVersionUID = 6608302484553276561L;
	
	public BadMapFileException() {
		super();
	}
	
	public BadMapFileException(String msg) {
		super(msg);
	}
	
	public BadMapFileException(File file, String msg) {
		super(file.getName() + msg);
	}
}
