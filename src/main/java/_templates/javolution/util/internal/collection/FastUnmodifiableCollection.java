/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package _templates.javolution.util.internal.collection;
import _templates.java.lang.UnsupportedOperationException;
import _templates.java.util.Collection;
import _templates.java.util.Iterator;
import _templates.java.util.List;
import _templates.java.util.ListIterator;
import _templates.javolution.lang.Reusable;
import _templates.javolution.util.FastAbstractList;
/**
 * An unmodifiable view over a collection.
 */
public final class FastUnmodifiableCollection extends FastAbstractList implements List, Reusable {
	private final FastAbstractList fc;
	public FastUnmodifiableCollection(FastAbstractList inner) {
		fc = inner;
	}
	public boolean add(Object element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public void add(int index, Object element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean addAll(int index, Collection c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public void clear() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean contains(Object value) {
		return fc.contains(value);
	}
	public boolean containsAll(Collection c) {
		return fc.containsAll(c);
	}
	public void delete(Record record) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public Object get(int index) {
		return fc.get(index);
	}
	public Record head() {
		return fc.head();
	}
	public int indexOf(Object o) {
		return fc.indexOf(o);
	}
	public boolean isEmpty() {
		return fc.isEmpty();
	}
	public Iterator iterator() {
		return new Iterator() {
			private final Iterator i = fc.iterator();
			public boolean hasNext() {
				return i.hasNext();
			}
			public Object next() {
				return i.next();
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	public int lastIndexOf(Object o) {
		return fc.lastIndexOf(o);
	}
	public ListIterator listIterator() {
		throw new UnsupportedOperationException("List iterator not supported for unmodifiable collection"); // Must be manually synched by user!
	}
	public ListIterator listIterator(int index) {
		throw new UnsupportedOperationException("List iterator not supported for unmodifiable collection"); // Must be manually synched by user!
	}
	public Object remove(int index) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean remove(Object value) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public void reset() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public Object set(int index, Object element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public int size() {
		return fc.size();
	}
	public List subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Sub-List not supported for unmodifiable collection");
	}
	public Record tail() {
		return fc.tail();
	}
	public Object[] toArray() {
		return fc.toArray();
	}
	public Object[] toArray(Object[] array) {
		return fc.toArray(array);
	}
	public Object valueOf(Record record) {
		return fc.valueOf(record);
	}
}