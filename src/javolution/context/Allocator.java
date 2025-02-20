/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2006 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.context;
/**
 * <p> This class represents an object allocator; instances of this class
 *     are generated by {@link AllocatorContext}.</p>
 *
 * <p> If an allocator has recycled objects available, those are returned
 *     first, before allocating new ones.</p>
 *
 * <p> Allocator instances are thread-safe without synchronization,
 *     they are the "production lines" of the {@link ObjectFactory factories},
 *     their implementation is derived from the {@link AllocatorContext}
 *     to which they belong (e.g. heap allocators for {@link HeapContext}).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.2, August 14, 2007
 * @see     AllocatorContext#getAllocator(ObjectFactory)
 */
public abstract class Allocator<T> {
	/**
	 * Holds the current user or <code>null</code> if deactivated.
	 */
	protected Thread user;
	/**
	 * Holds the queue of objects belonging to this allocator
	 * (always used first when available).
	 */
	protected T[] queue = (T[]) new Object[16];
	/**
	* Holds the number of objects in this allocator queue.
	*/
	protected int queueSize;
	/**
	 * Default constructor.
	 */
	protected Allocator() {}
	/**
	 * Returns the next available object from this allocator queue or
	 * {@link #allocate} one if none available.
	 *
	 * @return the next available object ready to use.
	 */
	public final T next() {
		return queueSize > 0 ? queue[--queueSize] : allocate();
	}
	/**
	 * Allocates a new object, this method is called when the allocator queue
	 * is empty.
	 *
	 * @return the allocated object.
	 */
	protected abstract T allocate();
	/**
	 * Recycles the specified object to this queue.
	 *
	 * @param object the object to recycle.
	 */
	protected abstract void recycle(T object);
	/**
	 * Resizes this allocator queue (hopefully it is not called too often on the
	 * same allocator as implementation should keep the queue small).
	 */
	void resize() {
		T[] tmp = (T[]) new Object[queue.length << 1];
		System.arraycopy(queue, 0, tmp, 0, queue.length);
		queue = tmp;
	}
}