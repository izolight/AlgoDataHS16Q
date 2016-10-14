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
		
		PQNode(K1 key, E1 elem){
			this.key = key;
			this.elem = elem;
		}
	}
	
	private PQNode<K,E>[] heap = new PQNode[100];
	private int size;
	
	private void swap(int i, int k){
		PQNode tmp = heap[i];
		heap[i] = heap[k];
		heap[k] = tmp;
		heap[i].pos=i;
		heap[k].pos=k;
	}
	
	private void upheap(int pos){
		// precondition: heap[1..pos-1] is a heap
		while (pos>1){
			int parent = pos/2;
			if (heap[pos].key.compareTo(heap[parent].key) >= 0) return;
			swap(pos,parent);
			pos = parent;
		}
	}
	
	private void downheap(int pos){
		// precondition: heap[1..size] is a heap
		// with exception of heap[pos] which is assumed too big.
		int left = pos*2;
		while (left <= size){
			int right = left+1;
			int min = left;
			if (right<=size && heap[left].key.compareTo(heap[right].key) > 0 ) min = right;
			if (heap[pos].key.compareTo(heap[min].key) <= 0) return;
			swap(pos,min);
			pos = min;
			left = pos*2;
		}
	}

	
	@Override
	public Locator<K, E> showMin() {
		return heap[1];
	}

	@Override
	public Locator<K, E> removeMin() {
		PQNode ret = heap[1];
		ret.creator = null;
		swap(size,1);
		size--;
		downheap(1);
		return ret;
	}

	@Override
	public Locator<K, E> insert(K key, E element) {
		PQNode <K,E> n= new PQNode<K,E>(key, element); 
		heap[++size] = n;
		n.pos = size;
		upheap(size);
		return heap[size];
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
		return size;
	}

	public static void main(String[] args) {
		MyPriorityQueue<Integer,String> pq = new MyPriorityQueue<>();
		pq.insert(6,"erstes");
		pq.insert(2,"zweites");
		pq.insert(4,"drittes");
		pq.insert(1,"drittes");
		pq.insert(7,"drittes");		
		System.out.println(pq.removeMin().key());
		System.out.println(pq.removeMin().key());
		System.out.println(pq.removeMin().key());
		System.out.println(pq.removeMin().key());
		System.out.println(pq.removeMin().key());
	}

}
