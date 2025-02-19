package javolution.util.internal.collection;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
public abstract class AbstractCollection<E> extends FastCollection<E> implements List<E> {
	private static final long serialVersionUID = 0x564;
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
	@Override
	public abstract int size();
	@Override
	public abstract Record head();
	@Override
	public abstract Record tail();
	@Override
	public abstract E valueOf(Record record);
	@Override
	public abstract void delete(Record record);
}
