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
import javolution.util.internal.ReadWriteLockImpl;
/**
 * A shared view over a collection (reads-write locks).
 */
public final class SharedCollectionImpl<E> extends FastList<E> implements List<E>, Reusable {
	private static final long serialVersionUID = -2136254040285582378L;
	private final FastList<E> fc;
	private final ReadWriteLockImpl lock;
	public SharedCollectionImpl(FastList<E> inner) {
		fc = inner;
		lock = new ReadWriteLockImpl();
	}
	@Override
	public boolean add(E o) {
		lock.writeLock.lock();
		try {
			return fc.add(o);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean remove(Object o) {
		lock.writeLock.lock();
		try {
			return fc.remove(o);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public void add(int index, E element) {
		lock.writeLock.lock();
		try {
			fc.add(index, element);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		lock.writeLock.lock();
		try {
			return fc.addAll(c);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		lock.writeLock.lock();
		try {
			return fc.addAll(index, c);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public void clear() {
		lock.writeLock.lock();
		try {
			fc.clear();
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean contains(Object o) {
		lock.readLock.lock();
		try {
			return fc.contains(o);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		lock.readLock.lock();
		try {
			return fc.containsAll(c);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public void delete(Record record) {
		lock.writeLock.lock();
		try {
			fc.delete(record);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public E get(int index) {
		lock.readLock.lock();
		try {
			return fc.get(index);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public Record head() {
		lock.readLock.lock();
		try {
			return fc.head();
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public int indexOf(Object o) {
		lock.readLock.lock();
		try {
			return fc.indexOf(o);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public boolean isEmpty() {
		lock.readLock.lock();
		try {
			return fc.isEmpty();
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public Iterator<E> iterator() {
		return fc.iterator(); // Must be manually synched by user!
	}
	@Override
	public int lastIndexOf(Object o) {
		lock.readLock.lock();
		try {
			return fc.lastIndexOf(o);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public ListIterator<E> listIterator() {
		return fc.listIterator(); // Must be manually synched by user!
	}
	@Override
	public ListIterator<E> listIterator(int index) {
		return fc.listIterator(index); // Must be manually synched by user!
	}
	@Override
	public E remove(int index) {
		lock.writeLock.lock();
		try {
			return fc.remove(index);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		lock.writeLock.lock();
		try {
			return fc.removeAll(c);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	public void reset() {
		lock.writeLock.lock();
		try {
			fc.reset();
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		lock.writeLock.lock();
		try {
			return fc.retainAll(c);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public E set(int index, E element) {
		lock.writeLock.lock();
		try {
			return fc.set(index, element);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public int size() {
		lock.readLock.lock();
		try {
			return fc.size();
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		lock.writeLock.lock();
		try {
			return fc.subList(fromIndex, toIndex);
		}
		finally {
			lock.writeLock.unlock();
		}
	}
	@Override
	public Record tail() {
		lock.readLock.lock();
		try {
			return fc.tail();
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public Object[] toArray() {
		lock.readLock.lock();
		try {
			return fc.toArray();
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public <T> T[] toArray(T[] array) {
		lock.readLock.lock();
		try {
			return fc.toArray(array);
		}
		finally {
			lock.readLock.unlock();
		}
	}
	@Override
	public E valueOf(Record record) {
		lock.readLock.lock();
		try {
			return fc.valueOf(record);
		}
		finally {
			lock.readLock.unlock();
		}
	}
}