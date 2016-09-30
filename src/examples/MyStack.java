package examples;

import java.util.Arrays;

public class MyStack<E> implements Stack<E> {
	
	private E[] stor = (E[]) new Object[256];
	private int stackPtr;
	
	private void expand(){
		stor = Arrays.copyOf(stor,2*stor.length);
	}
	
	@Override
	public void push(E o) {
		if (stackPtr==stor.length) expand();
		stor[stackPtr++] = o;
	}

	@Override
	public E pop() {
		if (stackPtr==0) throw new RuntimeException("stack is empty!");
		return stor[--stackPtr];
	}

	@Override
	public E top() {
		if (stackPtr==0) throw new RuntimeException("stack is empty!");
		return stor[stackPtr-1];
	}

	@Override
	public int size() {
		return stackPtr;
	}

	@Override
	public boolean isEmpty() {
		return stackPtr==0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
