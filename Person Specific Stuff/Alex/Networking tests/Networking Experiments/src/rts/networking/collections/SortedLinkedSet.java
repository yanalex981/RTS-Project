package rts.networking.collections;

public class SortedLinkedSet {
	private Node root;
	private Node end;
	private int size = 0;
	
	public SortedLinkedSet() {}
	
	public SortedLinkedSet(int startValue) {
		root = new Node(startValue);
		end = root;
		++size;
	}
	
	private class Node {
		int value;
		Node next = null;
		
		private Node(int n) {
			value = n;
			end = root;
		}
		
		private void add(int n) {
			if (next == null) {
				next = new Node(n);
				++size;
			}
			else {
				if (n < next.value) {
					Node newNode = new Node(n);
					newNode.next = next;
					next = newNode;
					++size;
				}
				else if (n > next.value) {
					next.add(n);
				}
			}
		}
	}
	
	// TODO Find a way to do this iteratively
	public void add(int n) {
		if (root == null) {
			root = new Node(n);
			++size;
		}
		else {
			if (n < root.value) {
				Node newRoot = new Node(n);
				newRoot.next = root;
				root = newRoot;
				++size;
			}
			else if (n > root.value) {
				root.add(n);
			}
		}
	}
	
	public void printContents() {
		Node temp = root;
		
		while (temp != null) {
			System.out.print(temp.value);
			temp = temp.next;
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		Node temp = root;
		
		while (temp != null) {
			str += " " + String.valueOf(temp.value);
			temp = temp.next;
		}
		
		return str.trim();
	}
	
	public void remove(int n) {
		Node temp = root;
		
		if (size > 0) {
			
		}
		
		while (temp != null && temp.next != null) {
			if (temp.value == n) {
				
			}
			
			temp = temp.next;
		}
	}
	
	public boolean exists(int n) {
		Node temp = root;
		
		while (temp != null) {
			if (temp.value == n) {
				return true;
			}
			
			temp = temp.next;
		}
		
		return false;
	}
	
	public int largest() {
		return end.value;
	}
	
	public int size() {
		return size;
	}
	
	public static void main(String[] args) {
		// TESTING
		SortedLinkedSet sls = new SortedLinkedSet();
		
		sls.add(30);
		sls.add(24);
		sls.add(10);
		sls.add(68);
		sls.add(12);
		sls.add(90);
		sls.add(76);
		sls.add(47);
		sls.add(45);
		
		sls.printContents();
		System.out.println();
		System.out.println(sls.toString());
		System.out.println(sls.exists(-2));
		System.out.println(sls.exists(4));
		System.out.println(sls.size);
	}
}
