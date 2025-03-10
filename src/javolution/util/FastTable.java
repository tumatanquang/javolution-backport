/*
 * Javolution - Java(TM) Solution for Real-Time and Embedded Systems
 * Copyright (C) 2005 - Javolution (http://javolution.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javolution.util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import javax.realtime.MemoryArea;
import javolution.context.ObjectFactory;
import javolution.context.PersistentContext;
import javolution.lang.MathLib;
import javolution.lang.Reusable;
import javolution.util.internal.FastComparator;
/**
 * <p> This class represents a random access collection with real-time behavior
 *     (smooth capacity increase).</p>
 *     <img src="doc-files/list-add.png"/>
 *
 * <p> This class has the following advantages over the widely used
 *     <code>java.util.ArrayList</code>:<ul>
 *     <li> No large array allocation (for large collections multi-dimensional
 *          arrays are employed). The garbage collector is not stressed with
 *          large chunk of memory to allocate (likely to trigger a
 *          full garbage collection due to memory fragmentation).</li>
 *     <li> Support concurrent access/iteration/modification without synchronization
 *          if marked {@link FastCollection#shared shared}. </li>
 *     </ul></p>
 *
 *  <p> Iterations over the {@link FastTable} values are faster when
 *      performed using the {@link #get} method rather than using collection
 *      records or iterators:[code]
 *     for (int i = 0, n = table.size(); i < n; i++) {
 *          table.get(i);
 *     }[/code]</p>
 *
 *  <p> {@link FastTable} supports {@link #sort sorting} in place (quick sort)
 *      using the {@link FastCollection#getValueComparator() value comparator}
 *      for the table (no object or array allocation when sorting).</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.4.5, August 20, 2007
 */
public class FastTable<E> extends FastList<E> implements List<E>, Reusable, RandomAccess {
	private static final long serialVersionUID = -2258186618781046936L;
	/**
	 * Holds the factory for this fast table.
	 */
	private static final ObjectFactory FACTORY = new ObjectFactory() {
		@Override
		public Object create() {
			return new FastTable();
		}
	};
	// We do a full resize (and copy) only when the capacity is less than C1.
	// For large collections, multi-dimensional arrays are employed.
	private static final int B0 = 4; // Initial capacity in bits.
	private static final int C0 = 1 << B0; // Initial capacity (16)
	private static final int B1 = 10; // Low array maximum capacity in bits.
	private static final int C1 = 1 << B1; // Low array maximum capacity (1024).
	private static final int M1 = C1 - 1; // Mask.
	private static final Object[] NULL_BLOCK = new Object[C1];
	// Resizes up to 1024 maximum (16, 32, 64, 128, 256, 512, 1024).
	private transient E[] _low;
	// For larger capacity use multi-dimensional array.
	private transient E[][] _high;
	/**
	 * Holds the current capacity.
	 */
	private transient int _capacity;
	/**
	 * Holds the current size. Volatility ensures that when elements are
	 * added
	 */
	private transient int _size;
	/**
	 * Holds the value comparator.
	 */
	private transient FastComparator<? super E> _valueComparator = FastComparator.DEFAULT;
	/**
	 * Creates a table of small initial capacity.
	 */
	public FastTable() {
		_capacity = C0;
		_low = (E[]) new Object[C0];
		_high = (E[][]) new Object[1][];
		_high[0] = _low;
	}
	/**
	 * Creates a persistent table associated to the specified unique identifier
	 * (convenience method).
	 *
	 * @param id the unique identifier for this map.
	 * @throws IllegalArgumentException if the identifier is not unique.
	 * @see javolution.context.PersistentContext.Reference
	 */
	public FastTable(String id) {
		this();
		new PersistentContext.Reference(id, this) {
			@Override
			protected void notifyChange() {
				FastTable.this.clear();
				FastTable.this.addAll((FastSequence) this.get());
			}
		};
	}
	/**
	 * Creates a table of specified initial capacity; unless the table size
	 * reaches the specified capacity, operations on this table will not
	 * allocate memory (no lazy object creation).
	 *
	 * @param capacity the initial capacity.
	 */
	public FastTable(int capacity) {
		this();
		while(capacity > _capacity) {
			increaseCapacity();
		}
	}
	/**
	 * Creates a table containing the specified values, in the order they
	 * are returned by the collection's iterator.
	 *
	 * @param values the values to be placed into this table.
	 */
	public FastTable(Collection<? extends E> values) {
		this(values.size());
		addAll(values);
	}
	/**
	 * Returns a new, preallocated or {@link #recycle recycled} table instance
	 * (on the stack when executing in a {@link javolution.context.StackContext
	 * StackContext}).
	 *
	 * @return a new, preallocated or recycled table instance.
	 */
	public static <E> FastTable<E> newInstance() {
		return (FastTable<E>) FACTORY.object();
	}
	/**
	 * Recycles a table {@link #newInstance() instance} immediately
	 * (on the stack when executing in a {@link javolution.context.StackContext
	 * StackContext}).
	 */
	public static void recycle(FastTable instance) {
		FACTORY.recycle(instance);
	}
	// Implements FastCollection abstract method.
	@Override
	public int size() {
		return _size;
	}
	// Overrides.
	@Override
	public boolean isEmpty() {
		return _size == 0;
	}
	// Overrides (optimization).
	@Override
	public boolean contains(Object value) {
		return indexOf(value) >= 0;
	}
	/**
	 * Returns an iterator over the elements in this list
	 * (allocated on the stack when executed in a
	 * {@link javolution.context.StackContext StackContext}).
	 *
	 * @return an iterator over this list values.
	 */
	@Override
	public Iterator<E> iterator() {
		return listIterator();
	}
	/**
	 * Appends the specified value to the end of this table.
	 *
	 * @param o the value to be appended to this table.
	 * @return <code>true</code> (as per the general contract of the
	 *         <code>Collection.add</code> method).
	 */
	@Override
	public boolean add(E o) {
		if(_size >= _capacity) {
			increaseCapacity();
		}
		_high[_size >> B1][_size & M1] = o;
		++_size;
		return true;
	}
	// Overrides.
	@Override
	public boolean remove(Object o) {
		final int index = indexOf(o);
		if(index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}
	/**
	 * Appends all of the elements in the specified collection to the end of
	 * this list, in the order that they are returned by the
	 * specified collection's Iterator.  The behavior of this operation is
	 * undefined if the specified collection is modified while the operation
	 * is in progress.  (This implies that the behavior of this call is
	 * undefined if the specified collection is this list, and this
	 * list is nonempty.)
	 *
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws NullPointerException if the specified collection is null
	 */
	@Override
	public boolean addAll(Collection<? extends E> values) {
		return addAll(_size, values);
	}
	/**
	 * Inserts all of the values in the specified collection into this
	 * table at the specified position. Shifts the value currently at that
	 * position (if any) and any subsequent values to the right
	 * (increases their indices).
	 *
	 * <p>Note: If this method is used concurrent access must be synchronized
	 *          (the table is no more thread-safe).</p>
	 *
	 * @param index the index at which to insert first value from the specified
	 *        collection.
	 * @param values the values to be inserted into this list.
	 * @return <code>true</code> if this list changed as a result of the call;
	 *         <code>false</code> otherwise.
	 * @throws IndexOutOfBoundsException if <code>(index < 0) ||
	 *         (index > size())</code>
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> values) {
		if(index < 0 || index > _size)
			throw new IndexOutOfBoundsException("index: " + index);
		final int shift = values.size();
		shiftRight(index, shift);
		final Iterator<? extends E> valuesIterator = values.iterator();
		for(int i = index, n = index + shift; i < n; ++i) {
			_high[i >> B1][i & M1] = valuesIterator.next();
		}
		_size += shift; // Increases size last (thread-safe)
		return shift != 0;
	}
	// Overrides.
	@Override
	public void clear() {
		for(int i = 0; i < _size; i += C1) {
			final int count = MathLib.min(_size - i, C1);
			final E[] low = _high[i >> B1];
			System.arraycopy(NULL_BLOCK, 0, low, 0, count);
		}
		_size = 0; // No need for volatile, removal are not thread-safe.
	}
	/**
	 * Returns the element at the specified index.
	 *
	 * @param index index of value to return.
	 * @return the value at the specified position in this list.
	 * @throws IndexOutOfBoundsException if <code>(index < 0) ||
	 *         (index >= size())</code>
	 */
	@Override
	public E get(int index) { // Short to be inlined.
		if(index >= _size)
			throw new IndexOutOfBoundsException();
		return index < C1 ? _low[index] : _high[index >> B1][index & M1];
	}
	/**
	 * Replaces the value at the specified position in this table with the
	 * specified value.
	 *
	 * @param index index of value to replace.
	 * @param value value to be stored at the specified position.
	 * @return previous value.
	 * @throws IndexOutOfBoundsException if <code>(index < 0) ||
	 *         (index >= size())</code>
	 */
	@Override
	public E set(int index, E value) {
		if(index >= _size)
			throw new IndexOutOfBoundsException();
		final E[] low = _high[index >> B1];
		final E previous = low[index & M1];
		low[index & M1] = value;
		return previous;
	}
	/**
	 * Inserts the specified value at the specified position in this table.
	 * Shifts the value currently at that position
	 * (if any) and any subsequent values to the right (adds one to their
	 * indices).
	 *
	 * <p>Note: If this method is used concurrent access must be synchronized
	 *          (the table is no more thread-safe).</p>
	 *
	 * @param index the index at which the specified value is to be inserted.
	 * @param value the value to be inserted.
	 * @throws IndexOutOfBoundsException if <code>(index < 0) ||
	 *         (index > size())</code>
	 */
	@Override
	public void add(int index, E value) {
		if(index < 0 || index > _size)
			throw new IndexOutOfBoundsException("index: " + index);
		shiftRight(index, 1);
		_high[index >> B1][index & M1] = value;
		++_size;
	}
	/**
	 * Removes the value at the specified position from this table.
	 * Shifts any subsequent values to the left (subtracts one
	 * from their indices). Returns the value that was removed from the
	 * table.
	 *
	 * <p>Note: If this method is used concurrent access must be synchronized
	 *          (the table is no more thread-safe).</p>
	 *
	 * @param index the index of the value to removed.
	 * @return the value previously at the specified position.
	 * @throws IndexOutOfBoundsException if <code>(index < 0) ||
	 *         (index >= size())</code>
	 */
	@Override
	public E remove(int index) {
		final E previous = get(index);
		shiftLeft(index + 1, 1);
		--_size; // No need for volatile, removal are not thread-safe.
		_high[_size >> B1][_size & M1] = null; // Deallocates for GC.
		return previous;
	}
	/**
	 * Returns the index in this table of the first occurrence of the specified
	 * value, or -1 if this table does not contain this value.
	 *
	 * @param o the value to search for.
	 * @return the index in this table of the first occurrence of the specified
	 *         value, or -1 if this table does not contain this value.
	 */
	@Override
	public int indexOf(Object o) {
		final FastComparator comp = this.getValueComparator();
		for(int i = 0; i < _size;) {
			final E[] low = _high[i >> B1];
			final int count = MathLib.min(low.length, _size - i);
			for(int j = -1; ++j < count;) {
				if(comp == FastComparator.DEFAULT ? defaultEquals(o, low[j]) : comp.areEqual(o, low[j]))
					return i + j;
			}
			i += count;
		}
		return -1;
	}
	/**
	 * Returns the index in this table of the last occurrence of the specified
	 * value, or -1 if this table does not contain this value.
	 *
	 * @param o the value to search for.
	 * @return the index in this table of the last occurrence of the specified
	 *         value, or -1 if this table does not contain this value.
	 */
	@Override
	public int lastIndexOf(Object o) {
		final FastComparator comp = this.getValueComparator();
		for(int i = _size - 1; i >= 0;) {
			final E[] low = _high[i >> B1];
			final int count = (i & M1) + 1;
			for(int j = count; --j >= 0;) {
				if(comp == FastComparator.DEFAULT ? defaultEquals(o, low[j]) : comp.areEqual(o, low[j]))
					return i + j - count + 1;
			}
			i -= count;
		}
		return -1;
	}
	/**
	 * Returns a list iterator over the elements in this list
	 * (allocated on the stack when executed in a
	 * {@link javolution.context.StackContext StackContext}).
	 *
	 * @return an iterator over this list values.
	 */
	@Override
	public ListIterator<E> listIterator() {
		return FastTableIterator.valueOf(this, 0, 0, _size);
	}
	/**
	 * Returns a list iterator from the specified position
	 * (allocated on the stack when executed in a
	 * {@link javolution.context.StackContext StackContext}).
	 * The list iterator being returned does not support insertion/deletion.
	 *
	 * @param index the index of first value to be returned from the
	 *        list iterator (by a call to the <code>next</code> method).
	 * @return a list iterator of the values in this table
	 *         starting at the specified position in this list.
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *         [code](index < 0 || index > size())[/code]
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		if(index < 0 || index > _size)
			throw new IndexOutOfBoundsException();
		return FastTableIterator.valueOf(this, index, 0, _size);
	}
	/**
	 * Returns a view of the portion of this list between the specified
	 * indexes (instance of {@link FastSequence} allocated from the "stack" when
	 * executing in a {@link javolution.context.StackContext StackContext}).
	 * If the specified indexes are equal, the returned list is empty.
	 * The returned list is backed by this list, so non-structural changes in
	 * the returned list are reflected in this list, and vice-versa.
	 *
	 * This method eliminates the need for explicit range operations (of
	 * the sort that commonly exist for arrays). Any operation that expects
	 * a list can be used as a range operation by passing a subList view
	 * instead of a whole list.  For example, the following idiom
	 * removes a range of values from a list: [code]
	 * list.subList(from, to).clear();[/code]
	 * Similar idioms may be constructed for <code>indexOf</code> and
	 * <code>lastIndexOf</code>, and all of the algorithms in the
	 * <code>Collections</code> class can be applied to a subList.
	 *
	 * The semantics of the list returned by this method become undefined if
	 * the backing list (i.e., this list) is <i>structurally modified</i> in
	 * any way other than via the returned list (structural modifications are
	 * those that change the size of this list, or otherwise perturb it in such
	 * a fashion that iterations in progress may yield incorrect results).
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList.
	 * @param toIndex high endpoint (exclusive) of the subList.
	 * @return a view of the specified range within this list.
	 *
	 * @throws IndexOutOfBoundsException if [code](fromIndex < 0 ||
	 *          toIndex > size || fromIndex > toIndex)[/code]
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if(fromIndex < 0 || toIndex > _size || fromIndex > toIndex)
			throw new IndexOutOfBoundsException(
					"fromIndex: " + fromIndex + ", toIndex: " + toIndex + " for list of size: " + _size);
		return SubTable.valueOf(this, fromIndex, toIndex - fromIndex);
	}
	// Implements FastCollection abstract method.
	@Override
	public Record head() {
		return Index.valueOf(-1);
	}
	// Implements FastCollection abstract method.
	@Override
	public Record tail() {
		return Index.valueOf(_size);
	}
	// Implements FastCollection abstract method.
	@Override
	public E valueOf(Record record) {
		return get(((Index) record).intValue());
	}
	// Implements FastCollection abstract method.
	@Override
	public void delete(Record record) {
		remove(((Index) record).intValue());
	}
	/**
	 * Sets the size of this table. If the specified size is greater than
	 * the current size then <code>null</code> elements are added; otherwise
	 * the last elements are removed until the desired size is reached.
	 *
	 * @param size the new size.
	 */
	public void setSize(int size) {
		while(_size < size) { // Adds null elements.
			addLast(null);
		}
		while(_size > size) { // Removes last elements.
			removeLast();
		}
	}
	/**
	 * Returns the first value of this table.
	 *
	 * @return this table first value.
	 * @throws NoSuchElementException if this table is empty.
	 */
	public E getFirst() {
		if(_size == 0)
			throw new NoSuchElementException();
		return _low[0];
	}
	/**
	 * Returns the last value of this table.
	 *
	 * @return this table last value.
	 * @throws NoSuchElementException if this table is empty.
	 */
	public E getLast() {
		if(_size == 0)
			throw new NoSuchElementException();
		return get(_size - 1);
	}
	/**
	 * Appends the specified value to the end of this table <i>(fast)</i>.
	 *
	 * @param value the value to be added.
	 */
	public void addLast(E value) {
		add(value);
	}
	/**
	 * Removes and returns the last value of this table <i>(fast)</i>.
	 *
	 * @return this table's last value before this call.
	 * @throws NoSuchElementException if this table is empty.
	 */
	public E removeLast() {
		if(_size == 0)
			throw new NoSuchElementException();
		--_size; // No need for volatile, removal are not thread-safe.
		final E[] low = _high[_size >> B1];
		final E previous = low[_size & M1];
		low[_size & M1] = null;
		return previous;
	}
	// Implements Reusable interface.
	public void reset() {
		clear();
		this.setValueComparator(FastComparator.DEFAULT);
	}
	/**
	 * Removes the values between <code>[fromIndex..toIndex[<code> from
	 * this table.
	 *
	 * <p>Note: If this method is used concurrent access must be synchronized
	 *          (the table is no more thread-safe).</p>
	 *
	 * @param fromIndex the beginning index, inclusive.
	 * @param toIndex the ending index, exclusive.
	 * @throws IndexOutOfBoundsException if <code>(fromIndex < 0) || (toIndex < 0)
	 *         || (fromIndex > toIndex) || (toIndex > this.size())</code>
	 */
	public void removeRange(int fromIndex, int toIndex) {
		if(fromIndex < 0 || toIndex < 0 || fromIndex > toIndex || toIndex > _size)
			throw new IndexOutOfBoundsException(
					"FastTable removeRange(" + fromIndex + ", " + toIndex + ") index out of bounds, size: " + _size);
		final int shift = toIndex - fromIndex;
		shiftLeft(toIndex, shift);
		_size -= shift; // No need for volatile, removal are not thread-safe.
		for(int i = _size, n = _size + shift; i < n; ++i) {
			_high[i >> B1][i & M1] = null; // Deallocates for GC.
		}
	}
	/**
	 * Reduces the capacity of this table to the current size (minimize
	 * storage space).
	 */
	public void trimToSize() {
		while(_capacity - _size > C1) {
			_capacity -= C1;
			_high[_capacity >> B1] = null;
		}
	}
	/**
	 * Sorts this table in place (quick sort) using this table
	 * {@link FastCollection#getValueComparator() value comparator}
	 * (smallest first).
	 *
	 * @return <code>this</code>
	 */
	public FastTable<E> sort() {
		if(_size > 1) {
			quicksort(0, _size - 1, this.getValueComparator());
		}
		return this;
	}
	/*
	 * From Wikipedia Quick Sort - http://en.wikipedia.org/wiki/Quicksort
	 */
	private void quicksort(int first, int last, FastComparator cmp) {
		int pivIndex = 0;
		if(first < last) {
			pivIndex = partition(first, last, cmp);
			quicksort(first, pivIndex - 1, cmp);
			quicksort(pivIndex + 1, last, cmp);
		}
	}
	private int partition(int f, int l, FastComparator cmp) {
		final E piv = get(f);
		int up = f, down = l;
		do {
			while(cmp.compare(get(up), piv) <= 0 && up < l) {
				++up;
			}
			while(cmp.compare(get(down), piv) > 0 && down > f) {
				--down;
			}
			if(up < down) { // Swaps.
				E temp = get(up);
				set(up, get(down));
				set(down, temp);
			}
		}
		while(down > up);
		set(f, get(down));
		set(down, piv);
		return down;
	}
	/**
	 * Sets the comparator to use for value equality or comparison if the
	 * collection is ordered (see {@link #sort()}).
	 *
	 * @param comparator the value comparator.
	 * @return <code>this</code>
	 */
	public FastTable<E> setValueComparator(FastComparator<? super E> comparator) {
		_valueComparator = comparator;
		return this;
	}
	// Overrides.
	@Override
	public FastComparator<? super E> getValueComparator() {
		return _valueComparator;
	}
	// Overrides  to return a list (JDK1.5+).
	@Override
	public FastTable<E> unmodifiable() {
		return new FastTable<E>(super.unmodifiable());
	}
	// Overrides  to return a list (JDK1.5+).
	@Override
	public FastTable<E> shared() {
		return new FastTable<E>(super.shared());
	}
	// Requires special handling during de-serialization process.
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setValueComparator((FastComparator) stream.readObject());
		final int size = stream.readInt();
		_capacity = C0;
		while(_capacity < _size && _capacity < C1) {
			_capacity <<= 1; // Increases capacity up to C1 to avoid resizes.
		}
		_low = (E[]) new Object[_capacity];
		_high = (E[][]) new Object[1][];
		_high[0] = _low;
		for(int i = -1; ++i < size;) {
			addLast((E) stream.readObject());
		}
	}
	// Requires special handling during serialization process.
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeObject(getValueComparator());
		final int size = _size;
		stream.writeInt(size);
		for(int i = -1; ++i < size;) {
			stream.writeObject(get(i));
		}
	}
	/**
	 * Returns the current capacity of this table.
	 *
	 * @return this table's capacity.
	 */
	protected final int getCapacity() {
		return _capacity;
	}
	/**
	 * Increases this table capacity.
	 */
	private void increaseCapacity() {
		MemoryArea.getMemoryArea(this).executeInArea(new Runnable() {
			public void run() {
				if(_capacity < C1) { // For small capacity, resize.
					_capacity <<= 1;
					final E[] tmp = (E[]) new Object[_capacity];
					System.arraycopy(_low, 0, tmp, 0, _size);
					_low = tmp;
					_high[0] = tmp;
				}
				else { // Add a new low block of 1024 elements.
					int j = _capacity >> B1;
					if(j >= _high.length) { // Resizes _high.
						final E[][] tmp = (E[][]) new Object[_high.length << 1][];
						System.arraycopy(_high, 0, tmp, 0, _high.length);
						_high = tmp;
					}
					_high[j] = (E[]) new Object[C1];
					_capacity += C1;
				}
			}
		});
	}
	/*private static final class Shared<E> extends FastTable<E> implements Collection<E>, Serializable {
		private final FastTable<E> list; // Backing FastTable
		private final Object mutex; // Object on which to synchronize
		private Shared(FastTable<E> target) {
			if(target == null)
				throw new NullPointerException();
			list = target;
			mutex = this;
		}
		@Override
		public void setSize(int size) {
			synchronized(mutex) {
				list.setSize(size);
			}
		}
		@Override
		public E get(int index) {
			synchronized(mutex) {
				return list.get(index);
			}
		}
		@Override
		public E set(int index, E value) {
			synchronized(mutex) {
				return list.set(index, value);
			}
		}
		@Override
		public boolean add(E o) {
			synchronized(mutex) {
				return list.add(o);
			}
		}
		@Override
		public E getFirst() {
			synchronized(mutex) {
				return list.getFirst();
			}
		}
		@Override
		public E getLast() {
			synchronized(mutex) {
				return list.getLast();
			}
		}
		@Override
		public void addLast(E value) {
			synchronized(mutex) {
				list.addLast(value);
			}
		}
		@Override
		public E removeLast() {
			synchronized(mutex) {
				return list.removeLast();
			}
		}
		@Override
		public void clear() {
			synchronized(mutex) {
				list.clear();
			}
		}
		@Override
		public boolean isEmpty() {
			synchronized(mutex) {
				return list.isEmpty();
			}
		}
		@Override
		public void reset() {
			synchronized(mutex) {
				list.reset();
			}
		}
		@Override
		public boolean addAll(Collection<? extends E> c) {
			synchronized(mutex) {
				return list.addAll(c);
			}
		}
		@Override
		public void add(int index, E value) {
			synchronized(mutex) {
				list.add(index, value);
			}
		}
		@Override
		public E remove(int index) {
			synchronized(mutex) {
				return list.remove(index);
			}
		}
		@Override
		public void removeRange(int fromIndex, int toIndex) {
			synchronized(mutex) {
				list.removeRange(fromIndex, toIndex);
			}
		}
		@Override
		public int indexOf(Object value) {
			synchronized(mutex) {
				return list.indexOf(value);
			}
		}
		@Override
		public int lastIndexOf(Object value) {
			synchronized(mutex) {
				return list.lastIndexOf(value);
			}
		}
		@Override
		public Iterator<E> iterator() {
			return list.iterator(); // Must be manually synched by user!
		}
		@Override
		public ListIterator<E> listIterator() {
			return list.listIterator(); // Must be manually synched by user!
		}
		@Override
		public ListIterator<E> listIterator(int index) {
			return list.listIterator(index); // Must be manually synched by user!
		}
		@Override
		public List<E> subList(int fromIndex, int toIndex) {
			synchronized(mutex) {
				return list.subList(fromIndex, toIndex);
			}
		}
		@Override
		public void trimToSize() {
			synchronized(mutex) {
				list.trimToSize();
			}
		}
		@Override
		public FastTable<E> sort() {
			synchronized(mutex) {
				return list.sort();
			}
		}
		@Override
		public FastTable<E> setValueComparator(FastComparator<? super E> comparator) {
			synchronized(mutex) {
				return list.setValueComparator(comparator);
			}
		}
		@Override
		public FastComparator<? super E> getValueComparator() {
			synchronized(mutex) {
				return list.getValueComparator();
			}
		}
		@Override
		public int size() {
			synchronized(mutex) {
				return list.size();
			}
		}
		@Override
		public boolean contains(Object o) {
			synchronized(mutex) {
				return list.contains(o);
			}
		}
		@Override
		public Object[] toArray() {
			synchronized(mutex) {
				return list.toArray();
			}
		}
		@Override
		public <T> T[] toArray(T[] a) {
			synchronized(mutex) {
				return list.toArray(a);
			}
		}
		@Override
		public boolean remove(Object o) {
			synchronized(mutex) {
				return list.remove(o);
			}
		}
		@Override
		public boolean containsAll(Collection<?> coll) {
			synchronized(mutex) {
				return list.containsAll(coll);
			}
		}
		@Override
		public boolean removeAll(Collection<?> coll) {
			synchronized(mutex) {
				return list.removeAll(coll);
			}
		}
		@Override
		public boolean retainAll(Collection<?> coll) {
			synchronized(mutex) {
				return list.retainAll(coll);
			}
		}
		@Override
		public String toString() {
			synchronized(mutex) {
				return list.toString();
			}
		}
	}*/
	/**
	* This inner class implements a sub-table.
	*/
	private static final class SubTable extends FastCollection implements List, RandomAccess {
		private static final long serialVersionUID = 8961471037048267243L;
		private static final ObjectFactory FACTORY = new ObjectFactory() {
			@Override
			protected Object create() {
				return new SubTable();
			}
			@Override
			protected void cleanup(Object obj) {
				final SubTable st = (SubTable) obj;
				st._table = null;
			}
		};
		private FastTable _table;
		private int _offset;
		private int _size;
		public static SubTable valueOf(FastTable table, int offset, int size) {
			final SubTable subTable = (SubTable) FACTORY.object();
			subTable._table = table;
			subTable._offset = offset;
			subTable._size = size;
			return subTable;
		}
		@Override
		public int size() {
			return _size;
		}
		@Override
		public Record head() {
			return Index.valueOf(-1);
		}
		@Override
		public Record tail() {
			return Index.valueOf(_size);
		}
		@Override
		public Object valueOf(Record record) {
			return _table.get(((Index) record).intValue() + _offset);
		}
		@Override
		public void delete(Record record) {
			throw new UnsupportedOperationException("Deletion not supported, thread-safe collections.");
		}
		public boolean addAll(int index, Collection values) {
			throw new UnsupportedOperationException("Insertion not supported, thread-safe collections.");
		}
		public Object get(int index) {
			if(index < 0 || index >= _size)
				throw new IndexOutOfBoundsException("index: " + index);
			return _table.get(index + _offset);
		}
		public Object set(int index, Object value) {
			if(index < 0 || index >= _size)
				throw new IndexOutOfBoundsException("index: " + index);
			return _table.set(index + _offset, value);
		}
		public void add(int index, Object element) {
			throw new UnsupportedOperationException("Insertion not supported, thread-safe collections.");
		}
		public Object remove(int index) {
			throw new UnsupportedOperationException("Deletion not supported, thread-safe collections.");
		}
		public int indexOf(Object value) {
			final FastComparator comp = _table.getValueComparator();
			for(int i = -1; ++i < _size;) {
				if(comp.areEqual(value, _table.get(i + _offset)))
					return i;
			}
			return -1;
		}
		public int lastIndexOf(Object value) {
			final FastComparator comp = _table.getValueComparator();
			for(int i = _size; --i >= 0;) {
				if(comp.areEqual(value, _table.get(i + _offset)))
					return i;
			}
			return -1;
		}
		public ListIterator listIterator() {
			return listIterator(0);
		}
		public ListIterator listIterator(int index) {
			if(index >= 0 && index <= _size)
				return FastTableIterator.valueOf(_table, index + _offset, _offset, _offset + _size);
			throw new IndexOutOfBoundsException("index: " + index + " for table of size: " + _size);
		}
		public List subList(int fromIndex, int toIndex) {
			if(fromIndex < 0 || toIndex > _size || fromIndex > toIndex)
				throw new IndexOutOfBoundsException(
						"fromIndex: " + fromIndex + ", toIndex: " + toIndex + " for list of size: " + _size);
			return SubTable.valueOf(_table, _offset + fromIndex, toIndex - fromIndex);
		}
	}
	/**
	 * This inner class implements a fast table iterator.
	 */
	private static final class FastTableIterator implements ListIterator {
		private static final ObjectFactory FACTORY = new ObjectFactory() {
			@Override
			protected Object create() {
				return new FastTableIterator();
			}
			@Override
			protected void cleanup(Object obj) {
				final FastTableIterator i = (FastTableIterator) obj;
				i._table = null;
				i._low = null;
				i._high = null;
			}
		};
		private FastTable _table;
		private int _currentIndex;
		private int _start; // Inclusive.
		private int _end; // Exclusive.
		private int _nextIndex;
		private Object[] _low;
		private Object[][] _high;
		public static FastTableIterator valueOf(FastTable table, int nextIndex, int start, int end) {
			final FastTableIterator iterator = (FastTableIterator) FACTORY.object();
			iterator._table = table;
			iterator._start = start;
			iterator._end = end;
			iterator._nextIndex = nextIndex;
			iterator._low = table._low;
			iterator._high = table._high;
			iterator._currentIndex = -1;
			return iterator;
		}
		public boolean hasNext() {
			return _nextIndex != _end;
		}
		public Object next() {
			if(_nextIndex == _end)
				throw new NoSuchElementException();
			final int i = _currentIndex = _nextIndex++;
			return i < C1 ? _low[i] : _high[i >> B1][i & M1];
		}
		public int nextIndex() {
			return _nextIndex;
		}
		public boolean hasPrevious() {
			return _nextIndex != _start;
		}
		public Object previous() {
			if(_nextIndex == _start)
				throw new NoSuchElementException();
			final int i = _currentIndex = --_nextIndex;
			return i < C1 ? _low[i] : _high[i >> B1][i & M1];
		}
		public int previousIndex() {
			return _nextIndex - 1;
		}
		public void add(Object o) {
			_table.add(_nextIndex++, o);
			++_end;
			_currentIndex = -1;
		}
		public void set(Object o) {
			if(_currentIndex >= 0) {
				_table.set(_currentIndex, o);
			}
			else
				throw new IllegalStateException();
		}
		public void remove() {
			if(_currentIndex >= 0) {
				_table.remove(_currentIndex);
				--_end;
				if(_currentIndex < _nextIndex) {
					--_nextIndex;
				}
				_currentIndex = -1;
			}
			else
				throw new IllegalStateException();
		}
	}
	// Shifts element from the specified index to the right (higher indexes).
	private void shiftRight(int index, int shift) {
		while(_size + shift >= _capacity) {
			increaseCapacity();
		}
		for(int i = _size; --i >= index;) {
			final int dest = i + shift;
			_high[dest >> B1][dest & M1] = _high[i >> B1][i & M1];
		}
	}
	// Shifts element from the specified index to the left (lower indexes).
	private void shiftLeft(int index, int shift) {
		for(int i = index; i < _size; ++i) {
			final int dest = i - shift;
			_high[dest >> B1][dest & M1] = _high[i >> B1][i & M1];
		}
	}
	// For inlining of default comparator.
	private static boolean defaultEquals(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1 == o2 || o1.equals(o2);
	}
}