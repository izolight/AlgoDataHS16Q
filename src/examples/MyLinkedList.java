package examples;

import java.util.Iterator;

public class MyLinkedList<E> implements List<E> {
	// auxiliary class for the positions
	private class LNode implements Position<E>{
		E elem;
		LNode next,prev;
		Object creator = MyLinkedList.this;
		
		@Override
		public E element() {
			return elem;
		}		
	}
	
	private LNode first,last;
	private int size;
	
	private MyLinkedList<E>.LNode checkAndCast(Position<E> p) {
		LNode n;
		try {
			n = (LNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyLinkedList"); 
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyLinkedList instance!");			
		return n;
	}
	
	@Override
	public Position<E> first() {
		return first;
	}

	@Override
	public Position<E> last() {
		// TODO Auto-generated method stub
		return last;
	}

	@Override
	public boolean isFirst(Position<E> p) {
		return first==checkAndCast(p);
	}

	@Override
	public boolean isLast(Position<E> p) {
		return last==checkAndCast(p);
	}

	@Override
	public Position<E> next(Position<E> p) {
		return checkAndCast(p).next;
	}

	@Override
	public Position<E> previous(Position<E> p) {
		return checkAndCast(p).prev;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		LNode n=checkAndCast(p);
		E ret = n.elem;
		n.elem = o;
		return ret;
	}

	@Override
	public Position<E> insertFirst(E o) {
		LNode n = new LNode();
		n.elem = o;
		n.next = first;
		if (first != null){
			first.prev = n;			
		}
		else {
			last = n;
		}
		first = n;
		size++;	
		return n;
	}

	@Override
	public Position<E> insertLast(E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> insertBefore(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> insertAfter(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position<E> p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<Position<E>> positions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<E> elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
