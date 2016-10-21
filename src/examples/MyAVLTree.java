package examples;

import java.util.Iterator;

public class MyAVLTree<K extends Comparable<? super K>, E> implements
		OrderedDictionary<K, E> {

	class AVLNode implements Locator<K,E>{

		AVLNode parent, left, right;
		E elem;
		K key;
		int height;
		Object creator = MyAVLTree.this;
		
		@Override
		public E element() {
			return elem;
		}

		@Override
		public K key() {
			return key;
		}
		
		boolean esExternal(){
			return left == null;
		}
		
		boolean isLeftChild(){
			return parent != null && this == parent.left;
		}
		
		boolean isRightChild(){
			return parent != null && this == parent.right;
		}
		
		void expand(K k, E e){
			key=k;elem=e;
			left=new AVLNode();
			right=new AVLNode();
			left.parent = this;
			right.parent=this;
		}
	}

	private AVLNode root = new AVLNode();
	private int size;
	
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Locator<K, E> find(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E>[] findAll(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E o) {
		return null;
	}

	@Override
	public void remove(Locator<K, E> loc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Locator<K, E> closestBefore(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> closestAfter(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> next(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> min() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> max() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Locator<K, E>> sortedLocators() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
