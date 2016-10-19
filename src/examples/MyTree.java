package examples;

import java.util.Iterator;

public class MyTree<E> implements Tree<E> {

	private class TNode implements Position<E>{
		E elem;
		
		MyLinkedList<TNode> children = new MyLinkedList<>();
		Object creator = MyTree.this;
		Position<TNode> myChildrenPos; 	// position in the children 
										// list of my parent		
		TNode parent;
		@Override
		public E element() {
			return elem;
		}
		
	}
	
	private TNode root;
	private int size;

	private TNode checkAndCast(Position<E> p) {
		TNode n;
		try {
			n = (TNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyTree"); 
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyTree instance!");			
		return n;
	}

	@Override
	public Position<E> root() {
		return root;
	}
	
	@Override
	public Position<E> createRoot(E o) {
		if (size != 0) throw new RuntimeException("Tree is not empty");
		root = new TNode();
		root.elem = o;
		size++;
		return root;
	}

	@Override
	public Position<E> parent(Position<E> child) {
		return checkAndCast(child).parent;
	}

	@Override
	public Iterator<Position<E>> childrenPositions(Position<E> parent) {
		TNode np = checkAndCast(parent);
		return new Iterator<Position<E>>(){
			Iterator<TNode> it = np.children.elements();
			public boolean hasNext() {
				return it.hasNext();
			}
			@Override
			public Position<E> next() {
				return it.next();
			}
			
		};
	}

	@Override
	public Iterator<E> childrenElements(Position<E> parent) {
		TNode np = checkAndCast(parent);
		return new Iterator<E>(){
			Iterator<TNode> it = np.children.elements();
			public boolean hasNext() {
				return it.hasNext();
			}
			@Override
			public E next() {
				return it.next().elem;
			}			
		};
	}

	@Override
	public int numberOfChildren(Position<E> parent) {
		TNode p = checkAndCast(parent);
		return p.children.size();
	}

	@Override
	public Position<E> insertParent(Position<E> p, E o) {
		TNode n = checkAndCast(p);
		TNode newP = new TNode();
		newP.elem=o;
		if (n == root){
			root = newP;
		}
		else {
			// newP takes the former role of n:
			newP.parent = n.parent;
			newP.myChildrenPos = n.myChildrenPos; // we take the position of p
			newP.parent.children.replaceElement(newP.myChildrenPos,newP);		
		}
		//make 'p' a child of the new position
		n.parent = newP;
		n.myChildrenPos = newP.children.insertFirst(n);
		size++;
		return newP;			
	}

	@Override
	public Position<E> addChild(Position<E> parent, E o) {
		TNode np = checkAndCast(parent);
		TNode  n = new TNode();
		n.parent = np;
		n.myChildrenPos = np.children.insertLast(n);
		n.elem = o;
		size++;
		return n;
	}

	@Override
	public Position<E> addChildAt(int pos, Position<E> parent, E o) {
		TNode p = checkAndCast(parent);
		if (pos > p.children.size()|| pos < 0 ) throw new RuntimeException("invalid rank");
		TNode n = new TNode();
		n.elem = o;
		n.parent = p;
		Position<TNode> linkedListPosition = null;
		if (pos == 0) linkedListPosition = p.children.insertFirst(n); 
		else if (pos == p.children.size()) linkedListPosition = p.children.insertLast(n); 
		else {
			Iterator<Position<TNode>> it = p.children.positions();
			// skip pos-2 nodes
			for (int i=0;i<pos-1;i++){
				it.next();
			}
			Position<TNode> lPos = (it.next()); // lPos is the LinkedList-position before the insertion point
			linkedListPosition = p.children.insertAfter(lPos, n);
		}
		n.myChildrenPos = linkedListPosition;
		size++;
		return n;
	}

	@Override
	public Position<E> addSiblingAfter(Position<E> sibling, E o) {
		TNode s = checkAndCast(sibling);
		if (sibling == root) throw new RuntimeException("root cannot have siblings!");
		TNode n = new TNode();
		n.elem = o;
		n.parent = s.parent;
		n.myChildrenPos = n.parent.children.insertAfter(s.myChildrenPos,n);
		size++;
		return n;
	}

	@Override
	public Position<E> addSiblingBefore(Position<E> sibling, E o) {
		TNode s = checkAndCast(sibling);
		if (sibling == root) throw new RuntimeException("root cannot have siblings!");
		TNode n = new TNode();
		n.elem = o;
		n.parent = s.parent;
		n.myChildrenPos = s.parent.children.insertBefore(s.myChildrenPos,n);
		size++;
		return n;
	}

	@Override
	public void remove(Position<E> p) {
		TNode np = checkAndCast(p);
		if (np.children.size() != 0) throw new RuntimeException("cannot remove node with children!");
		if (np!= root) {
			np.parent.children.remove(np.myChildrenPos);
		}
		else root = null;
		size--;
		np.creator = null;
	}

	@Override
	public boolean isExternal(Position<E> p) {
		TNode n = checkAndCast(p);
		return n.children.size() == 0;
	}

	@Override
	public boolean isInternal(Position<E> p) {
		TNode n = checkAndCast(p);
		return n.children.size() != 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		TNode np = checkAndCast(p);
		E ret = np.elem;
		np.elem = o;
		return ret;
	}
	
	public void print(){
		if (size!=0)print(root,"");
	}

	private void print(TNode r,String ind) {
		System.out.println(ind+r.elem);
		Iterator<TNode> it = r.children.elements();
		while (it.hasNext()) print(it.next(),ind+"  ");
 	}

	public void removeSubtree(Position<E> p){
		TNode n = checkAndCast(p);
		Iterator<TNode> it = n.children.elements();
		while (it.hasNext()) remove(it.next());
		remove(n);
	}
	
	public static void main(String[] args) {
		MyTree<String> t = new MyTree<>();
		Position<String>p = t.createRoot("buch");
		t.addChild(p, "kapitel 1");
		Position<String>p2 = t.addChild(p, "kapitel 2");
		Position<String>p3 = t.addChild(p2, "kapitel 2.1");
		t.addChild(p, "kapitel 3");
		t.addChild(p, "kapitel 4");
		Position<String> p4 = t.addChild(p3, "kapitel 2.1.1");
		t.addSiblingAfter(p4, "kapitel 2.1.2");
		t.print();
//		t.removeSubtree(p3);
		t.print();
		System.out.println(height(t));
		System.out.println(deepestPos(t).element());
		
	}

	static int height(MyTree t){
		if (t.size == 0) return 0;
		return height(t, t.root());
	}
	
	static class Temp {
		int d;
		Position deepest;
	}
	
	static int maxDepth;
	static Position deepestPos;

	static Position deepestPos(MyTree t){
		if (t.size()==0) return null;
		Temp tmp = new Temp();
		traverse(t,t.root(),1,tmp);
		return tmp.deepest;
	}
	
	static void traverse(Tree t, Position p, int depth,Temp tmp){
		if (depth>tmp.d){
			tmp.d = depth;
			tmp.deepest = p;
		}
		Iterator<Position> it = t.childrenPositions(p);
		while (it.hasNext()) traverse(t, it.next(), depth+1,tmp);
		
	}
	
	static int height(MyTree t, Position p){
		int max = 0;
		Iterator<Position> it = t.childrenPositions(p);
		while (it.hasNext()){
			int h = height(t,it.next());
			if (max<h) max = h;
		}
		return max+1;
	}
}
