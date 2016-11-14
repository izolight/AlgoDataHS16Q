package examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


public class MyAVLTree<K extends Comparable<? super K>, E> implements
		OrderedDictionary<K, E> {

	class AVLNode implements Locator<K,E>{
		K key;
		E elem;
		int height;
		
		AVLNode parent,left,right;
		Object creator = MyAVLTree.this;
		
		@Override
		public E element() {
			return elem;
		}

		@Override
		public K key() {
			return key;
		}
		
		boolean isExternal(){
			return left == null; // right would also be null!
		}
		
		boolean isLeftChild(){
			return parent!=null && parent.left==this;
		}
		
		boolean isRightChild(){
			return parent!=null && parent.right==this;
		}
		
		void expand(K key, E elem){
			this.key=key;
			this.elem=elem;
			this.left = new AVLNode();
			this.right = new AVLNode();
			left.parent=right.parent=this;
			height=1;
		}
		
	}

	private AVLNode root = new AVLNode();
	private int size;
	

	private AVLNode checkAndCast(Locator<K,E> p) {
		AVLNode n;
		try {
			n = (AVLNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Locator belonging to MyLinkedList"); 
		}
		if (n.creator == null) throw new RuntimeException("locator was allready deleted!");
		if (n.creator != this) throw new RuntimeException("locator belongs to another MyLinkedList instance!");			
		return n;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Locator<K, E> find(K key) {
		AVLNode ret = null;
		// find the leftmost item with 'key'
		// (null if there is no such element)
		AVLNode n = root;
		while (! n.isExternal()) {
			int comp = key.compareTo(n.key);
			if (comp<0) n=n.left;
			else if (comp>0) n=n.right;
			else {
				ret = n;
				n=n.left;
			}
		}	
		return ret;
	}

	@Override
	public Locator<K, E>[] findAll(K key) {
		// finds all elements (sorted from left to right)
		// with 'key'
		LinkedList<AVLNode> al = new LinkedList<>(); 
		if (! root.isExternal()) findAll(key,root,al);
		return al.toArray(new Locator[0]);
	}

	private void findAll(K key, AVLNode n, LinkedList<AVLNode> al){
		if (n.isExternal()) return;
		int comp = key.compareTo(n.key);
		if (comp<0) findAll(key,n.left,al);
		else if (comp>0) findAll(key,n.right,al);
		else {
			findAll(key,n.left,al);
			al.addLast(n);
			findAll(key,n.right,al);
		}
	}
	
	@Override
	public Locator<K, E> insert(K key, E o) {
		AVLNode n = root;
		while ( ! n.isExternal()){
			if (key.compareTo(n.key)<0) n=n.left;
			else n=n.right;
		}
		n.expand(key,o);
		adjustHeightAboveAndRebalance(n);
		size++;
		return n;
	}

	@Override
	public void remove(Locator<K, E> loc) {
		AVLNode n = checkAndCast(loc);
		AVLNode w = null; 	// 'w' will be the node which took
							// the position of the node which we
							// removed really.
		if ( ! n.left.isExternal() && ! n.right.isExternal()){
			// we have no external child. Therefore
			// we remove the right-most node 'v' on the left of 'n'
			AVLNode v = n.left;
			while ( ! v.right.isExternal()) v = v.right;
			w = removeAboveExternal(v);
		
			// now we replace 'n' by 'v'
			v.height = n.height;
			v.left = n.left;
			v.right = n.right;
			v.parent = n.parent;
			
			// backwards chaining:
			v.left.parent = v;
			v.right.parent = v;
			if (n.isLeftChild()) v.parent.left = v;
			else if (n.isRightChild())v.parent.right = v;
			else root = v;
		}
		else {
			w = removeAboveExternal(n);
		}
		// invalidate 'n' and adjust the height and rebalance 
		// the tree above 'w'
		size--;
		n.creator = null;
		adjustHeightAboveAndRebalance(w);
	}	

	@Override
	public Locator<K, E> closestBefore(K key) {
		if (size==0) return null;
		AVLNode n = root;
		AVLNode found = null;
		while ( ! n.isExternal()){
			int comp = key.compareTo(n.key);
			if (comp < 0) n = n.left;
			else if (comp > 0) n=n.right;
			else {
				found = n;
				n=n.left;
			}			
		}
		if (found != null) return previous(found);
		// we are at a external position
		if (n.isRightChild()) return n.parent;
		return previous(n.parent);
	}

	@Override
	public Locator<K, E> closestAfter(K key) {
		if (size==0) return null;
		AVLNode n = root;
		AVLNode found = null;
		while ( ! n.isExternal()){
			int comp = key.compareTo(n.key);
			if (comp > 0) n = n.right;
			else if (comp < 0) n=n.left;
			else {
				found = n;
				n=n.right;
			}			
		}
		if (found != null) return next(found);
		if (n.isLeftChild()) return n.parent;
		return next(n.parent);	}

	@Override
	public Locator<K, E> next(Locator<K, E> loc) {
		AVLNode n = checkAndCast(loc);
		if (n.right.isExternal()){
			// next is above n
			// so we find the first ancester which 
			// is on the right of n
			while (n.isRightChild()) n=n.parent;
			n=n.parent;
		}
		else {
			// next is in the right subtree
			// find the most left in the right subtree
			n=n.right;
			while ( ! n.left.isExternal())n=n.left;
		}
		return n;
	}

	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		AVLNode n = checkAndCast(loc);
		if (n.left.isExternal()){
			while(n.isLeftChild()) n=n.parent;
			n=n.parent;
		}
		else {
			n=n.left;
			while (! n.right.isExternal()) n=n.right;
		}
		return n;
	}

	@Override
	public Locator<K, E> min() {
		if (size == 0) return null;
		AVLNode n = root;
		while (! n.left.isExternal()) n=n.left;
		return n;
	}

	@Override
	public Locator<K, E> max() {
		if (size == 0) return null;
		AVLNode n = root;
		while (! n.right.isExternal()) n=n.right;
		return n;
	}

	@Override
	public Iterator<Locator<K, E>> sortedLocators() {
		return new Iterator<Locator<K,E>>() {
			AVLNode current = (AVLNode)min();
			
			@Override
			public boolean hasNext() {
				return current == null;
			}

			@Override
			public Locator<K, E> next() {
				AVLNode ret = current; // this is the next node
				//  by one node:
				if (current.right.isExternal()){
					while(current.isRightChild()) current=current.parent;
					current=current.parent;
				}
				else {
					current=current.right;
					while (! current.left.isExternal()) current=current.left;
				}
				return ret;
			}
		};
	}
	
	private AVLNode restructure(AVLNode n) {

		// n is unbalanced
		// returns the node that takes the position of n
		AVLNode p=n.parent,z=n,x=null,y=null,
		a=null,b=null,c=null, t1=null,t2=null; 
		// t0 and t3 never change their parent, 
		// that's why we don't need them 
		if (z.left.height > z.right.height){
			//   z
			//  /
			// y
			c=z;
			y=z.left;
			if (y.left.height >= y.right.height){
				// in case we have two equal branches
				// concidering the length we take always the single
				// rotation
				//     z
				//    /
				//   y
				//  /
				// x
				x=y.left;
				t1=x.right;
				t2=y.right;
				b=y;
				a=x;
			}
			else {
				//     z
				//    /
				//   y
				//   \  
				//    x
				x=y.right;
				t1=x.left;
				t2=x.right;
				a=y;
				b=x;
			}
		}
		else{
			// z
			//   \
			//    y
			a=z;
			y=z.right;
			if (y.right.height >= y.left.height){
				//  z
				//   \
				//    y
				//     \  
				//      x
				x=y.right;
				b=y;
				c=x;
				t1=y.left;
				t2=x.left;
			}
			else {
				//  z
				//   \
				//    y
				//    /  
				//   x
				x=y.left;
				b=x;
				c=y;
				t1=x.left;
				t2=x.right;
			}
		}		
		// umhaengen
		b.parent = p;
		if (p != null){
			if (p.left == z) {
				p.left=b;
			}
			else p.right=b;
		}
		else {
			root=b;
		}
		b.right = c;
		b.left = a;
		// und umgekehrt
		a.parent = b;
		c.parent = b;

		// subtrees:
		a.right = t1;
		t1.parent = a;
		c.left = t2;
		t2.parent = c;
		
		
		a.height = Math.max(a.left.height, a.right.height)+1;
		c.height = Math.max(c.left.height, c.right.height)+1;
		// now we can calculate the height of b
		b.height = Math.max(b.left.height, b.right.height)+1;
		return b;
	}

	
	public void printKeys() {
		prittyPrint(root,"");
	}

	private void printKeys(AVLNode n, String ind) {
		if (n.isExternal()) return;
		printKeys(n.right,ind+"-");
		System.out.println(ind+n.key);
		printKeys(n.left,ind+"-");
	}

	private void prittyPrint(AVLNode r, String in) {
		if (r.isExternal()) return;		
		// right subtree 
		int sLen = in.length();
		String inNeu = in;
		if (r.isRightChild()) inNeu = in.substring(0,sLen-2)+"  ";
		prittyPrint(r.right,inNeu+" |");
		// root of the subtree
		String inN = in;
		if (sLen>0) inN = in.substring(0,sLen-1)+"+-";
		else inN = in+"-"; // root of the tree
		if ( ! r.right.isExternal()) System.out.println(inNeu+" |");
		else System.out.println(inNeu);
		System.out.println(inN+r.key());//+"(h="+r.height+")"+":"+r.elem+")"); 
		// left subtree
		inNeu = in;
		if (r.isLeftChild()){
			inNeu = in.substring(0,sLen-2)+"  ";
		}
		prittyPrint(r.left,inNeu+" |");
	}

	private void adjustHeightAboveAndRebalance(AVLNode n){
		n=n.parent;
		while(n!=null){
			int newHeight = 1+Math.max(n.left.height,n.right.height);
			boolean balanced = Math.abs(n.left.height-n.right.height)<2;
			if (n.height == newHeight && balanced) return;
			n.height=newHeight;			
			if ( ! balanced) n=restructure(n);
			n=n.parent;
		}
	}
	
	private AVLNode removeAboveExternal( AVLNode n) {
		// remove n and return the node which takes the place of n
		AVLNode ret = null;
		if (n.left.isExternal()){
			// replace n by its right child
			ret = n.right;
		}
		else {
			// replace n by its left child
			ret = n.left;
		}
		// chaining:
		ret.parent = n.parent;
		if (n.isLeftChild()) n.parent.left = ret;
		else if (n.isRightChild()) n.parent.right = ret;
		else root=ret;
		return ret;
	}
	
	public void test(){
		if (root.parent!=null) throw new RuntimeException("root has parent!");
		if (size>0) test(root);
	}


	private void test(AVLNode n) {
		if (n.isExternal()) return;
		test(n.left);
		test(n.right);
		if (n.left.parent != n) throw new RuntimeException("chaining incorrect"+n.key);
		if (n.right.parent != n) throw new RuntimeException("chaining incorrec"+n.key);
		if (Math.max(n.left.height,n.right.height)+1!=n.height)
			throw new RuntimeException("Height wrong"+n.key);
		if (n.left.key !=null &&  n.left.key.compareTo(n.key)>0) throw new RuntimeException("order wrong "+n.key); 
		if (n.right.key !=null &&  n.right.key.compareTo(n.key)<0) throw new RuntimeException("order wrong "+n.key);
		if (Math.abs(n.left.height-n.right.height)> 1) throw new RuntimeException("unbalanced "+n.key);
		if (n.creator != this) throw new RuntimeException("invalid node: "+n.key);
	}
	
	
	public static void main(String[] args) {
		MyAVLTree<Integer,Object> t = new MyAVLTree<>();
		Random r = new Random(214);
		final int N = 1000000;
		Locator [] locs = new Locator[N]; 
		long t1 = System.nanoTime();
		for (int i=0;i<N;i++)locs[i]= t.insert(r.nextInt(N),"i");
//		for (int i=0;i<N*0.9;i++) t.remove(locs[i]);
		long t2 = System.nanoTime();
		System.out.println("inserted "+N+" nodes. Time [s]: "+((t2-t1)*1e-9));
		System.out.println(t.root.height);
		t.test();
//		Locator loc = locs[0];
//		for (int i=0;i<100;i++) {
//			System.out.println(loc.key());
//			loc = t.next(loc);
//		}
//		t.insert(7,"");
//		t.insert(19,"");
//		t.insert(10,"");
//		t.insert(4,"1");
//		t.insert(1,"");
//		t.insert(3,"");
//		t.insert(4,"2");
//		t.insert(4,"3");
//		t.insert(4,"4");
//		t.insert(4,"5");
//		t.insert(4,"6");
//		System.out.println(t.find(4).element());
//		Locator[] f=t.findAll(4);
//		for (int i= 0;i<f.length;i++) System.out.println(f[i].element());
//		t.printKeys();
	}


}
