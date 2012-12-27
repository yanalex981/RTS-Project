package rts.networking.unit_testing;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import rts.networking.collections.SortedList;

/*
 * TEST adding
 * TEST removing
 * TEST seeking
 * TEST exists
 * TEST get largest value
 */

public class Collections {
	SortedList list;
	
	@Before
	public void prep() {
		list = new SortedList();
	}
	
	@Test
	public void addTest() {
		list.add(3);
		list.add(4);
		list.add(6);
		list.add(3);
		
		if (list.size() != 3) {
			fail("Add Test Failed: Bad size");
		}
	}
	
	@Test
	public void removeTest() {
		list.add(3);
		list.add(4);
		list.add(6);
		list.add(3);
		
		list.remove(3);
		
		if (list.size() != 2) {
			fail("Remove Test Failed: Bad Size");
		}
	}
	
	@Test
	public void existsTest() {
		list.add(3);
		list.add(4);
		list.add(6);
		list.add(3);
		
		if (!list.exists(3) || !list.exists(3) || !list.exists(3)) {
			fail("Existence Test Failed: Not all elements exist");
		}
	}
	
	@Test
	public void largestValueTest() {
		list.add(3);
		list.add(4);
		list.add(6);
		list.add(3);
		
		if (list.largest() != 6) {
			fail("Largest Value Test Failed: Incorrect Largest Value");
		}
	}
}
