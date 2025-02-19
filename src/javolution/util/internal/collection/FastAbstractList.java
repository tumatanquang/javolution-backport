package javolution.util.internal.collection;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import javolution.lang.Reusable;
public abstract class FastAbstractList<E> extends FastAbstractCollection<E> implements List<E>, Reusable {
	private static final long serialVersionUID = 0x565;
	@Override
	public FastAbstractList<E> shared() {
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