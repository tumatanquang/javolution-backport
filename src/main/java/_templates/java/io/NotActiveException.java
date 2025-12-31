/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2005 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package _templates.java.io;
public class NotActiveException extends ObjectStreamException {
	private static final long serialVersionUID = 498416539265223247L;
	public NotActiveException(String reason) {
		super(reason);
	}
	public NotActiveException() {
		super();
	}
}