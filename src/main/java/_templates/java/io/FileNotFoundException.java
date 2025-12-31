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
public class FileNotFoundException extends IOException {
	private static final long serialVersionUID = 2300640965756686351L;
	public FileNotFoundException() {}
	public FileNotFoundException(String s) {
		super(s);
	}
}