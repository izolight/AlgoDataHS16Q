package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Random;


/**
 * @author ps
 * Various sort programs for int arrays (exercise) 
 */
public class SortTest {
	
	
	public static long cnt;
	static Random rand = new Random();
	static int [] b;

	/**
	 * @param a int aray
	 * @return 'true' if 'a' is sorted 
	 */
	public static boolean sortCheck(int[] a) {
		for (int i=0;i<a.length-1;i++){
			if (a[i]>a[i+1]) return false; 
		}
		return true;
	}	

	/**
	 * Non optimized bubble sort for an int array 
	 * @param a
	 */
	public static void bubbleSort(int[] a) {
		cnt=0;
		int m = a.length-1;
		for(int i=m; i>0; i--){ 

			for (int k=0; k < i; k++){
				if(a[k]>a[k+1]) swap(a,k,k+1);
			}
			// now a[i] is on its final position!
		}
	}

	public static boolean isHeap(int [] a){
		for (int i=1;i<a.length;i++) if (a[i]>a[(i-1)/2]) return false;
		return true;
	}
	
	public static void heapSort(int []a){
//		for (int i=1; i< a.length;i++) upHeap(a,i);
		for (int i=a.length/2;i>=0;i--) downHeap(a,i,a.length);
		System.out.println(isHeap(a));
		for (int i=a.length-1;i>=0;i--){
			swap(a,0,i);
			downHeap(a,0,i);
		}
		
	}
	
	private static void downHeap(int[] a, int pos, int len) {
		// precondition: a[0..len-1] is a correct maxHeap
		// with exception of a[pos] which is ev. too small.
		// Swap a[pos] with the bigger of the two children
		// until heap ok.
		int left = pos*2+1;
		int right = left+1;
		while(left<len){
			int max = left;
			if (right<len && a[right]>a[left]) max = right;
			if (a[max]<=a[pos]) return;
			swap(a,pos,max);
			pos=max;
			left=pos*2+1;
			right=left+1;
		}	
	}

	private static void upHeap(int[] a, int pos) {
		// precondition a[0..pos-1] is a maxheap
		// postcondition: a[0..pos] is a maxheap
		while (pos>0){
			int parent = (pos-1)/2;
			if (a[parent]>=a[pos]) return;
			swap(a,pos,parent);
			pos = parent;
		}
	}

	public static void mergeSort(int [] a){
		b=new int[a.length]; // work array
		mSort(a,0,a.length-1);
	}
	
	private static void mSort(int[] a, int from, int to) {
		if (from==to) return; 
		int med  = (from+to)/2;
		mSort(a,from,med); // sort first half
		mSort(a,med+1,to); // sort second half
		merge(a,from,med,to); // merge the two parts
	}

	private static void merge(int[] a, int from, int med, int to) {
		// precondition:
		// a[from..med] and a[med+1..to] are already sorted
		// create a sorted sequence in b[from..to]
		int left=from,right=med+1,i=from;
		while (left<=med){
			if (right>to){
				// copy the rest of the first section
				while(left<=med) b[i++]=a[left++];
				break;
			}
			// take the smaller of the two candidates:
			else if (a[left]<=a[right]) b[i++]=a[left++];
			else b[i++]=a[right++];
		}
		// copy b[from..to] back to a[from..to]
		while(--i>= from){
			a[i]=b[i];
		}
	}

	/**
	 * swap the array elements a[i] and a[k]
	 * @param a int array 
	 * @param i position in the array 'a'
	 * @param k position in the array 'a'
	 */
	static void swap(int [] a, int i, int k){
		int tmp=a[i];
		a[i]=a[k];
		a[k]=tmp;
		cnt++;
	}

	static void quickSelect(int a[], int rank){
		// on return the following condition helds:
		// a[0..rank-1] <=  a[rank] <= a[rank+1..a.length-1]
		qSelect(a,0,a.length-1,rank);
	}
	
	private static void qSelect(int[] a, int from, int to, int rank) {
		// on return the following condition helds:
		// a[from..rank-1] <=  a[rank] <= a[rank+1..to]
		int piv = partition(a, from, to);
		if (piv==rank) return;
		else if(piv < rank) qSelect(a,piv+1,to,rank);
		else qSelect(a,from,piv-1,rank);
	}
	
	
	static void quickSort(int[]a){
		qSort(a,0,a.length-1);
	}
	
	

	
	private static void qSort(int[] a, int from, int to) {
		if (from>=to) return;
		int p = partition(a, from, to); 
		// now a[p] is on its final position
		qSort(a,from,p-1);
		qSort(a,p+1,to);
	}

	private static int partition(int[] a, int from, int to) {
		// after return of 'p' a[0..p-1]<= a[p] and
		// a[p]<=a[p+1..to]
		if (to > from) swap(a,to,from+rand.nextInt(to-from));
		int left=from-1;
		int right = to;
		int pivot = a[to];
		while(true){
			while(a[++left]  < pivot);  // we found an element to swap at a[left]
			while(a[--right] > pivot && right>from);
				// either 'right' met 'left' or we found an element to swap at a[right]
			if (left>=right) break;
			swap(a,left,right);
		}
		// final swap (put pivot to its definitive position)
		swap(a,left,to);
		return left;
	}

	public static void main(String[] args) {
		long t1=0,t2=0,te1=0,te2=0,eTime=0,time=0;
		int n = 50000000;
		// we need a random generatorbreak;
		Random rand=new Random(Integer.MAX_VALUE);
		rand.setSeed(8237493); // initialize always in the same state
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();	
		// new array
		int [] a = new int[n];
		// fill it randomly
		for (int i=0;i<a.length ;i++) {
			a[i]=i;rand.nextInt(n);
		}

		// mix: a little bit 
		for (int i=0;i<a.length ;i++) {
			swap(a,i,rand.nextInt(n-1));
		}
		
		cnt=0;  // for statistics reasons
		// get Time
		te1=System.nanoTime();
		t1 = threadBean.getCurrentThreadCpuTime();
		quickSelect(a,100);
		System.out.println(a[100]);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		time=t2-t1;
		eTime=te2-te1;
		System.out.println("# elements: "+n);
		System.out.println("CPU-Time usage: "+time/1000000.0+" ms");
		System.out.println("elapsed time: "+eTime/1e6+" ms");
		System.out.println("sorted? "+sortCheck(a));
		System.out.println("swap operation needed: "+cnt);		
		// ok
	}


}
