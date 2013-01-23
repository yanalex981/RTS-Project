package rts.networking.collections;

// seeking [?]
public class SortedList {
	private Node root;
	private Node end;
	private int size = 0;
	
	public SortedList() {}
	
	public SortedList(int n) {
		root = new Node(n);
		end = root;
		size = 1;
	}
	
	private class Node {
		int value;
		Node next = null;
		
		Node(int n) {
			value = n;
		}
		
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
	
	public void add(int n) {
		if (size == 0) {
			root = new Node(n);
			end = root;
			++size;
			return;
		}
		else {
			if (n == root.value)
				return;
			
			if (n < root.value) {
				Node node = new Node(n);
				node.next = root;
				root = node;
				++size;
				return;
			}
		}
		
		Node temp = root;
		
		while (temp != null) {
			if (temp.next != null) {
				if (n == temp.next.value)
					return;
				
				if (n > temp.next.value)
					temp = temp.next;
				else {
					Node node = new Node(n);
					node.next = temp.next;
					temp.next = node;
					++size;
					return;
				}
			}
			else {
				temp.next = new Node(n);
				end = temp.next;
				++size;
				return;
			}
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
		if (size > 0) {
			if (root.value == n) {
				root = root.next;
				--size;
			}
			else {
				Node temp = root;
				
				while (temp != null) {
					if (temp.next != null) {
						if (temp.next.value == n) {
							temp.next = temp.next.next;
							--size;
							break;
						}
					}
					
					temp = temp.next;
				}
			}
		}
	}
	
	public boolean exists(int n) {
		if (size > 0) {
			Node temp = root;
			
			while (temp != null) {
				if (n < temp.value) {
					return false;
				}
				
				if (temp.value == n) {
					return true;
				}
				
				temp = temp.next;
			}
		}
		
		return false;
	}
	
	public int largest() {
		return end.value;
	}
	
	public int size() {
		return size;
	}

	public void printContents() {
		Node temp = root;
		
		while (temp != null) {
			System.out.print(temp.value);
			temp = temp.next;
		}
	}
}
