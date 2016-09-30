package examples;

import java.util.Arrays;

public class MyQueue<E> implements Queue<E> {
	// simple array based queue (circular buffer)
	
	private E[] stor = (E[]) new Object[4];
	
	private int in,out,size;

	private void expand(){
		stor = Arrays.copyOf(stor,2*stor.length);
	}

	@Override
	public void enqueue(E o) {
		// TODO Auto-generated method stub

	}

	@Override
	public E dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E head() {
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
