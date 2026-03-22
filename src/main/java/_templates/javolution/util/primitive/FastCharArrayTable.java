/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2005 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package _templates.javolution.util.primitive;
import java.io.IOException;
import _templates.java.io.ObjectInputStream;
import _templates.java.io.ObjectOutputStream;
import _templates.java.io.Serializable;
import _templates.java.lang.CloneNotSupportedException;
import _templates.java.lang.Cloneable;
import _templates.java.lang.UnsupportedOperationException;
import _templates.java.util.RandomAccess;
/**
 * Implement <code>FastArrayTable</code> for the <code>char</code> primitive data type.
 * @since 5.7.1
 */
public class FastCharArrayTable implements Cloneable, RandomAccess, Serializable {
	private transient char[] elementData;
	private int size;
	public FastCharArrayTable() {
		this(10);
	}
	public FastCharArrayTable(int initialCapacity) {
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
		elementData = new char[initialCapacity];
	}
	public FastCharArrayTable(char[] src) {
		size = src.length;
		elementData = new char[size];
		System.arraycopy(src, 0, elementData, 0, size);
	}
	public void trimToSize() {
		if(size >= elementData.length)
			return;
		final char[] tmp = new char[size];
		System.arraycopy(elementData, 0, tmp, 0, size);
		elementData = tmp;
	}
	public void ensureCapacity(int min) {
		if(min <= elementData.length)
			return;
		int newCap = (elementData.length * 3 >> 1) + 1;
		if(newCap < min) {
			newCap = min;
		}
		final char[] tmp = new char[newCap];
		System.arraycopy(elementData, 0, tmp, 0, size);
		elementData = tmp;
	}
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size == 0;
	}
	public boolean contains(char v) {
		return indexOf(v) >= 0;
	}
	public int indexOf(char v) {
		for(int i = 0; i < size; ++i) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public int lastIndexOf(char v) {
		for(int i = size - 1; i >= 0; --i) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public Object/*FastCharArrayTable*/ clone() throws CloneNotSupportedException {
		/*@JVM-1.1+@
		if(true) {
			final FastCharArrayTable c = (FastCharArrayTable) super.clone();
			c.elementData = new char[elementData.length];
			System.arraycopy(elementData, 0, c.elementData, 0, size);
			return c;
		}
		/**/
		throw new UnsupportedOperationException("J2ME Not Supported Yet");
	}
	public char[] toArray() {
		final char[] a = new char[size];
		System.arraycopy(elementData, 0, a, 0, size);
		return a;
	}
	public char get(int index) {
		rangeCheck(index);
		return elementData[index];
	}
	public char set(int index, char value) {
		rangeCheck(index);
		final char old = elementData[index];
		elementData[index] = value;
		return old;
	}
	public boolean add(char value) {
		ensureCapacity(size + 1);
		elementData[size++] = value;
		return true;
	}
	public void add(int index, char element) {
		rangeCheckForAdd(index);
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = element;
		size++;
	}
	public char remove(int index) {
		rangeCheck(index);
		final char old = elementData[index];
		final int moved = size - index - 1;
		if(moved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, moved);
		}
		--size;
		return old;
	}
	public boolean delete(char value) {
		for(int i = 0; i < size; ++i) {
			if(elementData[i] == value) {
				final int moved = size - i - 1;
				if(moved > 0) {
					System.arraycopy(elementData, i + 1, elementData, i, moved);
				}
				--size;
				return true;
			}
		}
		return false;
	}
	public void clear() {
		size = 0;
	}
	public boolean addAll(char[] a) {
		if(a.length == 0)
			return false;
		ensureCapacity(size + a.length);
		System.arraycopy(a, 0, elementData, size, a.length);
		size += a.length;
		return true;
	}
	public boolean addAll(FastCharArrayTable c) {
		return addAll(c.toArray());
	}
	public boolean addAll(int index, char[] a) {
		rangeCheckForAdd(index);
		if(a.length == 0)
			return false;
		ensureCapacity(size + a.length);
		final int moved = size - index;
		if(moved > 0) {
			System.arraycopy(elementData, index, elementData, index + a.length, moved);
		}
		System.arraycopy(a, 0, elementData, index, a.length);
		size += a.length;
		return true;
	}
	public boolean addAll(int index, FastCharArrayTable c) {
		if(c.size == 0)
			return false;
		ensureCapacity(size + c.size);
		System.arraycopy(c.elementData, 0, elementData, size, c.size);
		size += c.size;
		return true;
	}
	protected void removeRange(int from, int to) {
		System.arraycopy(elementData, to, elementData, from, size - to);
		size -= to - from;
	}
	private void rangeCheck(int i) {
		if(i < 0 || i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
	}
	private void rangeCheckForAdd(int i) {
		if(i < 0 || i > size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
	}
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(elementData.length);
		for(int i = 0; i < size; ++i) {
			s.writeChar(elementData[i]);
		}
	}
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		elementData = new char[s.readInt()];
		for(int i = 0; i < size; ++i) {
			elementData[i] = s.readChar();
		}
	}
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		final FastCharArrayTable that = (FastCharArrayTable) o;
		if(size != that.size)
			return false;
		for(int i = 0; i < size; ++i)
			if(elementData[i] != that.elementData[i])
				return false;
		return true;
	}
	public int hashCode() {
		int h = 1;
		for(int i = 0; i < size; ++i) {
			h = 31 * h + elementData[i];
		}
		return h;
	}
	public String toString() {
		if(size == 0)
			return "[]";
		final StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(int i = 0; i < size; ++i) {
			if(i > 0) {
				sb.append(", ");
			}
			sb.append(elementData[i]);
		}
		return sb.append(']').toString();
	}
}