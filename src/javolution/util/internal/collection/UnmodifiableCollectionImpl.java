/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.util.internal.collection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javolution.lang.Reusable;
import javolution.util.FastList;
/**
 * A shared view over a collection (reads-write locks).
 */
public final class UnmodifiableCollectionImpl<E> extends FastList<E> implements List<E>, Reusable {
	private static final long serialVersionUID = 1099576476014476826L;
	private final FastList<E> fc;
	public UnmodifiableCollectionImpl(FastList<E> inner) {
		fc = inner;
	}
	@Override
	public boolean add(E element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean contains(Object value) {
		return fc.contains(value);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return fc.containsAll(c);
	}
	@Override
	public void delete(Record record) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public E get(int index) {
		return fc.get(index);
	}
	@Override
	public Record head() {
		return fc.head();
	}
	@Override
	public int indexOf(Object o) {
		return fc.indexOf(o);
	}
	@Override
	public boolean isEmpty() {
		return fc.isEmpty();
	}
	@Override
	public Iterator<E> iterator() { // Must be manually synched by user!
		return new Iterator<E>() {
			private final Iterator<? extends E> i = fc.iterator();
			public boolean hasNext() {
				return i.hasNext();
			}
			public E next() {
				return i.next();
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	@Override
	public int lastIndexOf(Object o) {
		return fc.lastIndexOf(o);
	}
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("List iterator not supported for unmodifiable collection"); // Must be manually synched by user!
	}
	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("List iterator not supported for unmodifiable collection"); // Must be manually synched by user!
	}
	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean remove(Object value) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	public void reset() {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("Unmodifiable");
	}
	@Override
	public int size() {
		return fc.size();
	}
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Sub-List not supported for unmodifiable collection");
	}
	@Override
	public Record tail() {
		return fc.tail();
	}
	@Override
	public Object[] toArray() {
		return fc.toArray();
	}
	@Override
	public <T> T[] toArray(T[] array) {
		return fc.toArray(array);
	}
	@Override
	public E valueOf(Record record) {
		return fc.valueOf(record);
	}
}