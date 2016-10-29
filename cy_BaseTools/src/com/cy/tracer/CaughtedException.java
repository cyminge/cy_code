package com.cy.tracer;

public class CaughtedException extends Exception {
	private static final long serialVersionUID = 3197760023902261356L;
	
	public final static int IGNORE = 0;
	public final static int REBOOT = 1;

	public String mStrExternInfo = null;
	public int mOperation = IGNORE;
	
}
