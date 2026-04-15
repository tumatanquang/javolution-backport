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
 * Implement <code>FastArrayList</code> for the <code>boolean</code> primitive data type.
 * @since 5.7.1
 */
public class FastBooleanArrayList implements Cloneable, RandomAccess, Serializable {
	public static final int DEFAULT_INITIAL_CAPACITY = 10;
	private transient boolean[] elementData;
	private int size;
	public FastBooleanArrayList() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	public FastBooleanArrayList(int initialCapacity) {
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
		elementData = new boolean[initialCapacity];
	}
	public FastBooleanArrayList(boolean[] src) {
		if(src == null)
			throw new NullPointerException("source array is null");
		size = src.length;
		elementData = new boolean[size];
		System.arraycopy(src, 0, elementData, 0, size);
	}
	public void trimToSize() {
		if(size >= elementData.length)
			return;
		final boolean[] tmp = new boolean[size];
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
		final boolean[] tmp = new boolean[newCap];
		System.arraycopy(elementData, 0, tmp, 0, size);
		elementData = tmp;
	}
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size == 0;
	}
	public boolean contains(boolean v) {
		return indexOf(v) >= 0;
	}
	public int indexOf(boolean v) {
		for(int i = -1; ++i < size;) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public int lastIndexOf(boolean v) {
		for(int i = size; --i >= 0;) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public Object/*FastBooleanArrayList*/ clone() throws CloneNotSupportedException {
		/*@JVM-1.1+@
		if(true) {
			final FastBooleanArrayList c = (FastBooleanArrayList) super.clone();
			c.elementData = new boolean[elementData.length];
			System.arraycopy(elementData, 0, c.elementData, 0, size);
			return c;
		}
		/**/
		throw new UnsupportedOperationException("J2ME Not Supported Yet");
	}
	public boolean[] toArray() {
		final boolean[] a = new boolean[size];
		System.arraycopy(elementData, 0, a, 0, size);
		return a;
	}
	public boolean get(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		return elementData[index];
	}
	public boolean set(int index, boolean value) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final boolean old = elementData[index];
		elementData[index] = value;
		return old;
	}
	public boolean add(boolean value) {
		ensureCapacity(size + 1);
		elementData[size++] = value;
		return true;
	}
	public void add(int index, boolean element) {
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = element;
		++size;
	}
	/**
	 * Returns the <i>removed value</i>, not a success flag.
	 */
	public boolean remove(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final boolean old = elementData[index];
		final int moved = size - index - 1;
		if(moved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, moved);
		}
		--size;
		return old;
	}
	/**
	 * Returns {@code true} if an element was removed.
	 */
	public boolean removeElement(boolean value) {
		for(int i = -1; ++i < size;) {
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
	public boolean addAll(boolean[] a) {
		if(a == null || a.length == 0)
			return false;
		ensureCapacity(size + a.length);
		System.arraycopy(a, 0, elementData, size, a.length);
		size += a.length;
		return true;
	}
	public boolean addAll(int index, boolean[] a) {
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		if(a == null || a.length == 0)
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
	public boolean addAll(FastBooleanArrayList c) {
		if(c == null || c.size == 0)
			return false;
		ensureCapacity(size + c.size);
		System.arraycopy(c.elementData, 0, elementData, size, c.size);
		size += c.size;
		return true;
	}
	public boolean addAll(int index, FastBooleanArrayList c) {
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		if(c == null || c.size == 0)
			return false;
		ensureCapacity(size + c.size);
		final int moved = size - index;
		if(moved > 0) {
			System.arraycopy(elementData, index, elementData, index + c.size, moved);
		}
		System.arraycopy(c.elementData, 0, elementData, index, c.size);
		size += c.size;
		return true;
	}
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(elementData.length);
		for(int i = -1; ++i < size;) {
			s.writeBoolean(elementData[i]);
		}
	}
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		elementData = new boolean[s.readInt()];
		for(int i = -1; ++i < size;) {
			elementData[i] = s.readBoolean();
		}
	}
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		final FastBooleanArrayList that = (FastBooleanArrayList) o;
		if(size != that.size)
			return false;
		for(int i = -1; ++i < size;) {
			if(elementData[i] != that.elementData[i])
				return false;
		}
		return true;
	}
	/**
	 * Boolean.hashCode(): value ? 1231 : 1237
	 */
	public int hashCode() {
		int h = 1;
		for(int i = -1; ++i < size;) {
			h = 31 * h + (elementData[i] ? 1231 : 1237);
		}
		return h;
	}
	public String toString() {
		if(size == 0)
			return "[]";
		final StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(int i = -1; ++i < size;) {
			if(i > 0) {
				sb.append(',').append(' ');
			}
			sb.append(elementData[i]);
		}
		return sb.append(']').toString();
	}
}