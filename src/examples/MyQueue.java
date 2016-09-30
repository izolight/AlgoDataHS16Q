package examples;

import java.util.Arrays;

public class MyQueue<E> implements Queue<E> {
	// simple array based queue (circular buffer)
	
	private E[] stor = (E[]) new Object[1];
	
	private int in,out,size;


	
	private void expand(){
		System.out.println("expanding..");
		E[] tmp = stor;
		stor = (E[]) new Object[tmp.length*2];
		for (int i=0;i<size;i++){
			if (out==size) out = 0;
			stor[i] = tmp[out++];
		}
		out=0;
		in=size;
	}

	@Override
	public void enqueue(E o) {
		if (size==stor.length) expand();
		if (in == stor.length) in = 0; // wrap around
		stor[in++]=o;
		size++;
	}

	@Override
	public E dequeue() {
		if (size==0) throw new RuntimeException("empty queue");
		if (out==stor.length) out = 0; // wrap around
		size--;
		return stor[out++];
	}

	@Override
	public E head() {
		if (size == 0) throw new RuntimeException("queue is empty!");
		return stor[out];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	public static void main(String[] args) {
		Queue<Integer> q = new MyQueue<>();
		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		q.enqueue(4);
		q.enqueue(5);
		System.out.println(q.dequeue());
		q.enqueue(6);		
		System.out.println(q.dequeue());
		q.enqueue(7);
		System.out.println(q.dequeue());
		q.enqueue(8);
		System.out.println(q.dequeue());
		q.enqueue(9);
		System.out.println(q.dequeue());
		q.enqueue(10);		
		System.out.println(q.dequeue());
		q.enqueue(11);
		System.out.println(q.dequeue());
		q.enqueue(12);
		System.out.println(q.dequeue());
		q.enqueue(13);
		System.out.println(q.dequeue());
	}

}
