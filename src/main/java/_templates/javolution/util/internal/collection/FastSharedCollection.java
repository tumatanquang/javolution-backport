/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2012 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package _templates.javolution.util.internal.collection;
import _templates.java.util.Collection;
import _templates.java.util.Iterator;
import _templates.java.util.List;
import _templates.java.util.ListIterator;
import _templates.javolution.lang.Reusable;
import _templates.javolution.util.FastAbstractList;
import _templates.javolution.util.concurrent.locks.Lock;
import _templates.javolution.util.concurrent.locks.ReadWriteLock;
import _templates.javolution.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * A shared view over a collection (reads-write locks).
 */
public final class FastSharedCollection extends FastAbstractList implements List, Reusable {
	private final FastAbstractList list;
	private final ReadWriteLock lock;
	public FastSharedCollection(FastAbstractList inner) {
		list = inner;
		lock = new ReentrantReadWriteLock();
	}
	public boolean add(Object o) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.add(o);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean remove(Object o) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.remove(o);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public void add(int index, Object element) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						list.add(index, element);
					}
					finally {
						w.release();
					}
					break;
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean addAll(Collection c) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.addAll(c);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean addAll(int index, Collection c) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.addAll(index, c);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public void clear() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						list.clear();
					}
					finally {
						w.release();
					}
					break;
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean contains(Object o) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.contains(o);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean containsAll(Collection c) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.containsAll(c);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public void delete(Record record) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						list.delete(record);
					}
					finally {
						w.release();
					}
					break;
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Object get(int index) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.get(index);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Record head() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.head();
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public int indexOf(Object o) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.indexOf(o);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean isEmpty() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.isEmpty();
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Iterator iterator() {
		return list.iterator(); // Must be manually synched by user!
	}
	public int lastIndexOf(Object o) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.lastIndexOf(o);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public ListIterator listIterator() {
		return list.listIterator(); // Must be manually synched by user!
	}
	public ListIterator listIterator(int index) {
		return list.listIterator(index); // Must be manually synched by user!
	}
	public Object remove(int index) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.remove(index);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean removeAll(Collection c) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.removeAll(c);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public void reset() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						list.reset();
					}
					finally {
						w.release();
					}
					break;
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public boolean retainAll(Collection c) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.retainAll(c);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Object set(int index, Object element) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.set(index, element);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public int size() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.size();
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public List subList(int fromIndex, int toIndex) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock w = lock.writeLock();
			for(;;) {
				try {
					w.acquire();
					try {
						return list.subList(fromIndex, toIndex);
					}
					finally {
						w.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Record tail() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.tail();
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Object[] toArray() {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.toArray();
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Object[] toArray(Object[] array) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.toArray(array);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	public Object valueOf(Record record) {
		boolean wasInterrupted = Thread.interrupted();
		try {
			final Lock r = lock.readLock();
			for(;;) {
				try {
					r.acquire();
					try {
						return list.valueOf(record);
					}
					finally {
						r.release();
					}
				}
				catch(final InterruptedException ex) {
					wasInterrupted = true;
				}
			}
		}
		finally {
			if(wasInterrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
}