package examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
		
		boolean isExternal(){
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
		AVLNode ret = null;
		AVLNode n = root;
		while ( !  n.isExternal()){
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
		ArrayList<AVLNode> al = new ArrayList<>();
		findAll(root,key,al);
		System.out.println("---"+al.size());
		return al.toArray(new Locator[0]);
	}

	private void findAll(AVLNode n, K key, ArrayList<AVLNode> al) {
		if (n.isExternal()) return;
		int comp = key.compareTo(n.key);
		if (comp<0) findAll(n.left, key, al);
		else if (comp>0) findAll(n.right,key, al);
		else {
			findAll(n.left,key,al);
			al.add(n);
			findAll(n.right,key,al);			
		}
	}

	@Override
	public Locator<K, E> insert(K key, E o) {
		AVLNode n = root;
		while( ! n.isExternal()){
			if (key.compareTo(n.key)>=0){
				n=n.right;
			}
			else n=n.left;
		}
		n.expand(key,o);
		size++;
		return n;
	}

	
	
	@Override
	public void remove(Locator<K, E> loc) {

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
	
	private void adjustHeightAboveAndRebalance(AVLNode n){
		n=n.parent;
		while(n!=null){
		  // ....
		}
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

	
	public static void main(String[] args) {
		MyAVLTree<Integer,Object> t = new MyAVLTree<>();
		Random r = new Random();
		t.insert(7,"");
		t.insert(6,"");
		Locator p1 =t.insert(4,"");
		t.insert(18,"");
		t.insert(12,"");
		t.insert(5,"");
		t.insert(3,"");
		t.insert(6,"");
		Locator p2 = t.insert(4,"");
		t.insert(6,"");
		t.printKeys();
		System.out.println(t.find(4).key());
		System.out.println(t.find(4)==p2);
		Locator[] tr = t.findAll(4);
		for(int i=0;i<tr.length;i++) System.out.println(tr[i].key());
	}

	private void printKeys() {
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

	
}
