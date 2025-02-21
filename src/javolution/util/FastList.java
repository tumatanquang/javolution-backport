package javolution.util;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import javolution.lang.Reusable;
import javolution.util.internal.collection.SharedCollectionImpl;
import javolution.util.internal.collection.UnmodifiableCollectionImpl;
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
	public abstract void add(int index, E element);
	public abstract boolean addAll(int index, Collection<? extends E> c);
	public abstract E get(int index);
	public abstract int indexOf(Object o);
	public abstract int lastIndexOf(Object o);
	public abstract ListIterator<E> listIterator();
	public abstract ListIterator<E> listIterator(int index);
	public abstract E remove(int index);
	public abstract E set(int index, E element);
	public abstract List<E> subList(int fromIndex, int toIndex);
}