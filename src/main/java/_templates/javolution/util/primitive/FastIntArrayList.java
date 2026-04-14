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
 * Implement <code>FastArrayList</code> for the <code>int</code> primitive data type.
 * @since 5.7.1
 */
public class FastIntArrayList implements Cloneable, RandomAccess, Serializable {
	public static final int DEFAULT_INITIAL_CAPACITY = 10;
	private transient int[] elementData;
	private int size;
	public FastIntArrayList() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	public FastIntArrayList(int initialCapacity) {
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
		elementData = new int[initialCapacity];
	}
	public FastIntArrayList(int[] src) {
		if(src == null)
			throw new NullPointerException("source array is null");
		size = src.length;
		elementData = new int[size];
		System.arraycopy(src, 0, elementData, 0, size);
	}
	public void trimToSize() {
		if(size >= elementData.length)
			return;
		final int[] tmp = new int[size];
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
		final int[] tmp = new int[newCap];
		System.arraycopy(elementData, 0, tmp, 0, size);
		elementData = tmp;
	}
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size == 0;
	}
	public boolean contains(int v) {
		return indexOf(v) >= 0;
	}
	public int indexOf(int v) {
		for(int i = -1; ++i < size;) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public int lastIndexOf(int v) {
		for(int i = size; --i >= 0;) {
			if(elementData[i] == v)
				return i;
		}
		return -1;
	}
	public Object/*FastIntArrayList*/ clone() throws CloneNotSupportedException {
		/*@JVM-1.1+@
		if(true) {
			final FastIntArrayList c = (FastIntArrayList) super.clone();
			c.elementData = new int[elementData.length];
			System.arraycopy(elementData, 0, c.elementData, 0, size);
			return c;
		}
		/**/
		throw new UnsupportedOperationException("J2ME Not Supported Yet");
	}
	public int[] toArray() {
		final int[] a = new int[size];
		System.arraycopy(elementData, 0, a, 0, size);
		return a;
	}
	public int get(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		return elementData[index];
	}
	public int set(int index, int value) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final int old = elementData[index];
		elementData[index] = value;
		return old;
	}
	public boolean add(int value) {
		ensureCapacity(size + 1);
		elementData[size++] = value;
		return true;
	}
	public void add(int index, int element) {
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = element;
		++size;
	}
	public int remove(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final int old = elementData[index];
		final int moved = size - index - 1;
		if(moved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, moved);
		}
		--size;
		return old;
	}
	public boolean removeElement(int value) {
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
	public boolean addAll(int[] a) {
		if(a == null || a.length == 0)
			return false;
		ensureCapacity(size + a.length);
		System.arraycopy(a, 0, elementData, size, a.length);
		size += a.length;
		return true;
	}
	public boolean addAll(int index, int[] a) {
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
	public boolean addAll(FastIntArrayList c) {
		if(c == null || c.size == 0)
			return false;
		ensureCapacity(size + c.size);
		System.arraycopy(c.elementData, 0, elementData, size, c.size);
		size += c.size;
		return true;
	}
	public boolean addAll(int index, FastIntArrayList c) {
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
			s.writeInt(elementData[i]);
		}
	}
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		elementData = new int[s.readInt()];
		for(int i = -1; ++i < size;) {
			elementData[i] = s.readInt();
		}
	}
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		final FastIntArrayList that = (FastIntArrayList) o;
		if(size != that.size)
			return false;
		for(int i = -1; ++i < size;) {
			if(elementData[i] != that.elementData[i])
				return false;
		}
		return true;
	}
	public int hashCode() {
		int h = 1;
		for(int i = -1; ++i < size;) {
			h = 31 * h + elementData[i];
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