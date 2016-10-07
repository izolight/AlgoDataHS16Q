package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;

public class MyStack<E> implements Stack<E> {
	
	private E[] stor;
	private int stackPtr;
	private int cnt; // count expansions
	
	private void expand(){
		cnt++;
		stor = Arrays.copyOf(stor,2*stor.length);
	}
	
	public MyStack(int initLen){
		stor = (E[]) new Object[initLen];
	}
	
	public MyStack(){
		stor = (E[]) new Object[1]; 
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
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();	
		final int N=16*1024*1024+2; 
		long t1,t2,te1,te2;
		MyStack<Integer> st = new MyStack<>(N);
		ArrayList<Integer> al = new ArrayList<Integer>(N);
		t1 = threadBean.getCurrentThreadCpuTime();
		te1 = System.nanoTime();
		for (int i=0;i<N;i++) {
			al.add(i);
		}
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		System.out.println("ArrayList: time to stor "+N+" elements:[s] "+1E-9*(te2-te1));
		System.out.println(" cpu time:[s] "+1E-9*(t2-t1));
		t1 = threadBean.getCurrentThreadCpuTime();
		te1 = System.nanoTime();
		for (int i=0;i<N;i++) st.push(i);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		System.out.println("MyStack: time to stor "+N+" elements:[s] "+1E-9*(te2-te1));
		System.out.println(" cpu time: "+1E-9*(t2-t1));
		System.out.println(st.cnt);
	}

}
