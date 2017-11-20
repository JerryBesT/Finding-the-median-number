/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Fall 2017 
// PROJECT:          Running Median
// FILE:             MaxPQ.java
//
// TEAM:    individual
// Author:  Zhenyu Zou
// email: zzou24@wisc.edu
// netID: 907 593 6980
// Lecture number: 001
//
//
// ---------------- OTHER ASSISTANCE CREDITS 
// Persons: Identify persons by name, relationship to you, and email. 
// Describe in detail the the ideas and help they provided. 
// 
// Online sources: Piazza Discussions
//////////////////////////// 80 columns wide //////////////////////////////////


/**
 * GENERAL DIRECTIONS -
 *
 * 1. You may add private data fields and private methods as you see fit.
 * 2. Implement ALL the methods defined in the PriorityQueueADT interface.
 * 3. DO NOT change the name of the methods defined in the PriorityQueueADT interface.
 * 4. DO NOT edit the PriorityQueueADT interface.
 * 5. DO NOT implement a shadow array.
 */

public class MaxPQ<E extends Comparable<E>> implements PriorityQueueADT<E>
{
    private E[] items;
    private static final int INITIAL_SIZE = 10;
    // keep track of the current items in the array
    private int numItems;

    @SuppressWarnings("unchecked")
	public MaxPQ()
    {
        this.items = (E[]) new Comparable[INITIAL_SIZE];
    }

	@Override
	public boolean isEmpty() {
		// if the size of the array is empty then return true
		return size() == 0;
	}

	@Override
	public void insert(E item) {
		
		//if the incoming item contains nothing, then catch the exception.
		if(item == null)
			throw new IllegalArgumentException();
		
		// if the items in queue are equal to the size, then expand the array.
		if(numItems == items.length - 1)
		{
			@SuppressWarnings("unchecked")
			// initialize a new array which is two times bigger than the former one
			E[] newArray = (E[]) new Comparable[items.length * 2];
			
			// use the for loop to copy every single item which was in the small array into the new one.
			for(int i=0;i < numItems + 1;i++)
				// put every item to the new array.
				newArray[i] = items[i];
			
			// point the new array to the original array
			items = newArray;
		}
		
		// as long as the size of the array is empty, then simply return.
		if(numItems == 0)
		{
			// set the first item in the array
			items[1] = item;
			// increment the current items
			numItems++;
			// return the method
			return;
		}
		
		// initialize the boolean statement to execute the method
		boolean isDone = true;
		
		// create a new index to reorder the array
		int tempIndex = numItems + 1;
		
		// set the incoming item to the next index in the array
		items[tempIndex] = item;
		
		// declare a new item child to be the child to reorder
		E child = items[tempIndex];
		
		// as long as isDone is not false, then continue
		while(isDone)
		{
			// initialize a new parent to reorder the array
			E parent = items[tempIndex / 2];
			
			// if the parent of current child contains nothing, then directly exit the loop
			if(parent == null)
			{
				// exit the loop and return the whole method
				isDone = false;
				break;
			}
			// if the parent item is bigger than the current child
			else if(parent.compareTo(item) >= 0)
			{
				// since the parent can not be smaller than child, then simply set the next
				// item to incoming item
				if(tempIndex == numItems)
					items[numItems] = item;
				// exit the loop and return the method
				isDone = false;
				break;
			}
			// if the parent is smaller than child, then swap child and parent items
			else
			{
				// set a temporary variable in case the value is lost
				E temp = items[tempIndex / 2];
				
				// set the parent item to be child
				items[tempIndex / 2] = child;
				
				// set the child item to be parent
				items[tempIndex] = temp;
				
				// update the temporary index
				tempIndex = tempIndex / 2;
			}
		}	
		
		// increment the current items in array
		numItems++;
	}

	@Override
	public E getMax() throws EmptyQueueException {
		// get the first item in maxPQ
		return items[1];
	}

	@Override
	public E removeMax() throws EmptyQueueException {
		// store the first item in case it is lost
		E removed = items[1];
		
		// set the first item to be the last one in array
		items[1] = items[numItems];
		
		// set child to be the last item in array
		E child = items[numItems];
		
		// initialize boolean statement to control loop
		boolean isDone = false;
		
		// set the temporary index to reorder
		int tempIndex = 1;
		
		// if it is not done then continue
		while(!isDone)
		{
			// if the size of the array is smaller than two, then directly return
			if(size() <= 2)
			{
				// decrement the current items in array
				numItems--;
				
				// return the removing item
				return null;
			}
			// it continues as long as two times the temporary index is smaller than size of array
			while(tempIndex * 2 < size())
			{
				// if the "parent" item's next item is smaller
				if(items[tempIndex * 2].compareTo(items[tempIndex * 2 + 1]) > 0 || items[tempIndex * 2 + 1].equals(null))
				{
					// if the child item is smaller than parent item, then it executes
					if(child.compareTo(items[tempIndex * 2]) < 0)
					{
						// update the temporary index
						tempIndex = tempIndex * 2;
						
						// store the temporary variable to be the parent item
						E temp = items[tempIndex];
						
						// swap the child and parent items
						items[tempIndex] = child;
						
						// swapping
						items[tempIndex / 2] = temp;
					}
					// if the child is smaller than the "parent" item
					else
					{
						// exit the loop and return the whole method
						isDone = true;
						break;
					}
					// in case the temporary index gets examined twice
					continue;
				}
				// if the "parent" item's next item is bigger
				if(items[tempIndex * 2].compareTo(items[tempIndex * 2 + 1]) < 0)
				{
					// if child item is smaller than the parent item
					if(child.compareTo(items[tempIndex * 2 + 1]) < 0)
					{
						// update the temporary index
						tempIndex = tempIndex * 2 + 1;
						
						// store the position's item in case it gets lost
						E temp = items[tempIndex];
						
						// swap the child and parent item
						items[tempIndex] = child;
						
						// swapping
						items[(tempIndex - 1) / 2] = temp;
					}
					// if it is bigger then exit
					else
					{
						// exit the loop
						isDone = true;
						
						// return the whole method
						break;
					}
				}
			}
			// exit the loop, in case infinite
			isDone = true;
		}
		// decrement the number of items
		numItems--;
		
		// return the removing max item
		return removed;
	}

	@Override
	public int size() {
		// return the current number of items
		return numItems;
	}
}
