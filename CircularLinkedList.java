import java.util.Iterator;

public class CircularLinkedList<E> implements Iterable<E> {

// variables
	Node<E> head;
	Node<E> tail;
	int size; 

// constructor method
	public CircularLinkedList() {
		head = tail = null;
		size = 0;
	}

// size method	
	public int size() {
		return size;
	}
// getNode method
	public Node<E> getNode(int index ) {
		if(index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index out of bounds.");
		Node<E> current = head;
		for(int i = 0; i < index; i++)
			current = current.next;
		return current;
	}
// get method
	public E get(int index) {
		return getNode(index).item;
	}


// add method (end of list)
	public boolean add(E item) {
		this.add(size,item);
		return true;
	}

// add method	
	public void add(int index, E item){
		if (index < 0 || index > size) //IOOB 
			throw new IndexOutOfBoundsException("Index out of bounds.");
		
		Node<E> adding = new Node(item);

		if(size == 0) { //empty list
			this.head = this.tail = adding;

		} else if(index == 0) { //adding head
			adding.next = head;
			tail.next = adding;
			head = adding;

		} else if(index == size) { //adding tail
			tail.next = adding;
			adding.next = head;
			tail = adding;

		} else{ //adding anywhere else
			Node<E> before = getNode(index - 1);
			adding.next = before.next;
			before.next = adding;

		}
		size++;
	}

// remove method
	public E remove(int index) {
		if (index < 0 || index > size) //IOOB
			throw new IndexOutOfBoundsException("Index out of bounds.");
		E toReturn = null;
		if(size == 1) { //removing only item
			toReturn = head.item;
			head = tail = null;
		} else if(index == 0){ //removing head
			toReturn = head.item;
			head = head.next;
			tail.next = head;
		} else if(index == size - 1){ //removing tail
			Node<E> before = getNode(index - 1);
			toReturn = tail.item;
			before.next = head;
			tail = before;
		} else{ //removing anywhere else
			Node<E> before = getNode(index - 1);
			toReturn = before.next.item;
			before.next = before.next.next;
		}
		size--;
		return toReturn;
	}
	
// toString method
	public String toString(){
		Node<E> current =  head;
		StringBuilder result = new StringBuilder();
		if(size == 0){
			return "";
		}
		if(size == 1) {
			return head.item.toString();
		}
		else{
			do{
				result.append(current.item);
				result.append(" ==> ");
				current = current.next;
			} while(current != head);
		}
		return result.toString();
	}
	
	
	public Iterator<E> iterator() {
		return new ListIterator<E>();
	}
	
	// provided code for different assignment
	// you should not have to change this
	// change at your own risk!
	// this class is not static because it needs the class it's inside of to survive!
	private class ListIterator<E> implements Iterator<E>{
		
		Node<E> nextItem;
		Node<E> prev;
		int index;
		
		@SuppressWarnings("unchecked")
		//Creates a new iterator that starts at the head of the list
		public ListIterator(){
			nextItem = (Node<E>) head;
			index = 0;
		}

		// returns true if there is a next node
		// this is always should return true if the list has something in it
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return size != 0;
		}
		
		// advances the iterator to the next item
		// handles wrapping around back to the head automatically for you
		public E next() {
			// TODO Auto-generated method stub
			prev =  nextItem;
			nextItem = nextItem.next;
			index =  (index + 1) % size;
			return prev.item;
	
		}
		
		// removed the last node was visted by the .next() call 
		// for example if we had just created a iterator
		// the following calls would remove the item at index 1 (the second person in the ring)
		// next() next() remove()
		public void remove() {
			int target;
			if(nextItem == head) {
				target = size - 1;
			} else{ 
				target = index - 1;
				index--;
			}
			CircularLinkedList.this.remove(target); //calls the above class
		}
		
	}
	
	// It's easiest if you keep it a singly linked list
	// SO DON'T CHANGE IT UNLESS YOU WANT TO MAKE IT HARDER
	private static class Node<E>{
		E item;
		Node<E> next;
		
		public Node(E item) {
			this.item = item;
		}
		
	}
	
}
