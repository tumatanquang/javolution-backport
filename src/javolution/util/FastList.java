package javolution.util;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javolution.lang.Reusable;
import javolution.util.internal.collection.SharedCollectionImpl;
import javolution.util.internal.collection.UnmodifiableCollectionImpl;
/**
 * This class represents the skeleton for {@link FastTable} and {@link FastSequence}
 */
public abstract class FastList<E> extends FastCollection<E> implements List<E>, Reusable {
	private static final long serialVersionUID = -3293995916577400839L;
	@Override
	public FastList<E> unmodifiable() {
		return new UnmodifiableCollectionImpl<E>(this);
	}
	@Override
	public FastList<E> shared() {
		return new SharedCollectionImpl<E>(this);
	}
	@Override
	public abstract int size();
	@Override
	public abstract boolean isEmpty();
	@Override
	public abstract boolean contains(Object o);
	@Override
	public abstract Iterator<E> iterator();
	//public abstract Object[] toArray();
	//public abstract <T> T[] toArray(T[] a);
	@Override
	public abstract boolean add(E o);
	@Override
	public abstract boolean remove(Object o);
	//public abstract boolean containsAll(Collection<?> c);
	@Override
	public abstract boolean addAll(Collection<? extends E> c);
	public abstract boolean addAll(int index, Collection<? extends E> c);
	//public abstract boolean removeAll(Collection<?> c);
	//public abstract boolean retainAll(Collection<?> c);
	@Override
	public abstract void clear();
	public abstract E get(int index);
	public abstract E set(int index, E element);
	public abstract void add(int index, E element);
	public abstract E remove(int index);
	public abstract int indexOf(Object o);
	public abstract int lastIndexOf(Object o);
	public abstract ListIterator<E> listIterator();
	public abstract ListIterator<E> listIterator(int index);
	public abstract List<E> subList(int fromIndex, int toIndex);
	@Override
	public abstract Record head();
	@Override
	public abstract Record tail();
	@Override
	public abstract E valueOf(Record record);
	@Override
	public abstract void delete(Record record);
}