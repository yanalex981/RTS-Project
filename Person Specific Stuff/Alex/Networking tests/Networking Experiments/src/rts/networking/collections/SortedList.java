package rts.networking.collections;

/**
 * Resizable linked list that stores integers
 * as unit IDs. Only stores unique integers
 * so that units won't share the same ID
 * 
 * @author Alex
 */
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
	
	/**
	 * iterative method to add an ID to the list
	 * 
	 * @param n
	 */
	public void add(int n) {
		/*
		 * this is iterative because I wanted to see
		 * how much faster it is compared to recursive.
		 * Not much different, but this one is slightly
		 * faster
		 */
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
	
	/**
	 * Returns all integers in this array as a string
	 * Used for Eclipse's debugging display
	 */
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
	
	/**
	 * Removes a unit ID from the list
	 * 
	 * @param n id to remove
	 */
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
	
	/**
	 * checks whether or not an ID exists
	 * 
	 * @param n	id
	 * @return	true if id exists, false if otherwise
	 */
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
	
	/**
	 * returns the largest ID
	 * 
	 * @return	largest ID that exists
	 */
	public int largest() {
		return end.value;
	}
	
	/**
	 * returns the size of the list
	 * 
	 * @return	return size of array
	 */
	public int size() {
		return size;
	}

	/**
	 * Prints contents of the list to the system console
	 */
	public void printContents() {
		Node temp = root;
		
		while (temp != null) {
			System.out.print(temp.value);
			temp = temp.next;
		}
	}
}
