package _templates.javolution.util;
import _templates.java.util.Collection;
import _templates.java.util.Iterator;
import _templates.java.util.List;
import _templates.java.util.ListIterator;
import _templates.javolution.lang.Reusable;
import _templates.javolution.util.internal.collection.FastSharedCollection;
import _templates.javolution.util.internal.collection.FastUnmodifiableCollection;
/**
 * This class represents a skeleton for {@link FastTable} and {@link FastList}.
 */
public abstract class FastAbstractList/*<E>*/ extends FastCollection/*<E>*/ implements List/*<E>*/, Reusable {
	public FastCollection/*FastAbstractList<E>*/ unmodifiable() {
		return new FastUnmodifiableCollection(this);
	}
	public FastCollection/*FastAbstractList<E>*/ shared() {
		return new FastSharedCollection(this);
	}
	public abstract int size();
	public abstract boolean isEmpty();
	public abstract boolean contains(Object o);
	public abstract Iterator/*<E>*/ iterator();
	//public abstract Object[] toArray();
	//public abstract <T> T[] toArray(T[] a);
	public abstract boolean add(Object/*{E}*/ o);
	public abstract boolean remove(Object o);
	//public abstract boolean containsAll(Collection<?> c);
	public abstract boolean addAll(Collection/*<? extends E>*/ c);
	public abstract boolean addAll(int index, Collection/*<? extends E>*/ c);
	//public abstract boolean removeAll(Collection<?> c);
	//public abstract boolean retainAll(Collection<?> c);
	public abstract void clear();
	public abstract Object/*{E}*/ get(int index);
	public abstract Object/*{E}*/ set(int index, Object/*{E}*/ element);
	public abstract void add(int index, Object/*{E}*/ element);
	public abstract Object/*{E}*/ remove(int index);
	public abstract int indexOf(Object o);
	public abstract int lastIndexOf(Object o);
	public abstract ListIterator/*<E>*/ listIterator();
	public abstract ListIterator/*<E>*/ listIterator(int index);
	public abstract List/*<E>*/ subList(int fromIndex, int toIndex);
	public abstract Record head();
	public abstract Record tail();
	public abstract Object/*{E}*/ valueOf(Record record);
	public abstract void delete(Record record);
}