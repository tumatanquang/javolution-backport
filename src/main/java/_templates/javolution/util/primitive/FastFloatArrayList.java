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
 * Implement <code>FastArrayList</code> for the <code>float</code> primitive data type.
 * @since 5.7.1
 */
public strictfp class FastFloatArrayList implements Cloneable, RandomAccess, Serializable {
	public static final int DEFAULT_INITIAL_CAPACITY = 10;
	private transient float[] elementData;
	private int size;
	public FastFloatArrayList() {
		this(DEFAULT_INITIAL_CAPACITY);
	}
	public FastFloatArrayList(int initialCapacity) {
		if(initialCapacity < 0)
			throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
		elementData = new float[initialCapacity];
	}
	public FastFloatArrayList(float[] src) {
		if(src == null)
			throw new NullPointerException("source array is null");
		size = src.length;
		elementData = new float[size];
		System.arraycopy(src, 0, elementData, 0, size);
	}
	public void trimToSize() {
		if(size >= elementData.length)
			return;
		final float[] tmp = new float[size];
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
		final float[] tmp = new float[newCap];
		System.arraycopy(elementData, 0, tmp, 0, size);
		elementData = tmp;
	}
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size == 0;
	}
	public boolean contains(float v) {
		return indexOf(v) >= 0;
	}
	public int indexOf(float v) {
		final int bits = Float.floatToIntBits(v);
		for(int i = -1; ++i < size;) {
			if(Float.floatToIntBits(elementData[i]) == bits)
				return i;
		}
		return -1;
	}
	public int lastIndexOf(float v) {
		final int bits = Float.floatToIntBits(v);
		for(int i = size; --i >= 0;) {
			if(Float.floatToIntBits(elementData[i]) == bits)
				return i;
		}
		return -1;
	}
	public Object/*FastFloatArrayList*/ clone() throws CloneNotSupportedException {
		/*@JVM-1.1+@
		if(true) {
			final FastFloatArrayList c = (FastFloatArrayList) super.clone();
			c.elementData = new float[elementData.length];
			System.arraycopy(elementData, 0, c.elementData, 0, size);
			return c;
		}
		/**/
		throw new UnsupportedOperationException("J2ME Not Supported Yet");
	}
	public float[] toArray() {
		final float[] a = new float[size];
		System.arraycopy(elementData, 0, a, 0, size);
		return a;
	}
	public float get(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		return elementData[index];
	}
	public float set(int index, float value) {
		if(index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final float old = elementData[index];
		elementData[index] = value;
		return old;
	}
	public boolean add(float value) {
		ensureCapacity(size + 1);
		elementData[size++] = value;
		return true;
	}
	public void add(int index, float element) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		ensureCapacity(size + 1);
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
		elementData[index] = element;
		++size;
	}
	public float remove(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		final float old = elementData[index];
		final int moved = size - index - 1;
		if(moved > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, moved);
		}
		--size;
		return old;
	}
	public boolean removeElement(float value) {
		final int bits = Float.floatToIntBits(value);
		for(int i = -1; ++i < size;) {
			if(Float.floatToIntBits(elementData[i]) == bits) {
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
	public boolean addAll(float[] a) {
		if(a == null || a.length == 0)
			return false;
		ensureCapacity(size + a.length);
		System.arraycopy(a, 0, elementData, size, a.length);
		size += a.length;
		return true;
	}
	public boolean addAll(int index, float[] a) {
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
	public boolean addAll(FastFloatArrayList c) {
		if(c == null || c.size == 0)
			return false;
		ensureCapacity(size + c.size);
		System.arraycopy(c.elementData, 0, elementData, size, c.size);
		size += c.size;
		return true;
	}
	public boolean addAll(int index, FastFloatArrayList c) {
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
			s.writeFloat(elementData[i]);
		}
	}
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		elementData = new float[s.readInt()];
		for(int i = -1; ++i < size;) {
			elementData[i] = s.readFloat();
		}
	}
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		final FastFloatArrayList that = (FastFloatArrayList) o;
		if(size != that.size)
			return false;
		for(int i = -1; ++i < size;) {
			if(Float.floatToIntBits(elementData[i]) != Float.floatToIntBits(that.elementData[i]))
				return false;
		}
		return true;
	}
	/**
	 * Float.hashCode(): Float.floatToIntBits(value)
	 */
	public int hashCode() {
		int h = 1;
		for(int i = -1; ++i < size;) {
			h = 31 * h + Float.floatToIntBits(elementData[i]);
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