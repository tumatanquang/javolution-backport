/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2005 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package _templates.java.io;
import java.io.IOException;
public abstract class ObjectStreamException extends IOException {
	private static final long serialVersionUID = 3182316735836843330L;
	protected ObjectStreamException(String classname) {
		super(classname);
	}
	protected ObjectStreamException() {
		super();
	}
}