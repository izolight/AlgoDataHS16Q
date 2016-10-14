package examples;

public class MyPriorityQueue<K extends Comparable<? super K>, E> implements
		PriorityQueue<K, E> {
	
	class PQNode<K1 extends Comparable<? super K1>, E1> implements Locator<K1,E1>{

		K1 key;
		E1 elem;
		Object creator= MyPriorityQueue.this;
		int pos;
		
		@Override
		public E1 element() {
			return elem;
		}

		@Override
		public K1 key() {
			return key;
		}
		
	}
	
	private PQNode<K,E>[] heap = new PQNode[100];
	private int size;
	
	@Override
	public Locator<K, E> showMin() {
		return heap[1];
	}

	@Override
	public Locator<K, E> removeMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Locator<K, E> loc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void replaceKey(Locator<K, E> loc, K newKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String[] args) {
		MyPriorityQueue<Integer,String> pq = new MyPriorityQueue<>();
		pq.insert(6,"erstes");
		pq.insert(2,"zweites");
		pq.insert(4,"drittes");
		System.out.println(pq.removeMin().key());
	}

}
